package com.xyyy.www.opengles01.surface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.xyyy.www.opengles01.R;
import com.xyyy.www.opengles01.image.XYTextureRender;

/**
 * @author liuml
 * @explain 多Surface渲染同一纹理
 * @time 2018/12/3 17:21
 */
public class SurfaceTextureActivity extends AppCompatActivity {
    private XYSurfaceTextureView xyglsurfaceview;
    private LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_texture);
        initView();
    }

    private void initView() {
        xyglsurfaceview = findViewById(R.id.xyglsurfaceview);
        llContent = findViewById(R.id.ll_content);

        xyglsurfaceview.getXyTextureRender().setOnRenderCreateListener(new XYTextureRender.OnRenderCreateListener() {
            @Override
            public void onCreate(final int textid) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (llContent.getChildCount() > 0) {
                            llContent.removeAllViews();
                        }

                        for (int i = 0; i < 3; i++) {

                            XYMutiSurfaceView xyMutiSurfaceView = new XYMutiSurfaceView(SurfaceTextureActivity.this);
                            xyMutiSurfaceView.setTextureId(textid);
                            xyMutiSurfaceView.setSurfaceAndEglContext(null, xyglsurfaceview.getEglContext());

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lp.width = 200;
                            lp.height = 300;
                            xyMutiSurfaceView.setLayoutParams(lp);

                            llContent.addView(xyMutiSurfaceView);
                        }

                    }
                });


            }
        });
    }
}
