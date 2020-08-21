#version 410

layout (location = 0) in vec3 position;
layout (location = 2) in vec2 textureCoord;

out vec2 texCoord;

uniform mat4 model;
uniform mat4 viewProjection;

void main() {
    gl_Position = gl_Position = viewProjection * model * vec4(position, 1.0f);
    texCoord = textureCoord;
}
