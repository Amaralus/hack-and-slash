#version 410

in vec2 textureCoordinate;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec2 offset;

void main () {
    vec4 color = texture(textureSampler, textureCoordinate + offset);
    if (color.a < 0.1) discard;
    outColor = color;
}