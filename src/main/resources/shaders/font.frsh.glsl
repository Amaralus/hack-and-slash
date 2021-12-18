#version 410

in vec2 textureCoordinate;

out vec4 outColor;

uniform sampler2D sampler;
uniform vec3 fontColor;

void main () {
    vec4 sampled = vec4(1.0, 1.0, 1.0, texture(sampler, textureCoordinate).r);
    vec4 color = vec4(fontColor, 1.0) * sampled;
    if (color.a < 0.1) discard;
    outColor = color;
}