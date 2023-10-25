package com.example.glproject

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.example.glproject.vShader.GLUtils.makeProgram
import com.example.glproject.vShader.Triangle
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestRenderer(var context: Context) : Renderer {
    private val TAG = "TestRenderer"
    var r = 1f
    lateinit var mTriangle: Triangle
    private val vPMatrix = FloatArray(16) // 模型视图投影矩阵

    //定点的数据，只有一个点，就放中心即可
    private val POINT_DATA = floatArrayOf(0f, 0f, 1f, 0f)

    /**
     * Float类型占4Byte
     */
    private val BYTES_PER_FLOAT = 4

    /**
     * 每个顶点数据关联的分量个数：当前案例只有x、y，故为2
     */
    private val POSITION_COMPONENT_COUNT = 2

    private val VERTEX_SHADER = """
        #version 300 es
layout(location=0) in vec4 a_Position;
void main(){
    gl_Position = a_Position;
    gl_PointSize = 100.0;
}
"""
    private val FRAGMENT_SHADER = """#version 300 es
precision mediump float;
out vec4 u_Color;
void main(){
    u_Color = vec4(1.0, 0.0, 0.0, 1.0);
}"""

    private var vertexData = ByteBuffer
        // 分配顶点坐标分量个数 * Float占的Byte位数
        .allocateDirect(POINT_DATA.size * BYTES_PER_FLOAT)
        // 按照本地字节序排序
        .order(ByteOrder.nativeOrder())
        // Byte类型转Float类型
        .asFloatBuffer()
        .put(POINT_DATA)
        //将缓冲区的指针指到头部，保证数据从头开始
        .position(0)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
//        // 关闭抗抖动
//        gl.glDisable(GL10.GL_DITHER);
//        //设置系统对透视进行修正
//        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
//
//        //设置阴影平滑模式
//        gl.glShadeModel(GL10.GL_SMOOTH);
//        //启用深度测试
//        gl.glEnable(GL10.GL_DEPTH_TEST);
//        //设置深度测试的类型
//        gl.glDepthFunc(GL10.GL_LEQUAL);
        //白色背景
        GLES30.glClearColor(1f, 1f, 1f, 1f)
        // 编译着色器相关程序
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        // 关联顶点坐标属性和缓存数据，参数说明如下：
        GLES30.glVertexAttribPointer(
            0, //位置索引
            POSITION_COMPONENT_COUNT,//用几个分量描述一个顶点
            GLES30.GL_FLOAT,//分量类型
            false, //固定点数据值是否应该被归一化
            0, //指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0
            vertexData
        ) //顶点数据缓冲区

        //通知GL程序使用指定的顶点属性索引
        GLES30.glEnableVertexAttribArray(0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // 设置3D视窗的大小及位置
        gl.glViewport(0, 0, width, height);
//        //将当前矩阵模式设为投影矩阵
//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        //初始化单位矩阵
//        gl.glLoadIdentity();
//        //计算透视视窗的宽度、高度比
//        val ratio = width.toFloat() / height;
//        //调用此方法设置透视视窗的空间大小
//        gl.glFrustumf(-ratio, ratio, -1f, 1f, 1f, 10f);
    }

    override fun onDrawFrame(gl: GL10) {
        Log.d(TAG, "onDrawFrame: ")
        //清屏
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        //绘制
        GLES30.glDrawArrays(GL10.GL_LINES, 0, 2);

    }
}