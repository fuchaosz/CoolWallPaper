package com.coolwallpaper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import com.coolwallpaper.bean.IUserInfo;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobUser;
import com.coolwallpaper.event.DownloadPictureFailureEvent;
import com.coolwallpaper.event.DownloadPictureSuccessEvent;
import com.coolwallpaper.event.UpdatePictureEvent;
import com.coolwallpaper.fragment.PictureDetailFragment;
import com.coolwallpaper.fragment.PictureListFragment;
import com.coolwallpaper.model.LocalFavourite;
import com.coolwallpaper.model.LocalFavouriteDao;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.DBUtil;
import com.coolwallpaper.utils.EmptyUtil;
import com.coolwallpaper.utils.FileUtil;
import com.coolwallpaper.utils.NetworkUtil;
import com.coolwallpaper.utils.ToastUtil;
import com.coolwallpaper.utils.UserUtil;
import com.library.common.util.ScreenUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 显示图片详情
 * Created by fuchao on 2015/10/21.
 */
public class ShowPictureDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_LOGIN = 0x100;//登录界面请求码
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";//调用uCrop之前必须给图片一个名字
    private Picture pictureBean;
    private int position;
    private PictureListFragment fragment;//左边的图片的列表
    private List<Picture> beanList;//图片列表
    private MyAdapter adapter;
    private Set<String> localFavouriteSet;//本地数据库里面的用户收藏数据
    private IUserInfo mUser;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //下载成功的消息
                case 0:
                    String filePath = (String) msg.obj;
                    //设置壁纸
                    Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(filePath);
                    try {
                        getActivity().setWallpaper(bitmap);
                        Toast.makeText(getActivity(), "设置壁纸成功", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "设置壁纸失败", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                //下载失败的消息
                case 1:
                    String reason = (String) msg.obj;
                    Toast.makeText(getActivity(), "设置壁纸失败:" + reason, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //浮动层菜单
    @ViewInject(R.id.ly_picture_detail_menu)
    View lyPictureDetailMenu;

    @ViewInject(R.id.sb_seekbar)
    SeekBar seekBar;

    //左边的Fragment
    @ViewInject(R.id.fl_left_container)
    View flContainer;

    //收藏图片
    @ViewInject(R.id.iv_favorite)
    ImageView ivFavorite;

    //详情中可以互动的viewpager
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    /**
     * 启动方法，要显示的图片
     *
     * @param context
     * @param postion  当前显示的图片在列表中的位置
     * @param beanList 图片列表
     */
    public static void startActivity(Context context, int postion, List<Picture> beanList) {
        Intent intent = new Intent(context, ShowPictureDetailActivity.class);
        intent.putExtra("POSITION", postion);
        intent.putExtra("BEAN_LIST", (Serializable) beanList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        this.position = getIntent().getIntExtra("POSITION", 0);
        this.beanList = (List<Picture>) getIntent().getSerializableExtra("BEAN_LIST");
        this.pictureBean = beanList.get(position);
        //初始化
        this.init();
        //添加监听器
        this.addListener();
    }

    //初始化
    private void init() {
        //添加图片列表的Fragment
        this.fragment = PictureListFragment.newInstance(beanList);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fl_left_container, fragment);
        transaction.commit();
        //创建adapter
        adapter = new MyAdapter(this, beanList);
        viewPager.setAdapter(adapter);
        //设置选中的页数
        viewPager.setCurrentItem(position);
        //获取本地收藏
        localFavouriteSet = MyApplication.getInstance().getLocalFavouriteSet();
        //如果第一个图片被收藏了
        if (localFavouriteSet != null && localFavouriteSet.contains(pictureBean.getDownloadUrl())) {
            //第一个图片是被收藏的图片
            ivFavorite.setImageResource(R.drawable.btn_remove_favorate_selector);
        }
        //没有被收藏
        else {
            //显示可以被收藏的icon
            ivFavorite.setImageResource(R.drawable.btn_add_favorate_selector);
        }
    }

    //添加监听器
    private void addListener() {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.ly_picture_detail_menu).setOnClickListener(this);
        findViewById(R.id.ly_similar_pic).setOnClickListener(this);
        findViewById(R.id.ly_title).setOnClickListener(this);
        findViewById(R.id.ly_favorite_pic).setOnClickListener(this);
        findViewById(R.id.ly_set_wallpaper).setOnClickListener(this);
        findViewById(R.id.ly_cut_pic).setOnClickListener(this);
        findViewById(R.id.ly_more_choose).setOnClickListener(this);
        //进度条
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //移动图片
                PictureDetailFragment fragment = adapter.getFragment(viewPager.getCurrentItem());
                if (fragment != null) {
                    fragment.movePicture(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //给viewpgaer添加滚动监听监听
        viewPager.addOnPageChangeListener(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //顶部向左的箭头
            case R.id.btn_left:
                this.finish();
                break;
            //点击浮动层
            case R.id.ly_picture_detail_menu:
                onDetailMenuClick();
                break;
            //点击相似图片
            case R.id.ly_similar_pic:
                //显示图片列表fragment
                showFragmentView();
                break;
            //收藏图片
            case R.id.ly_favorite_pic:
                onFavouriteClick();
                break;
            //设置壁纸
            case R.id.ly_set_wallpaper:
                setPaper();
                break;
            //剪切
            case R.id.ly_cut_pic:
                crop();
                break;
            //更多
            case R.id.ly_more_choose:
                //显示更多
                showMoreMenu();
                break;
        }
    }

    //浮动层点击事件
    private void onDetailMenuClick() {
        //如果Fragment显示了，则隐藏掉
        if (flContainer.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.coolwallpaper_slid_out_to_left);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    flContainer.clearAnimation();
                    flContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            flContainer.clearAnimation();
            flContainer.startAnimation(animation);
        }
        //Fragment没有显示，则隐藏掉浮动层
        else {
            lyPictureDetailMenu.setVisibility(View.GONE);
        }
    }

    //显示左边的fragment
    private void showFragmentView() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.coolwallpaper_slid_in_from_left);
        animation.setFillAfter(true);
        flContainer.clearAnimation();
        flContainer.startAnimation(animation);
        flContainer.setVisibility(View.VISIBLE);
    }

    //订阅图片刷新事件
    @Subscribe
    public void updatePicture(UpdatePictureEvent event) {
        this.beanList = event.getBeanList();
        this.position = event.getPosition();
        viewPager.setCurrentItem(position);
    }

    //订阅下载文件成功的事件
    @Subscribe
    public void downloadSuccessEvent(DownloadPictureSuccessEvent event) {
        Toast.makeText(this, "图片成功收藏到本地" + event.getSavePath(), Toast.LENGTH_SHORT).show();
    }

    //订阅下载文件失败的事件
    @Subscribe
    public void downloadFailuerEvent(DownloadPictureFailureEvent event) {
        Toast.makeText(this, "图片 " + event.getPictureBean().getDesc() + " 下载失败", Toast.LENGTH_SHORT).show();
        //设置按钮为未收藏的状态
        //this.ivFavorite.setImageDrawable(favoriteAddDrawable);
    }

    //点击收藏按钮
    private void onFavouriteClick() {
        //如果网络不可用则直接提示
        if (!NetworkUtil.isNetworkAvailable()) {
            ToastUtil.show("网络不可用，请检查网络");
            return;
        }
        //判断是否登录了
        mUser = UserUtil.getInstance().getUser();
        //没有登录
        if (mUser == null) {
            //跳转到登录中心
            LoginActivity.startActivityForResult(this, REQUEST_CODE_LOGIN);
        }
        //已经登录了
        else {
            Picture currentPicture = getCurrentPicture();
            //如果还没有收藏
            if (localFavouriteSet != null && !localFavouriteSet.contains(currentPicture.getDownloadUrl())) {
                //去收藏
                doMyFavourite(currentPicture);
            }
            //已经收藏了
            else {
                //取消收藏
                doCancelFavouroite(currentPicture);
            }
        }
    }

    //点击了下载按钮
    private void doDownload() {
        //下载图片
        FileUtil.getInstance().downloadPictureFile(pictureBean);
    }

    //适配器
    class MyAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        Context context;
        List<PictureDetailFragment> fragmentList = new ArrayList<>();
        List<Picture> pictureList;
        int currentPage;//当前显示的页面
        boolean isScrolled = false;//是否滚动过了
        boolean isPageChange = false;//是否换了一页

        public MyAdapter(Context context, List<Picture> pictureList) {
            super(getFragmentManager());
            this.context = context;
            this.pictureList = pictureList;
            //创建Fragment
            if (pictureList != null) {
                for (Picture p : pictureList) {
                    fragmentList.add(PictureDetailFragment.newInstance(p));
                }
            }
        }

        @Override
        public int getCount() {
            return pictureList == null ? 0 : pictureList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        //获取指定的fragment
        public PictureDetailFragment getFragment(int position) {
            PictureDetailFragment fragment = null;
            if (fragmentList != null && position < fragmentList.size()) {
                fragment = fragmentList.get(position);
            }
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        //获取Fragment列表
        public List<PictureDetailFragment> getFragmentList() {
            return fragmentList;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Logger.d("onPageSelected() position = " + position);
            currentPage = position;
            if (position > 0) {
                isScrolled = true;
            }
            // 第一页
            if (position == 0 && isScrolled) {
                Toast.makeText(getActivity(), "已经到第一页了", Toast.LENGTH_SHORT).show();
            }
            //最后一页
            if (position == beanList.size() - 1) {
                Toast.makeText(getActivity(), "已经到最后一页了", Toast.LENGTH_SHORT).show();
            }
            isPageChange = true;
            //翻页了之后，要刷新一下收藏按钮的图标
            refreshFavouriteIcon(getCurrentPicture().getDownloadUrl());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Logger.d("onPageScrollStateChanged() = " + state);
            //viewpager停止滑动
            if (state == ViewPager.SCROLL_STATE_IDLE && isPageChange) {
                //取消自动加载
                fragmentList.get(currentPage).setAutoLoad(false);
                //加载图片
                fragmentList.get(currentPage).startLoadingPicture();
                isPageChange = false;
            }
            //正在惯性移动中
            else if (state == ViewPager.SCROLL_STATE_SETTLING && isPageChange) {
                //取消上一个图片加载
                fragmentList.get(currentPage).cancelLoadingPicture();
                //取消自动加载
                fragmentList.get(currentPage).setAutoLoad(false);
            }
        }

    }

    //显示seekBar
    public void showSeekBar() {
        seekBar.setVisibility(View.VISIBLE);
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    //隐藏seekBar
    public void hideSeekBar() {
        seekBar.setVisibility(View.GONE);
    }

    //显示浮层
    public void showMaskMenu() {
        //如果浮层不可见
        if (lyPictureDetailMenu.getVisibility() != View.VISIBLE) {
            //当前图片是否在本地收藏中
            String url = getCurrentPicture().getDownloadUrl();
            //刷新一下收藏按钮的图标
            refreshFavouriteIcon(url);
            //弹出浮动层菜单
            lyPictureDetailMenu.setVisibility(View.VISIBLE);
            Toast.makeText(this, "点击了图片", Toast.LENGTH_SHORT).show();
        }
    }

    //设置壁纸
    private void setPaper() {
        //获取当前的Fragment
        PictureDetailFragment fragment = adapter.getFragmentList().get(viewPager.getCurrentItem());
        //获取当前显示的图片
        String url = fragment.getPictureUrl();
        //下载图片
        FileUtil.getInstance().downloadFile(url, FileUtil.DIRECTORY_DOWNLOAD, new FileUtil.DownloadCallback() {

            @Override
            public void onSuccess(String filePath) {
                //发送设置壁纸的消息
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = filePath;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(String reason) {
                //发送下载壁纸失败的消息
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = reason;
                handler.sendMessage(msg);
            }
        });
    }

    //剪切
    private void crop() {
        //获取当前的url
        String url = adapter.getFragment(viewPager.getCurrentItem()).getPictureUrl();
        Picture picture = beanList.get(viewPager.getCurrentItem());
        if (url == null || "".equals(url)) {
            Toast.makeText(this, "error:图片url为空", Toast.LENGTH_SHORT).show();
        } else {
            Uri sourceUri = Uri.parse(url);
            Uri destinationUri = Uri.fromFile(new File(FileUtil.getInstance().DIRECTORY_DOWNLOAD, SAMPLE_CROPPED_IMAGE_NAME));
            UCrop.of(sourceUri, destinationUri).withAspectRatio(16, 9).withMaxResultSize(picture.getWidth(), picture.getHeight()).start(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //剪切成功
            if (requestCode == UCrop.REQUEST_CROP) {
                //保存剪切的文件
                FileInputStream fin = null;
                FileOutputStream fou = null;
                FileChannel inChanel = null;
                FileChannel outChanel = null;
                try {
                    //保存文件
                    Uri uri = UCrop.getOutput(data);
                    fin = new FileInputStream(new File(uri.getPath()));
                    String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), uri.getLastPathSegment());
                    File saveFile = new File(FileUtil.getInstance().DIRECTORY_DOWNLOAD, filename);
                    fou = new FileOutputStream(saveFile);
                    inChanel = fin.getChannel();
                    outChanel = fou.getChannel();
                    inChanel.transferTo(0, inChanel.size(), outChanel);
                    //保存文件成功后，存储到本地壁纸数据库
                    FileUtil.getInstance().saveLocalPicture(saveFile.getAbsolutePath());
                    Toast.makeText(getActivity(), "壁纸剪切成功，请在'我的壁纸'中查看", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "error:壁纸剪切失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    try {
                        if (outChanel != null) {
                            outChanel.close();
                        }
                        if (inChanel != null) {
                            inChanel.close();
                        }
                        if (fou != null) {
                            fou.close();
                        }
                        if (fin != null) {
                            fin.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //登录界面返回
            else if (requestCode == REQUEST_CODE_LOGIN) {
                //取出参数
                IUserInfo user = (IUserInfo) data.getSerializableExtra("USER");
                //登录失败
                if (user == null) {
                    ToastUtil.show("登录失败");
                }
                //登录成功
                else {
                    ToastUtil.show("登录成功，继续收藏吧");
                }
            }
        }
        //剪切图片失败
        if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(getActivity(), "图片裁切失败", Toast.LENGTH_SHORT).show();
        }
    }

    //显示更多菜单
    private void showMoreMenu() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_pic_detail_popup_window, null);
        //设置PopupWindwow
        PopupWindow window = new PopupWindow(this);
        window.setHeight(ScreenUtils.dpToPxInt(this, 80));
        window.setWidth(ScreenUtils.dpToPxInt(this, 80));
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.getBackground().setAlpha(0);
        window.setContentView(view);
        //下载按钮添加监听器
        view.findViewById(R.id.tv_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载图片
                doDownload();
            }
        });
        //显示PopupWindow
        window.showAsDropDown(lyPictureDetailMenu.findViewById(R.id.ly_more_choose), 0, 0);
    }

    //收藏图片
    private void doMyFavourite(Picture picture) {
        MyBmobFavourite myBmobFavourite = ConvertUtil.toMyBmobFavourite(mUser, picture);
        myBmobFavourite.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //收藏成功
                favouriteSuccess(myBmobFavourite);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("收藏失败");
            }
        });
    }

    //收藏成功后，做一些界面有关的工作
    private void favouriteSuccess(MyBmobFavourite favourite) {
        //收藏图标改一下
        ivFavorite.setImageResource(R.drawable.btn_remove_favorate_selector);
        //查询本地数据库是否存在
        LocalFavouriteDao dao = DBUtil.getInstance().getLocalFavouriteDao();
        QueryBuilder<LocalFavourite> qb = dao.queryBuilder();
        qb.where(LocalFavouriteDao.Properties.Account.eq(mUser.getAccount()), LocalFavouriteDao.Properties.Url.eq(favourite.getUrl()));
        List tmpList = qb.list();
        //没有数据
        if (EmptyUtil.isEmpty(tmpList)) {
            //保存到本地数据库
            LocalFavourite localFavourite = ConvertUtil.toLocalFavourite(favourite);
            dao.insert(localFavourite);
            //刷新数据
            MyApplication.getInstance().addFavouroiteUrl(localFavourite.getUrl());
            this.localFavouriteSet = MyApplication.getInstance().getLocalFavouriteSet();
        }
    }

    //每次滑动图片后刷新一下收藏按钮的图标
    private void refreshFavouriteIcon(String url) {
        //用户已经收藏
        if (localFavouriteSet != null && localFavouriteSet.contains(url)) {
            ivFavorite.setImageResource(R.drawable.btn_remove_favorate_selector);
        }
        //用户没有收藏
        else {
            ivFavorite.setImageResource(R.drawable.btn_add_favorate_selector);
        }
    }

    //取消收藏
    private void doCancelFavouroite(Picture picture) {
        //获取MyBmobUser对象
        MyBmobUser myBmobUser = BmobUser.getCurrentUser(this, MyBmobUser.class);
        //查询收藏记录
        BmobQuery<MyBmobFavourite> query = BmobUtil.getMyFavouriteQuery();
        query.addWhereEqualTo("account", mUser.getAccount());
        query.addWhereEqualTo("url", picture.getDownloadUrl());
        query.findObjects(this, new FindListener<MyBmobFavourite>() {
            @Override
            public void onSuccess(List<MyBmobFavourite> list) {
                //服务器上没有查询到这条数据
                if (EmptyUtil.isEmpty(list)) {
                    ToastUtil.showDebug("服务器上不存在这个收藏记录，取消收藏成功");
                    cancelFavouriteSuccess(picture.getDownloadUrl());
                    return;
                }
                //获取objecId
                String objectId = list.get(0).getObjectId();
                //正式删除
                MyBmobFavourite favourite = new MyBmobFavourite();
                favourite.setObjectId(objectId);
                favourite.delete(MyApplication.getInstance(), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        //删除成功
                        cancelFavouriteSuccess(picture.getDownloadUrl());
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //取消收藏成功后，处理一下逻辑
    private void cancelFavouriteSuccess(String url) {
        //删除本地数据库中的记录
        LocalFavouriteDao dao = DBUtil.getInstance().getLocalFavouriteDao();
        QueryBuilder<LocalFavourite> qb = dao.queryBuilder();
        IUserInfo user = UserUtil.getInstance().getUser();
        qb.where(LocalFavouriteDao.Properties.Account.eq(user.getAccount()), LocalFavouriteDao.Properties.Url.eq(url));
        List<LocalFavourite> list = qb.list();
        if (!EmptyUtil.isEmpty(list)) {
            dao.deleteInTx(list);
        }
        //刷新数据
        MyApplication.getInstance().removeFavouriteUrl(url);
        this.localFavouriteSet = MyApplication.getInstance().getLocalFavouriteSet();
        //刷新收藏图标
        refreshFavouriteIcon(url);
    }

    private Picture getCurrentPicture() {
        return beanList.get(viewPager.getCurrentItem());
    }
}
