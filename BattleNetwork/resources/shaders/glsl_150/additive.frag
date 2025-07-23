precision lowp float;
precision lowp int;

varying vec4 vColor;
varying vec2 vTexCoord;
uniform sampler2D texture;

void main()
{
    vec4 pixel = texture2D(texture, vec2(vTexCoord.x, vTexCoord.y));
    vec4 color = (vColor*vColor.a) * (vec4(1.0f)-pixel);
    color = color + pixel;
    color.a = pixel.a;
    gl_FragColor = color;
}
