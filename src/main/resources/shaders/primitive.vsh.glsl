#version 410

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec4 color;

out vec4 primitiveColor;

uniform mat4 model;
uniform mat4 projection;

void main() {
    gl_Position = projection * model * vec4(vertex, 0.0, 1.0);
    primitiveColor = color;
}
