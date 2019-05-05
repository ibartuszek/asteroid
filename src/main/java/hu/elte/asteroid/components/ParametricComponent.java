package hu.elte.asteroid.components;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;

abstract class ParametricComponent extends Component {

    float u_detailness = 20.0f;
    float v_detailness = 20.0f;

    ParametricComponent(final Color mainColor, final Color supColor,
        final PApplet pApplet, final PVector position) {
        super(mainColor, supColor, pApplet, position);
    }

    void drawParametricShape() {
        float step_u = 1.0f / u_detailness;
        float step_v = 1.0f / v_detailness;

        for (int i = 0; i < u_detailness; i++) {
            for (int j = 0; j < v_detailness; j++) {
                float u = i * step_u;
                float v = j * step_v;

                PVector p1 = getPosition(u, v);
                PVector p2 = getPosition(u + step_u, v);
                PVector p3 = getPosition(u, v + step_v);
                PVector p4 = getPosition(u + step_u, v + step_v);

                PVector n1 = getNormal(u, v);
                PVector n2 = getNormal(u + step_u, v);
                PVector n3 = getNormal(u, v + step_v);
                PVector n4 = getNormal(u + step_u, v + step_v);

                createVertex(v, p1, n1, u);
                createVertex(v, p2, n2, u + step_u);
                createVertex(v + step_v, p3, n3, u);
                createVertex(v, p2, n2, u + step_u);
                createVertex(v + step_v, p4, n4, u + step_u);
                createVertex(v + step_v, p3, n3, u);

            }
        }

    }

    private void createVertex(float v, PVector p2, PVector n2, float v2) {
        pApplet.vertex(p2.x, p2.y, p2.z, v2, v);
        pApplet.normal(n2.x, n2.y, n2.z);
    }

    abstract PVector getPosition(float u, float v);

    abstract PVector getNormal(float u, float v);

}
