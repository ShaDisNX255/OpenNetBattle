#define PI 3.1415926535897932384626433832795

precision lowp float;
precision lowp int;

uniform sampler2D texture;
uniform float factor;
uniform vec4 firstColor;
uniform vec4 secondColor;

varying vec4 vColor;
varying vec2 vTexCoord;

void main()
{
    vec2 pos = vTexCoord;
    vec4 pixel = texture2D(texture, pos);

    if(pixel.a == 0)
      discard;

    // TODO: get rid of this comparison
    if(pixel.r == pixel.g && pixel.g == pixel.b) {
      /* oscillate gradient over time */ 
      /* 0 => 1 => 0 => repeat */
      float neg_half_pi = -0.5 * PI;
      float blend = (sin(neg_half_pi + radians((factor+pixel.r)*180.0)) + 1.0) / 2.0;

      pixel = mix(firstColor, secondColor, blend);
    }

    gl_FragColor = pixel * vColor;
}
