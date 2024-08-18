#version 150

// The main texture

uniform sampler2D Sampler0;

out vec4 fragColor;
in lowp vec2 texCoord;

uniform float Angle;
uniform int Pixelate;




float angleDeltaToPointFromCenter(vec2 center, vec2 point) {
    float x = distance(center.x, point.x);
    float y = distance(center.y, point.y);
    float result = degrees(atan(y, x)) * 0.5;
    if (point.y < center.y && point.x >= center.x) {
        result = (45. + (45. - (result)));
    }
    if (point.y >= center.y && point.x > center.x) {
        result = (90. + (result));
    }
    if (point.y > center.y && point.x <= center.x) {
        result = (135. + (45. - (result)));
    }
    return result / 180.;
}

vec2 getPixelPos(vec2 uv) {
    vec2 res = textureSize(Sampler0, 0);
    float x = floor(uv.x * float(res.x)) / float(res.x);
    float y = floor(uv.y * float(res.y)) / float(res.y);
    return vec2(x, y);
}

void main() {
    vec4 col = texture(Sampler0, getPixelPos(texCoord));
    float pixel_delta = angleDeltaToPointFromCenter(vec2(0.5), getPixelPos(texCoord));
    float non_pixel_delta = angleDeltaToPointFromCenter(vec2(0.5), texCoord);
    float delta = 0;
    if (Pixelate == 1) {
        delta = pixel_delta;
    } else {
        delta = non_pixel_delta;
    }
    if (col.a > 0.0) {
        if (Angle == 0.0) {
            fragColor.a = 0;
            return;
        }
        if (delta <= Angle / 360.0) {
            if (Pixelate == 1) {
                fragColor = texture(Sampler0, getPixelPos(texCoord));
            } else {
                fragColor = texture(Sampler0, texCoord);
            }
        } else {
            fragColor.a = 0;
        }
    } else {
        fragColor = col;
    }

}