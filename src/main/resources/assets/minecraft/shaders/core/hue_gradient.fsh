#version 150

// The main texture
out vec4 fragColor;
in vec2 texCoord;

uniform float Hue;


vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 10.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    fragColor.rgb = hsv2rgb(vec3(Hue, texCoord.x, 1.0 - texCoord.y));
    fragColor.a = 1.0;
}