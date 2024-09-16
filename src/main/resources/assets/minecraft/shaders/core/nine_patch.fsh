#version 150

#moj_import <gooey_shader_util.glsl>

// The main texture
out vec4 fragColor;
in vec2 texCoord;
uniform sampler2D Sampler0;

uniform ivec2 quad_size;

uniform int edge_thickness;
uniform int tile_center;
uniform int tile_edges;


vec2 getPixelSize() {
    return textureSize(Sampler0, 0);
}

vec2 getQuadPixelPos(vec2 uv) {
    vec2 res = quad_size;
    float x = floor(uv.x * float(res.x)) / float(res.x);
    float y = floor(uv.y * float(res.y)) / float(res.y);
    return vec2(x, y);
}

vec2 getPixelPos(vec2 uv) {
    vec2 res = getPixelSize();
    float x = floor(uv.x * float(res.x)) / float(res.x);
    float y = floor(uv.y * float(res.y)) / float(res.y);
    return vec2(x, y);
}

vec2 getUV(vec2 uv) {
    vec2 uvTexCoord = uv * getPixelSize();
    vec2 uvQuadCoord = uv * quad_size;
    vec2 quadUV = getQuadPixelPos(uv);
    vec2 texUv = getPixelPos(uv);
    vec2 halfSize = getPixelSize() / 2.0;
    vec2 sub = uvQuadCoord - (quad_size - halfSize);
    vec2 result = mod(getQuadPixelPos(uv) * quad_size, vec2(getPixelSize())) / getPixelSize();
    bool inEdgeMaxX = sub.x > halfSize.x - float(edge_thickness);
    bool inEdgeMaxY = sub.y >  halfSize.y - float(edge_thickness);
    bool inCenterMaxX = sub.x > halfSize.x - float(edge_thickness);
    bool inCenterMaxY = sub.y > halfSize.y - float(edge_thickness);
    bool inEdgeMinX = sub.x < getPixelSize().x - quad_size.x - (halfSize.x - float(edge_thickness));
    bool inEdgeMinY = sub.y < getPixelSize().y - quad_size.y - (halfSize.y - float(edge_thickness));
    float xEdge = halfSize.x - float(edge_thickness);
    vec2 subSize = (vec2(sub.x, sub.y) / getPixelSize());
    float loopX = ((mod(uvQuadCoord.x, (halfSize.x - float(edge_thickness)) * 2.0) + float(edge_thickness)) / getPixelSize().x);
    float loopY = ((mod(uvQuadCoord.y, (halfSize.y - float(edge_thickness)) * 2.0) + float(edge_thickness)) / getPixelSize().y);
    float maxEdgeStartX = (mod((subSize.x * getPixelSize().x),  float(edge_thickness)) / getPixelSize().x);
    float maxEdgeStartY =  (mod( (subSize.y * getPixelSize().y), float(edge_thickness)) / getPixelSize().y);
    //edges
    if (inEdgeMaxX && !(inEdgeMaxY || inEdgeMinY)) {
        return vec2(subSize.x + (halfSize.x / getPixelSize().x), loopY);
    }
    if (inEdgeMaxY && !(inEdgeMaxX || inEdgeMinX)) {
        return vec2(loopX, subSize.y + (halfSize.y / getPixelSize().y));
    }
    if (inEdgeMinX && !(inEdgeMaxY || inEdgeMinY)) {
        return vec2(result.x, loopY);
    }
    if (inEdgeMinY && !(inEdgeMaxX || inEdgeMinX)) {
        return vec2(loopX, result.y);
    }
    //center
    if ((!inCenterMaxX && !inEdgeMinX) && (!inCenterMaxY && !inEdgeMinY)) {
        return (mod(uvQuadCoord, getPixelSize() - vec2(float(edge_thickness) * 2.0)) + vec2(float(edge_thickness))) / getPixelSize();
    }
    //corners
    if (sub.x > 0.0 && !(sub.y > 0.0)) {
        return (vec2(0.5, (uvQuadCoord.y / uvTexCoord.y) - 0.5) + vec2(sub.x, sub.y) / getPixelSize());
    }
    if (sub.x > 0.0 && sub.y > 0.0) {
        return (vec2(0.5, 0.5) + vec2(sub.x, sub.y) / getPixelSize());
    }
    if (sub.y > 0.0 && !(sub.x > 0.0)) {
        return (vec2((uvQuadCoord.x / uvTexCoord.x) - 0.5, 0.5) + vec2(sub.x, sub.y) / getPixelSize());
    }
    return result;
}

void main() {

    fragColor = texture(Sampler0, getUV(texCoord));

}