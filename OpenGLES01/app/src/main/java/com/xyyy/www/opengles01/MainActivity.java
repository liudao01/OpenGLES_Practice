package com.xyyy.www.opengles01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xyyy.www.opengles01.anim.OpenGLAnimActivity;
import com.xyyy.www.opengles01.image.TextureActivity;
import com.xyyy.www.opengles01.image_abstruct.ImageAbstructActivity;
import com.xyyy.www.opengles01.surface.SurfaceTextureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btSample;
    private Button btXyglsurfaceview;
    private Button bt3;
    private Button btAnim;
    private Button btImageAbstruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btSample = findViewById(R.id.bt_sample);
        btXyglsurfaceview = findViewById(R.id.bt_xyglsurfaceview);
        bt3 = findViewById(R.id.bt_3);
        btAnim = findViewById(R.id.bt_anim);

        btSample.setOnClickListener(this);
        btXyglsurfaceview.setOnClickListener(this);
        bt3.setOnClickListener(this);
        btAnim.setOnClickListener(this);
        btImageAbstruct = findViewById(R.id.bt_image_abstruct);
        btImageAbstruct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_sample:
                intent = new Intent(this, SampleOpenGLActivity.class);
                break;
            case R.id.bt_xyglsurfaceview:
                intent = new Intent(this, TextureActivity.class);

                break;
            case R.id.bt_3:
                intent = new Intent(this, SurfaceTextureActivity.class);

                break;
            case R.id.bt_anim:
                intent = new Intent(this, OpenGLAnimActivity.class);
                break;
            case R.id.bt_image_abstruct:
                intent = new Intent(this, ImageAbstructActivity.class);
                break;

        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
