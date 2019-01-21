package com.xyyy.www.opengles01.anim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xyyy.www.opengles01.LogUtil;
import com.xyyy.www.opengles01.R;
import com.xyyy.www.opengles01.util.AppUtils;

/**
 * @author liuml
 * @explain 使用XYTextureView
 * @time 2018/12/3 17:21
 */
public class OpenGLAnimActivity extends AppCompatActivity {
    private XYTextureView xyglsurfaceview;
    private Button btStartimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_texture);


        initView();
    }

    private void startImg() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 8; i++) {
                    LogUtil.d("遍历 = " + i);

                    //拿到资源
                    int imgsrc = getResources().getIdentifier("img_" + i, "drawable", AppUtils.getPackageName(getApplicationContext()));
                    xyglsurfaceview.setCurrentImg(imgsrc);
                    if (i == 7) {
                        i = 0;
                    }
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();

    }

    private void initView() {
        boolean isStart;
        xyglsurfaceview = findViewById(R.id.xyglsurfaceview);
        btStartimg = findViewById(R.id.bt_startimg);
        btStartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImg();
            }
        });
    }
}
