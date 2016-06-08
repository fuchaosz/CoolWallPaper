package com.coolwallpaper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
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
import com.coolwallpaper.bean.IUserOperator;
import com.coolwallpaper.bmob.BmobUtil;
import com.coolwallpaper.bmob.MyBmobFavourite;
import com.coolwallpaper.bmob.MyBmobPicture;
import com.coolwallpaper.bmob.MyBmobUser;
import com.coolwallpaper.event.DownloadPictureFailureEvent;
import com.coolwallpaper.event.DownloadPictureSuccessEvent;
import com.coolwallpaper.event.UpdatePictureEvent;
import com.coolwallpaper.fragment.PictureDetailFragment;
import com.coolwallpaper.fragment.PictureListFragment;
import com.coolwallpaper.model.Picture;
import com.coolwallpaper.utils.ConvertUtil;
import com.coolwallpaper.utils.FileUtil;
import com.coolwallpaper.utils.LogUtil;
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

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 显示图片详情
 * Created by fuchao on 2015/10/21.
 */
public class ShowPictureDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_LOGIN = 0x100;//登录界面请求码
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";//调用uCrop之前必须给图片一个名字
    private Picture pictureBean;
    private int position;
    private Matrix matrix;
    private float maxMoveLength;//最大可以移动的距离
    private int currentProgress = 50;//当前进度
    private PictureListFragment fragment;//左边的图片的列表
    private List<Picture> beanList;//图片列表
    private Drawable favoriteAddDrawable;//没有收藏的时候显示的drawable
    private Drawable favoriteRemoveDrawable;//收藏了之后显示的drawable
    private MyAdapter adapter;
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
        //创建收藏图片的Drawable
        this.favoriteAddDrawable = getResources().getDrawable(R.drawable.btn_add_favorate_selector);
        this.favoriteRemoveDrawable = getResources().getDrawable(R.drawable.btn_remove_favorate_selector);
        //默认为未收藏的按钮
        this.ivFavorite.setImageDrawable(favoriteAddDrawable);
        //创建adapter
        adapter = new MyAdapter(this, beanList);
        viewPager.setAdapter(adapter);
        //设置选中的页数
        viewPager.setCurrentItem(position);
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

    //查询下载量和收藏量
    private void queryMyBmobPicture() {

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
                doFavorite();
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
        Toast.makeText(this, "图片 " + event.getPictureBean().getDesc() + " 收藏失败", Toast.LENGTH_SHORT).show();
        //设置按钮为未收藏的状态
        this.ivFavorite.setImageDrawable(favoriteAddDrawable);
    }

    //点击了收藏按钮
    private void doFavorite() {
        //如果是没有收藏的状态
        if (ivFavorite.getDrawable() == favoriteAddDrawable) {
            //设置按钮为已经收藏的状态
            this.ivFavorite.setImageDrawable(favoriteRemoveDrawable);
            //下载图片
            FileUtil.getInstance().downloadPictureFile(pictureBean);
        }
        //已经收藏的状态
        else {
            //设置按钮为未收藏的状态
            this.ivFavorite.setImageDrawable(favoriteAddDrawable);
            //删除收藏的图片
            FileUtil.getInstance().deleteDownloadPictureFile(FileUtil.getFileName(pictureBean.getDownloadUrl()));
            Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show();
        }
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

        //获取当前显示的图片
        private Picture getCurrentPicture() {
            return beanList.get(currentPage);
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
                //收藏图片
                doMyFavourite(adapter.getCurrentPicture());
            }
        });
        //显示PopupWindow
        window.showAsDropDown(lyPictureDetailMenu.findViewById(R.id.ly_more_choose), 0, 0);
    }

    //收藏图片
    private void doMyFavourite(Picture picture) {
        //首先判断有没有登录成功
        IUserOperator operator = UserUtil.getInstance();
        IUserInfo user = operator.getUser();
        //本地没有保存的用户
        if (user == null) {
            //跳转到登录中心
            LoginActivity.startActivityForResult(this, REQUEST_CODE_LOGIN);
        }
        //本地有用户登录
        else {
            //获取到BmobUser
            BmobQuery<MyBmobUser> query = BmobUtil.getMyUserQuery();
            query.addWhereEqualTo("account", user.getAccount());
            query.findObjects(this, new FindListener<MyBmobUser>() {
                @Override
                public void onSuccess(List<MyBmobUser> list) {
                    //表示没有这个用户
                    if (list == null || list.size() == 0) {
                        ToastUtil.showDebug("error:本地这个用户在Bmob上查不到");
                        return;
                    }
                    //取出这个用户
                    MyBmobUser bmobUser = list.get(0);
                    //保存图片到bmob的图片表中，如果图片没有保存
                    saveBmobPicture(bmobUser, picture);
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    //保存当前要收藏的图片到Bmob上，如果不存在的话
    private void saveBmobPicture(MyBmobUser bmobUser, Picture picture) {
        //查找Bmob上是否存在这个图片
        BmobQuery<MyBmobPicture> query = BmobUtil.getMyPictureQuery();
        //query.addWhereEqualTo("url", picture.getDownloadUrl());
        query.addWhereEqualTo("width", 100);
        query.findObjects(this, new FindListener<MyBmobPicture>() {
            @Override
            public void onSuccess(List<MyBmobPicture> list) {
                //没有这个图片，则要先把这个图片保存到bmob上
                if (list == null || list.size() == 0) {
                    //转换为bmob对象
                    MyBmobPicture myBmobPicture = ConvertUtil.toMyBmobPicture(picture);
                    //保存到bmob上
                    myBmobPicture.save(ShowPictureDetailActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtil.showDebug("保存图片到bmob成功,desc=" + picture.getDesc());
                            //保存收藏信息
                            saveMyFavoutiteInfo(bmobUser, myBmobPicture);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            LogUtil.d("保存图片失败,s = " + s);
                        }
                    });

                }
                //这个图片已经存在了
                else {
                    //保存收藏信息
                    saveMyFavoutiteInfo(bmobUser, list.get(0));
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d("查找图片失败,s = " + s);
            }
        });
    }

    //保存收藏信息，到这一步，用户和图片都已经保存到bmob上
    private void saveMyFavoutiteInfo(MyBmobUser user, MyBmobPicture picture) {
        //开始保存收藏信息
        MyBmobFavourite favourite = new MyBmobFavourite();
        favourite.setPicture(picture);
        favourite.setUser(user);
        favourite.save(ShowPictureDetailActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("收藏成功");
                //收藏成功
                favouriteSuccess();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    //收藏成功后，做一些界面有关的工作
    private void favouriteSuccess() {
        //收藏图标改一下
        ivFavorite.setImageDrawable(favoriteRemoveDrawable);
    }

}
