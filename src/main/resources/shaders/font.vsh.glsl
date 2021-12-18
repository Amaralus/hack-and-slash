#version 410

layout (location = 0) in vec4 vertex;

out vec2 textureCoordinate;

uniform mat4 projection;
uniform mat4 model;

void main() {
    gl_Position = projection * model * vec4(vertex.xy, 0.0, 1.0);
    textureCoordinate = vertex.zw;
}
