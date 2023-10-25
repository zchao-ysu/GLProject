#version 300 es
layout(location=0) in vec4 a_Position;
void main(){
    gl_Position = a_Position;
    gl_PointSize = 100.0;
}

#version 300 es
precision mediump float;
out vec4 u_Color;
void main(){
    u_Color = vec4(1.0, 0.0, 0.0, 1.0);
}
