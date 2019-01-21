package com.xyyy.www.opengles01.image_abstruct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xyyy.www.opengles01.R;

/**
 * 显示GLsurface  抽取基类 使用
 */
public class ImageAbstructActivity extends AppCompatActivity {

    private Button btChangeFilter;
    private ImageGLView imageGLView;

    String resourceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_abstruct);
        initView();
    }

    private void initView() {
        btChangeFilter = findViewById(R.id.bt_change_filter);
        imageGLView = findViewById(R.id.imageGLView);
        btChangeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGLView.setFilter(resourceData);
            }
        });
    }
}
