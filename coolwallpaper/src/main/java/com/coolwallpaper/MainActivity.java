package com.coolwallpaper;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private PhotoViewAttacher attacher;
    private float maxMoveLength;
    private float centerX;
    private float centerY;
    private float moveLength;
    private Matrix matrix;
    private float count = 0;
    
    @ViewInject(R.id.tv_hollo)
    TextView helloworld;

    @ViewInject(R.id.image)
    ImageView image;

    @ViewInject(R.id.btn_left)
    Button btnLeft;

    @ViewInject(R.id.btn_right)
    Button btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        //直接跳转到图片列表
        ShowPictureListActivity.startActivity(this);
        finish();
        //添加监听器
        this.addListener();
        //调整图片
        this.image.post(new Runnable() {
            @Override
            public void run() {
                //adjustPicture();
            }
        });
    }

    //添加监听器
    private void addListener() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_show_pic)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_pic:
                //跳转到显示图片的地方
                // ShowPictureActivity.startActivity(this);
                ShowPictureListActivity.startActivity(this);
                finish();
                break;
        }

    }

    //调整图片
    private void adjustPicture() {
        this.matrix = image.getImageMatrix();
        //获取ImageView的大小
        int viewHeight = image.getHeight() - image.getPaddingBottom() - image.getPaddingTop();
        int viewWidth = image.getWidth() - image.getPaddingLeft() - image.getPaddingRight();
        //获取图片的实际大小
        int drawWidth = image.getDrawable().getIntrinsicWidth();
        int drawHeight = image.getDrawable().getIntrinsicHeight();
        //图片缩放，保证高度铺满整个屏幕
        float scale = viewHeight * 1.0f / drawHeight;
        matrix.postScale(scale, scale);
        image.setImageMatrix(matrix);
        //计算出放大之后的图片的宽度,高度就是屏幕的高度
        float widthAfterScale = drawWidth * scale;
        //最大移动距离
        this.maxMoveLength = (widthAfterScale - viewWidth) / 2;
        //将图片移动到中间去
        matrix.postTranslate(-maxMoveLength, 0);
        //matrix.postTranslate(-maxMoveLength, 0);
        image.setImageMatrix(matrix);
        //屏幕向右移动
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLength -= 100;
                if (moveLength >= -maxMoveLength) {
                    matrix.postTranslate(-100, 0);
                    image.setImageMatrix(matrix);
                    count += 100;
                } else {
                    //超过屏幕范围之后要控制刚好显示到屏幕边上
                    float tmp = maxMoveLength + (moveLength + 100);
                    matrix.postTranslate(-tmp, 0);
                    count += tmp;
                    image.setImageMatrix(matrix);
                    moveLength = -maxMoveLength;
                }
            }
        });
        //向右移动图片
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLength += 100;
                if (moveLength <= maxMoveLength) {
                    matrix.postTranslate(100, 0);
                    image.setImageMatrix(matrix);
                } else {
                    //超过屏幕范围之后要控制刚好显示到屏幕边上
                    float tmp = maxMoveLength - (moveLength - 100);
                    matrix.postTranslate(tmp, 0);
                    image.setImageMatrix(matrix);
                    moveLength = maxMoveLength;
                }
            }
        });
    }

}
