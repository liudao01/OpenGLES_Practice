# OpenGLES_Practice
OpenGLES Pratice

[TOC]

代码地址 
https://github.com/liudao01/OpenGLES_Practice


# 1. 多surfaceview 渲染同一纹理

![image](http://ws1.sinaimg.cn/large/958c5b69ly1fy1ox5jf03j20km13yndw.jpg)

给加个滤镜

1、首先利用离屏渲染把图像渲染到纹理texture中

2、通过共享EGLContext和texture，实现纹理共享

3、然后在新的Render里面可以对texture进行新的滤镜操作


代码:

在原来代码的基础上新写一个surufaceview  

```

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

//这里就是  通过共享EGLContext和texture，实现纹理共享
    public void setTextureId(int textureId, int index) {
        if (xyMutiRender != null) {
            xyMutiRender.setTextureId(textureId,index);
        }
    }


}
```

下面是新鞋的Render

```

/**
 * 多surfaceview 渲染单一纹理 的render
 */
public class XYMutiRender implements XYEGLSurfaceView.XYGLRender {
    private Context context;
    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,
            //三角形
            -0.25f, -0.25f,
            0.25f, -0.25f,
            0f, 0.15f

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

    private int program;
    private int vPosition;
    private int fPosition;
    private int sampler;
    private int textureId;
    private int imgTetureId;
    private int index;
    private String fragmentSource;

    public void setTextureId(int textureId, int index) {
        this.textureId = textureId;
        this.index = index;
    }

    public XYMutiRender(Context context) {
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

        imgTetureId = loadTexture(R.drawable.ghnl);
//        LogUtil.d("打印 index = " + index);
        fragmentSource = XYShaderUtil.getRawResource(context, R.raw.fragment_shader1);
        switch (index) {
            case 0:
                fragmentSource = XYShaderUtil.getRawResource(context, R.raw.fragment_shader1);
                break;
            case 1:
                fragmentSource = XYShaderUtil.getRawResource(context, R.raw.fragment_shader2);
                break;
            case 2:
                fragmentSource = XYShaderUtil.getRawResource(context, R.raw.fragment_shader3);
//                fragmentSource = XYShaderUtil.getRawResource(context, R.raw.img_fragment_shader);
                break;

        }

        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        vPosition = GLES20.glGetAttribLocation(program, "v_Position");
        fPosition = GLES20.glGetAttribLocation(program, "f_Position");
        sampler = GLES20.glGetUniformLocation(program, "sTexture");

        int[] vbos = new int[1];
//        1、创建VBO
        GLES20.glGenBuffers(1, vbos, 0);
        vboId = vbos[0];
        //2.绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);

        //3. 分配VBO需要的缓冲大小
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4 + fragmentData.length * 4, null, GLES20.GL_STATIC_DRAW);

        //4,为VBO设置顶点数据的值(这里想象内存区域 先偏移0用于存储顶点坐标,再偏移顶点坐标的大小,用于存储片元坐标) 大小是顶点坐标加上片元坐标大小
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexData.length * 4, vertexBuffer);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, fragmentData.length * 4, fragmentBuffer);
//        5、解绑VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame() {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f, 0f, 0f, 1f);

        GLES20.glUseProgram(program);


        //先绑定vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);//使用imageTexture

        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                0);

        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //第二次绘制

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);//使用imageTexture

        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                32);

        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);//3个点

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        //解绑
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
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


}

```

---

## 的那一surfaceview 渲染多个纹理


![image](http://ws3.sinaimg.cn/large/958c5b69ly1fy1ouo7h8zj20km148qk9.jpg)


原理：
主要是利用OpenGL ES绘制多次，把不同的纹理绘制到纹理或窗口上

实际操作：
只需要改变OpenGL ES从顶点数组开始取点的位置就可以了

1、vertexBuffer.position(index);

2、GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,index);

index:内存中起始位置（字节）
