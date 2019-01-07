package com.xyyy.www.opengles01.anim;

import android.content.Context;
import android.util.AttributeSet;

import com.xyyy.www.opengles01.LogUtil;
import com.xyyy.www.opengles01.XYEGLSurfaceView;

/**
 * @author liuml
 * @explan
 * @time 2018/12/3 17:19
 */
public class XYTextureView extends XYEGLSurfaceView {
    XYTextureRender xyImgVideoRender ;

    private int fbotextureid;

    public XYTextureView(Context context) {
        this(context,null);
    }

    public XYTextureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XYTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xyImgVideoRender = new XYTextureRender(context);
        setRender(xyImgVideoRender);

        xyImgVideoRender.setOnRenderCreateListener(new XYTextureRender.OnRenderCreateListener() {
            @Override
            public void onCreate(int textureId) {

                fbotextureid = textureId;
            }

        });
    }
    public void setCurrentImg(int imgsrc) {
        if (xyImgVideoRender != null) {
            xyImgVideoRender.setCurrentImgSrc(imgsrc);
//            requestRender();//手动刷新 调用一次
            LogUtil.d("手动刷新 "+imgsrc);
        }
    }

    public int getFbotextureid() {
        return fbotextureid;
    }
}
