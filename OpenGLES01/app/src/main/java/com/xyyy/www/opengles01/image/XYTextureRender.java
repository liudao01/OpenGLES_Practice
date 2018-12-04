package com.xyyy.www.opengles01.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.xyyy.www.opengles01.R;
import com.xyyy.www.opengles01.XYEGLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author liuml
 * @explain
 * @time 2018/12/3 17:15
 */
public class XYTextureRender implements XYEGLSurfaceView.XYGLRender {
    private Context context;


    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };
    private FloatBuffer vertexBuffer;

    private float[] fragmentData = {
//            0f, 1f,
//            1f, 1f,
//            0f, 0f,
//            1f, 0f

            0f, 0.5f,
            0.5f, 0.5f,
            0f, 0f,
            0.5f, 0f
    };
    private FloatBuffer fragmentBuffer;

    private int program;
    private int vPosition;
    private int fPosition;
    private int textureid;
    private int sampler;


    public XYTextureRender(Context context) {
        this.context = context;
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        fragmentBuffer = ByteBuffer.allocateDirect(fragmentData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(fragmentData);
        fragmentBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated() {

        String vertexSource = XYShaderUtil.getRawResource(context, R.raw.img_vertex_shader);
        String fragmentSource = XYShaderUtil.getRawResource(context, R.raw.img_fragment_shader);

        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        vPosition = GLES20.glGetAttribLocation(program, "v_Position");
        fPosition = GLES20.glGetAttribLocation(program, "f_Position");
        sampler = GLES20.glGetUniformLocation(program, "sTexture");


        int []textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        textureid = textureIds[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(sampler, 0);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
        bitmap = null;
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame() {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f,0f, 0f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid);
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexBuffer);

        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                fragmentBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

    }
}
