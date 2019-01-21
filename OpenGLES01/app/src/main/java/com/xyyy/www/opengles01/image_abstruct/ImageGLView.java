package com.xyyy.www.opengles01.image_abstruct;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2019/1/20 18:w15
 */
public class ImageGLView extends GLSurfaceView {

    // 记录当前滤镜数据
    private String mResourceData;
    ImageFilterRender render;

    public ImageGLView(Context context) {
        this(context, null);
    }

    public ImageGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        render = new ImageFilterRender(getContext());
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    public void setFilter(String resourceData) {
        mResourceData = resourceData;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                //创建滤镜
                createColorFilter(mResourceData);
                onFilterSizeChanged();
                requestRender();
            }
        });
    }

    /**
     * 滤镜大小发生变化
     */
    private void onFilterSizeChanged() {

    }

    /**
     * 创建滤镜
     *
     * @param mResourceData
     */
    private void createColorFilter(String mResourceData) {

    }
}
