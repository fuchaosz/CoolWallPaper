package com.coolwallpaper;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
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
        //添加监听器
        this.addListener();
        //调整图片
        this.adjustPicture();
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
        //获取屏幕大小
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取图片的实际大小
        int drawWidth = image.getDrawable().getIntrinsicWidth();
        int drawHeight = image.getDrawable().getIntrinsicHeight();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        matrix.postScale(screenHeight * 1.0f / drawHeight, screenHeight * 1.0f / drawHeight);
        image.setImageMatrix(matrix);
        this.maxMoveLength = (this.image.getDrawable().getBounds().width() - screenWidth) / 2.0f;
        this.centerX = screenWidth / 2;
        this.centerY = screenHeight / 2;
        //图片右移
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLength += 20;
                matrix.preTranslate(moveLength, 0);
                image.setImageMatrix(matrix);
            }
        });

        //        attacher = new PhotoViewAttacher(image);
        //        float scale = screenHeight * 1.0f / drawHeight;
        //        attacher.setScaleLevels(scale, scale * attacher.getMediumScale(), scale * attacher.getMaximumScale());
        //        attacher.setScale(scale);
        //        attacher.update();
    }

}
