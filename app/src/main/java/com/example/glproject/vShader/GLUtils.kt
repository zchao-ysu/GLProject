package com.example.glproject.vShader

import android.opengl.GLES30
import android.util.Log

object GLUtils {
    private val TAG = "Utils"
    open fun compileShader(type: Int, shaderCode: String): Int {
        //创建一个shader 对象
        val shaderId = try {
            GLES30.glCreateShader(type)
        } catch (e: Exception) {
            0
        }
        if (shaderId == 0) {
            Log.d(TAG, " 创建失败")
            return 0
        }
        //将着色器代码上传到着色器对象中
        GLES30.glShaderSource(shaderId, shaderCode)
        //编译对象
        GLES30.glCompileShader(shaderId)
        //获取编译状态，OpenGL 把想要获取的值放入长度为1的数据首位
        val compileStatus = intArrayOf(1)
        GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
        Log.d(TAG, " compileShader: ${compileStatus[0]}")

        if (compileStatus[0] == 0) {
            Log.d(TAG, " 编译失败: ${GLES30.glGetShaderInfoLog(shaderId)}")
            GLES30.glDeleteShader(shaderId)
            return 0
        }

        return shaderId
    }

    /**
     * 关联着色器代码，组成可执行程序
     */
    open fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        //创建一个 OpenGL 程序对象
        val programId = GLES30.glCreateProgram()
        if (programId == 0) {
            Log.d(TAG, " 创建OpenGL程序对象失败")
            return 0
        }
        //关联顶点着色器
        GLES30.glAttachShader(programId, vertexShaderId)
        //关联片段周色漆
        GLES30.glAttachShader(programId, fragmentShaderId)
        //将两个着色器关联到 OpenGL 对象
        GLES30.glLinkProgram(programId)
        //获取链接状态，OpenGL 把想要获取的值放入长度为1的数据首位
        val linkStatus = intArrayOf(1)
        GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0)
        Log.d(TAG, " linkProgram: ${linkStatus[0]}")

        if (linkStatus[0] == 0) {
            GLES30.glDeleteProgram(programId)
            Log.d(TAG, " 编译失败")
            return 0
        }
        return programId;

    }

    /**
     * 生成可执行程序，并使用该程序
     */
    fun makeProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
        //需要编译着色器，编译成一段可执行的bin，去与显卡交流
        val vertexShader = compileShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        //步骤2，编译片段着色器
        val fragmentShader = compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        val programId = linkProgram(vertexShader, fragmentShader)
        //链接之后就可以删除着色器对象了，不需要了
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)

        //通过OpenGL 使用该程序
        GLES30.glUseProgram(programId)
        return programId
    }
}