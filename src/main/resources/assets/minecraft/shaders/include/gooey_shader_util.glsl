#version 150


#define and &&
#define or ||

struct Rect {
    vec2 pos;
    vec2 dims;
};


Rect getSamplerRect(sampler2D sampler) {
    return Rect(vec2(0,0), textureSize(sampler, 0));
}

