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
}
