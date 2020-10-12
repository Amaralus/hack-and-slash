#version 410

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec4 color;

out vec4 primitiveColor;

void main() {
    gl_Position = vec4(vertex, 0.0, 1.0);
    primitiveColor = color;
}
