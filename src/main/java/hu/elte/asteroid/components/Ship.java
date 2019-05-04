package hu.elte.asteroid.components;

import static processing.core.PConstants.TRIANGLES;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;

public class Ship {

    private static final double DELTA_ALPHA = 0.1;
    private static final float MAX_SPEED = 250.0f;
    private static final float TIME_CONSTANT = 1000.0f;
    private static final int X_SIDE_LENGTH = 20;
    private static final int Y_SIDE_LENGTH = 20;
    private static final int Z_SIDE_LENGTH = 50;
    private static final Color MAIN_COLOR = Color.decode("#474A51");
    private static final Color SUP_COLOR = Color.decode("#A9A9A9");
    private final PApplet pApplet;
    private final float acceleration = 100.0f;
    private PVector position;
    private float lastTime;
    private boolean isMoveForward;
    private float speed = 0.0f;
    private float alpha;
    private boolean isRotateLeft;
    private boolean isRotateRight;

    private Ship(final PApplet pApplet) {
        this.pApplet = pApplet;
        this.position = new PVector(0, 0, 0);
        this.lastTime = this.pApplet.millis();
    }

    public static Ship createShip(final PApplet pApplet) {
        return new Ship(pApplet);
    }

    public void draw() {
        pApplet.fill(MAIN_COLOR.getRed(), MAIN_COLOR.getGreen(), MAIN_COLOR.getBlue());
        pApplet.stroke(SUP_COLOR.getRed(), SUP_COLOR.getGreen(), SUP_COLOR.getBlue());
        pApplet.pushMatrix();
        pApplet.translate(position.x, position.y, position.z);
        pApplet.rotateY(-PApplet.PI / 2);
        pApplet.rotateY(-alpha - PApplet.PI);
        pApplet.rotateZ(pApplet.millis() / TIME_CONSTANT);

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
        float deltaTime = (pApplet.millis() - lastTime) / TIME_CONSTANT;
        lastTime = pApplet.millis();

        PVector forward = getForward();

        if (isMoveForward) {
            speed = speed < MAX_SPEED ? speed + deltaTime * acceleration : MAX_SPEED;
        } else {
            speed = speed > 0 ? speed - deltaTime * acceleration : 0;
        }

        forward.mult(deltaTime * speed);
        position.add(forward);

        if (isRotateLeft) {
            alpha -= DELTA_ALPHA;
        } else if (isRotateRight) {
            alpha += DELTA_ALPHA;
        }
    }

    private PVector getForward() {
        return new PVector(PApplet.cos(alpha), 0, PApplet.sin(alpha));
    }

    public void moveForward(boolean on) {
        isMoveForward = on;
    }

    public void rotateLeft(boolean on) {
        isRotateLeft = on;
    }

    public void rotateRight(boolean on) {
        isRotateRight = on;
    }
}
