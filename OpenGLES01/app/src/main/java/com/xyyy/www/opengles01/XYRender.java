package com.xyyy.www.opengles01;

import android.opengl.GLES20;
import android.util.Log;


/**
 * @author liuml
 * @explain
 * @time 2018/12/3 15:08
 */
public class XYRender implements XYEGLSurfaceView.XYGLRender{

    String TAG = "XYrender";
    public XYRender() {

    }

    @Override
    public void onSurfaceCreated() {
        Log.d(TAG, "onSurfaceCreated: ");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame() {
        //Log.d(TAG, "onDrawFrame: \"ondrawFrame\"");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    }
}
