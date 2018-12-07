package com.xyyy.www.opengles01.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.xyyy.www.opengles01.LogUtil;
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
            1f, 1f,


            -0.5f,-0.5f,
            0.5f,-0.5f,
            -0.5f,0.5f,
            0.5f,0.5f
    };

    private float[] fragmentData = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f

            //离屏渲染 FBO坐标系不一致 所以需要用这个
//            0f, 0f,
//            1f, 0f,
//            0f, 1f,
//            1f, 1f

    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer fragmentBuffer;

    private int program;
    private int vPosition;
    private int fPosition;
    private int textureid;
    private int sampler;

    private int vboId;
    private int fboId;

    private int imageTextureId;
    private int imageTextureId2;

    private FboRender fboRender;

    private int umatrix;
    private float[] matrix = new float[16];

    private int width;
    private int height;

    private int screenW = 1080;
    private int screenH = 1920;

    private float imgWidth = 526f;
    private float imgHeight = 702f;

    private OnRenderCreateListener onRenderCreateListener;

    public XYTextureRender(Context context) {
        this.context = context;
        fboRender = new FboRender(context);
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

        fboRender.onCreate();

        String vertexSource = XYShaderUtil.getRawResource(context, R.raw.img_vertex_shader_m);
        String fragmentSource = XYShaderUtil.getRawResource(context, R.raw.img_fragment_shader);

        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        vPosition = GLES20.glGetAttribLocation(program, "v_Position");
        fPosition = GLES20.glGetAttribLocation(program, "f_Position");
        sampler = GLES20.glGetUniformLocation(program, "sTexture");
        umatrix = GLES20.glGetUniformLocation(program, "u_Matrix");

        int[] vbos = new int[1];
//        1、创建VBO
        GLES20.glGenBuffers(1, vbos, 0);
        vboId = vbos[0];
        //2.绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, null, GLES20.GL_STATIC_DRAW);

        //3. 分配VBO需要的缓冲大小
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4 + fragmentData.length * 4, null, GLES20.GL_STATIC_DRAW);

        //4,为VBO设置顶点数据的值(这里想象内存区域 先偏移0用于存储顶点坐标,再偏移顶点坐标的大小,用于存储片元坐标) 大小是顶点坐标加上片元坐标大小
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexData.length * 4, vertexBuffer);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, fragmentData.length * 4, fragmentBuffer);
//        5、解绑VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        //FBO
        int[] fbos = new int[1];
        GLES20.glGenBuffers(1, fbos, 0);
        fboId = fbos[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);


        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        textureid = textureIds[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(sampler, 0);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureid, 0);//把纹理绑定到FBO上
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, screenW, screenH, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            LogUtil.e("fbo wrong");
        } else {
            LogUtil.e("fbo success");
        }


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        imageTextureId = loadTexture(R.drawable.androids);
        imageTextureId2 = loadTexture(R.drawable.girl);
//        imageTextureId = loadTexture(R.mipmap.test);

        if (onRenderCreateListener != null) {
            onRenderCreateListener.onCreate(textureid);
        }

    }

    public void setOnRenderCreateListener(OnRenderCreateListener onRenderCreateListener) {
        this.onRenderCreateListener = onRenderCreateListener;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
//        fboRender.onChange(width, height);
        //这里上面的width . height 是surface 的宽度 和高度

        //记录外层的宽高
        this.width = width;
        this.height = height;

        //这里是屏幕的宽高
        width = screenW;
        height = screenH;

        //横屏 宽大越高
        if (width > height) {
            //left 实际的高 除以 图片的高 得到比例 拉伸了多少作用于宽上面 再乘以图片的高度 求出实际下面绘制的宽度 最后 用宽除实际的宽度求出比例    因为是-1到1之间  left 是负的 right是正的
            Matrix.orthoM(matrix, 0, -width / ((height / imgHeight) * imgWidth), width / ((height / imgHeight) * imgWidth), -1f, 1f, -1f, 1f);

        } else {//竖屏 宽小于高  把宽作为1
            Matrix.orthoM(matrix, 0, -1, 1, -height / ((width / imgWidth) * imgHeight), height / ((width / imgWidth) * imgHeight), -1f, 1f);
        }

        //旋转
        Matrix.rotateM(matrix, 0, 180, 1, 0, 0);

        //z 轴旋转180度
//        Matrix.rotateM(matrix, 0, 180, 0, 0, 1);

    }

    @Override
    public void onDrawFrame() {
//        按照实际的宽高
        GLES20.glViewport(0, 0, screenW, screenH);//这里实际上就把图片绘制到离屏纹理上
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);//0的话就是不使用离屏渲染
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);//这里绑定之后 后面的操作不会显示到窗口上面


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f, 0f, 0f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glUniformMatrix4fv(umatrix, 1, false, matrix, 0);//使用正交投影 矩阵matrix

        //绑定vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);

        //绘制第一张图片
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId);//使用imageTexture

        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                0);

        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);

        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //绘制第二张图片
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId2);//使用imageTexture

        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                32);
        //offset需要递增  一个float是4个字节,8个坐标 所以要移动4*8个字节,下一个坐标才是第二张

        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);

        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        //解绑
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //切换到屏幕后 重新设置渲染的宽高
        GLES20.glViewport(0, 0, width, height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);//解绑
        fboRender.onDraw(textureid);
    }

    //生成一个纹理
    private int loadTexture(int src) {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);


        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), src);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureIds[0];
    }

    public interface OnRenderCreateListener {
        //textid  离屏渲染的id
        void onCreate(int textid);
    }
}
