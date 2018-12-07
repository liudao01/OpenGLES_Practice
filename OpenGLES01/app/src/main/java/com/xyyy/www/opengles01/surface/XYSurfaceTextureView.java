package com.xyyy.www.opengles01.surface;

import android.content.Context;
import android.util.AttributeSet;

import com.xyyy.www.opengles01.XYEGLSurfaceView;
import com.xyyy.www.opengles01.image.XYTextureRender;

/**
 * @author liuml
 * @explain
 * @time 2018/12/3 17:19
 */
public class XYSurfaceTextureView extends XYEGLSurfaceView {

    private XYTextureRender xyTextureRender;

    public XYSurfaceTextureView(Context context) {
        this(context,null);
    }

    public XYSurfaceTextureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XYSurfaceTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xyTextureRender = new XYTextureRender(context);
        setRender(xyTextureRender);
    }

    public XYTextureRender getXyTextureRender() {
        return xyTextureRender;
    }
}
