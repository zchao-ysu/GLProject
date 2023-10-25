package com.example.glproject.vShader

import android.content.Context
import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(context: Context) {
    companion object {
        // 坐标数组中每个顶点的坐标数
        private const val COORDINATE_PER_VERTEX = 3
    }

    private var programHandle: Int = 0
    private var positionHandle: Int = 0
    private var colorHandler: Int = 0
    private var vPMatrixHandle: Int = 0
    private var vertexStride = COORDINATE_PER_VERTEX * 4

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

    // 三角形的三条边
    private var triangleCoordinate = floatArrayOf(     // 逆时针的顺序的三条边
        0.0f, 0.5f, 0.0f,      // top
        -0.5f, -0.5f, 0.0f,    // bottom left
        0.5f, -0.5f, 0.0f      // bottom right
    )

    // 颜色数组
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoordinate.size * 4).run {
            // ByteBuffer使用本机字节序
            this.order(ByteOrder.nativeOrder())
            // ByteBuffer to FloatBuffer
            this.asFloatBuffer().apply {
                put(triangleCoordinate)
                position(0)
            }
        }

    init {
        // read shader sourceCode
        val vertexShaderCode =
            VERTEX_SHADER
        val fragmentShaderCode =
            FRAGMENT_SHADER
        if (vertexShaderCode.isNullOrEmpty() || fragmentShaderCode.isNullOrEmpty()) {
            throw RuntimeException("vertexShaderCode or fragmentShaderCode is null or empty")
        }
        // compile shader
        val vertexShaderHandler = GLUtils.compileShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShaderHandler =
            GLUtils.compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)
        // create and link program
        programHandle = GLUtils.linkProgram(vertexShaderHandler, fragmentShaderHandler)
    }

    /**
     *  绘制方法
     */
    fun draw(mvpMatrix: FloatArray) {
        GLES30.glUseProgram(programHandle)
        // 获取attribute变量的地址索引
        // get handle to vertex shader's vPosition member
        positionHandle = GLES30.glGetAttribLocation(programHandle, "a_Position").also {
            // enable vertex attribute，默认是disable
            GLES30.glEnableVertexAttribArray(it)
            GLES30.glVertexAttribPointer(
                it, // 着色器中第一个顶点属性的位置
                COORDINATE_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                vertexStride, // 连续的顶点属性组之间的间隔
                vertexBuffer
            )
        }
        // get handle to fragment shader's vColor member
        colorHandler = GLES30.glGetUniformLocation(programHandle, "u_Color").also {
            GLES30.glUniform4fv(it, 1, color, 0)
        }
        // draw triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, triangleCoordinate.size / COORDINATE_PER_VERTEX)
        GLES30.glDisableVertexAttribArray(positionHandle)
    }
}