package hu.elte.asteroid.components;

import static processing.core.PConstants.TRIANGLES;

import processing.core.PApplet;
import processing.core.PVector;

public class Ship {

    public static final int X_SIDE_LENGTH = 100;
    public static final int Y_SIDE_LENGTH = 200;
    public static final int Z_SIDE_LENGTH = 300;
    private final PApplet pApplet;
    private final PVector pVector;

    private Ship(final PApplet pApplet) {
        this.pApplet = pApplet;
        this.pVector = new PVector(0, 0, 0);
    }

    public static Ship createShip(final PApplet pApplet) {
        return new Ship(pApplet);
    }

    public void draw() {
        // pApplet.fill(color.getRed(), color.getGreen(), color.getBlue());
        pApplet.fill(50);
        pApplet.pushMatrix();
        pApplet.translate(200, 200, 0);
        pApplet.rotateZ(pApplet.millis() / 1000.0f);

        drawShipShape();

        pApplet.popMatrix();
    }

    private void drawShipShape() {
        pApplet.beginShape(TRIANGLES);
        drawTetrahedron();
        drawBase();
        pApplet.endShape();
    }

    private void drawTetrahedron() {
        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(0, 0, Z_SIDE_LENGTH);
        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);

        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(0, 0, Z_SIDE_LENGTH);
        pApplet.vertex(X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);

        pApplet.vertex(X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(0, 0, Z_SIDE_LENGTH);
        pApplet.vertex(X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);

        pApplet.vertex(X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(0, 0, Z_SIDE_LENGTH);
        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);
    }

    private void drawBase() {
        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);

        pApplet.vertex(X_SIDE_LENGTH / 2.0f, Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);
        pApplet.vertex(-X_SIDE_LENGTH / 2.0f, -Y_SIDE_LENGTH / 2.0f, 0);
    }

    public void update() {

    }
}
