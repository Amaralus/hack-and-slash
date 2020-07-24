#version 460

layout (location = 0) in vec3 position;

out vec3 vertexColor;
uniform float vertexOffset;

void main() {
    gl_Position = vec4(position.x, position.y, position.z, 1.0f);
}
