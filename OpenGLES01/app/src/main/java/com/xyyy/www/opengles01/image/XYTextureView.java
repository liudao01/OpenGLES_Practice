package com.xyyy.www.opengles01.image;

import android.content.Context;
import android.util.AttributeSet;

import com.xyyy.www.opengles01.XYEGLSurfaceView;

/**
 * @author liuml
 * @explain
 * @time 2018/12/3 17:19
 */
public class XYTextureView extends XYEGLSurfaceView {
    public XYTextureView(Context context) {
        this(context,null);
    }

    public XYTextureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XYTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRender(new XYTextureRender(context));
    }


}
