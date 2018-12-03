package com.xyyy.www.opengles01;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2018/12/3 15:06
 */
public class XYGLSurfaceView extends XYEGLSurfaceView {
    public XYGLSurfaceView(Context context) {
        this(context,null);
    }

    public XYGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XYGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRender(new XYRender());
    }
}
