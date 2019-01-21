package com.xyyy.www.opengles01.image_abstruct;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author liuml
 * @explain
 * @time 2019/1/21 10:14
 */
public class ImageFilterRender implements GLSurfaceView.Renderer {
    BaseFilter baseFilter;
    private Context context;

    public ImageFilterRender(Context context) {
        baseFilter = new BaseFilter(context);
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        baseFilter.onCreate();
        //设置图片的纹理id
//        loadTexture(R.drawable.img_1);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        baseFilter.setSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        baseFilter.draw();
    }

    private int loadTexture(int src) {
        //创建纹理
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);

        int textureId = textureIds[0];

        //绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);

        //设置参数 环绕（超出纹理坐标范围）：
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        //过滤（纹理像素映射到坐标点
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //图片内容
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), src);
        if (bitmap == null) {
            return 0;
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return textureId;
    }
}
