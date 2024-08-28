#version 150

// The main texture

uniform float r;
uniform float g;
uniform float b;
uniform float a;

out vec4 fragColor;
in vec2 texCoord;
uniform sampler2D Sampler0;


void main() {
    vec4 col = texture(Sampler0, texCoord);

    if (distance(texCoord, vec2(0.5,0.5)) <= 0.5) {
        fragColor = vec4(r,g,b,a);
    } else {
        fragColor.a = 0.0;
    }
}