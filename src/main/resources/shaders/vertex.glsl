#version 410

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec2 textureCoord;

out vec2 texCoord;

uniform mat4 model;
uniform mat4 projection;

void main() {
    gl_Position = gl_Position = projection * model * vec4(vertex.xy, 0.0, 1.0);
    texCoord = textureCoord;
}
