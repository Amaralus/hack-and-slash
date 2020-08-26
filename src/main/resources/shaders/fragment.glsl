#version 410

in vec2 texCoord;

out vec4 outColor;

uniform sampler2D textureSampler;

void main () {
    vec4 color = texture(textureSampler, texCoord);
    if (color.a < 0.1) discard;
    outColor = color;
}