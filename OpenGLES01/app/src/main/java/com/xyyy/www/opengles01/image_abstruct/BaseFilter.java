package com.xyyy.www.opengles01.image_abstruct;

import android.content.Context;
import android.opengl.GLES20;

import com.xyyy.www.opengles01.LogUtil;
import com.xyyy.www.opengles01.R;
import com.xyyy.www.opengles01.image.XYShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author liuml
 * @explain
 * @time 2019/1/20 18:15
 */
public class BaseFilter {

    private Context context;
    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,

    };

    private float[] fragmentData = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };
    private FloatBuffer fragmentBuffer;
    private FloatBuffer vertexBuffer;

    private int vboId;

    private int mProgram;

    private int vPosition;//顶点坐标句柄
    private int fPosition;//纹理坐标句柄


    private int textureType = 0;//模式使用Texture2D0
    private int textureId = 0;//纹理的id
    /**
     * 默认纹理贴图句柄
     */
    protected int vTexture;
    private String vertexSource;
    private String fragmentSource;


    public BaseFilter(Context context) {
        this.context = context;
        initBuffer();
    }

    /**
     * 实现此方法，完成程序的创建，可直接调用createProgram来实现
     */
//    protected abstract void onCreate();
//
//    protected abstract void onSizeChanged(int width, int height);

    /**
     * //0.初始化顶点坐标 片元坐标
     * Buffer初始化
     */
    protected void initBuffer() {
        ByteBuffer a = ByteBuffer.allocateDirect(vertexData.length * 4);
        a.order(ByteOrder.nativeOrder());
        vertexBuffer = a.asFloatBuffer();
        vertexBuffer.put(vertexData);
        vertexBuffer.position(0);

        ByteBuffer b = ByteBuffer.allocateDirect(vertexData.length * 4);
        b.order(ByteOrder.nativeOrder());
        fragmentBuffer = b.asFloatBuffer();
        fragmentBuffer.put(fragmentData);
        fragmentBuffer.position(0);


    }


    public void onCreate() {
        //加载shader
        vertexSource = XYShaderUtil.getRawResource(context, R.raw.base_img_vertex_shader);
        fragmentSource = XYShaderUtil.getRawResource(context, R.raw.base_img_fragment_shader);

//        textureId = loadTexture(R.drawable.androids);
    }

    public void setSize(int width, int height) {
        //绘制区域
        GLES20.glViewport(0, 0, width, height);
    }


    //1.清除画布

    /**
     * 1.清除画布
     */
    protected void onClear() {

        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    //2.创建纹理


    //3.绘制  3.1 清屏  3.2 使用GL程序 3.3 绑定纹理(需要有textureId )  3.4 启用顶点坐标和纹理坐标进行绘制
    protected void draw() {

        onClear();

        createProgram(vertexSource, fragmentSource);

        onUseProgram();

        onBindTexture();

        onDraw();
    }

    private void onDraw() {
        //顶点坐标
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //纹理坐标
        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 0, fragmentBuffer);
        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //glEnableVertexAttribArray启用index指定的通用顶点属性数组。
        //glDisableVertexAttribArray禁用index指定的通用顶点属性数组
        GLES20.glDisableVertexAttribArray(vPosition);
        GLES20.glDisableVertexAttribArray(fPosition);
    }


    /**
     * 创建GL程序 获取
     *
     * @param vertex
     * @param fragment
     */
    protected final void createProgram(String vertex, String fragment) {
        mProgram = uCreateGlProgram(vertex, fragment);
        vPosition = GLES20.glGetAttribLocation(mProgram, "v_Position");
        fPosition = GLES20.glGetAttribLocation(mProgram, "f_Position");
//        mHMatrix= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        vTexture = GLES20.glGetUniformLocation(mProgram, "sTexture");
    }

    /**
     * 创建GL程序
     *
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    public static int uCreateGlProgram(String vertexSource, String fragmentSource) {

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }
        //创建一个渲染程序
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            //将着色器程序添加到渲染程序中
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            //链接源程序
            GLES20.glLinkProgram(program);
            int[] lineSatus = new int[1];
            //检查链接源程序是否成功
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, lineSatus, 0);
            if (lineSatus[0] != GLES20.GL_TRUE) {
                LogUtil.e("link program error");
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    /**
     * 加载shader
     *
     * @param shaderType
     * @param source
     * @return
     */
    public static int loadShader(int shaderType, String source) {

        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compile = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compile, 0);
            if (compile[0] != GLES20.GL_TRUE) {
                LogUtil.e("shader compile error");
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    protected void onUseProgram() {
        //让program可用
        GLES20.glUseProgram(mProgram);
    }

    /**
     * 绑定纹理
     */
    protected void onBindTexture() {
        //纹理单元的主要目的是让我们在着色器中可以使用多于一个的纹理。
        // 通过把纹理单元赋值给采样器，我们可以一次绑定多个纹理，只要我们首先激活对应的纹理单元。
        // 在绑定纹理之前先激活纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
        //绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTextureId());
        //
        GLES20.glUniform1i(vTexture, textureType);
    }


    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }

}
