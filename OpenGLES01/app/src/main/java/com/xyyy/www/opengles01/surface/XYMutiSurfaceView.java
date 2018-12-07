package com.xyyy.www.opengles01.surface;

import android.content.Context;
import android.util.AttributeSet;

import com.xyyy.www.opengles01.LogUtil;
import com.xyyy.www.opengles01.XYEGLSurfaceView;

/**
 * @author liuml
 * @explain 这个是多的
 * @time 2018/12/6 14:20
 */
public class XYMutiSurfaceView extends XYEGLSurfaceView {

    private XYMutiRender xyMutiRender;

    public XYMutiSurfaceView(Context context) {
        this(context, null);
    }

    public XYMutiSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XYMutiSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xyMutiRender = new XYMutiRender(context);
        setRender(xyMutiRender);
    }

    public void setTextureId(int textureId, int index) {
        if (xyMutiRender != null) {
            LogUtil.d("index = "+index);
            xyMutiRender.setTextureId(textureId,index);
        }
    }


}
