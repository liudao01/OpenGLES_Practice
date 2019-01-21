package com.xyyy.www.opengles01.image_abstruct;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2019/1/20 18:15
 */
public class ImageGLView extends GLSurfaceView {
    public ImageGLView(Context context) {
        this(context, null);
    }

    public ImageGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
