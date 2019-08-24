varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {

    vec4 color = texture2D(u_texture, v_texCoords);

    if (color.r > 0.0) {
       gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    } else {
       gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
    }

}