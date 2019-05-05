package hu.elte.asteroid.components;

import static processing.core.PConstants.TRIANGLES;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;

public class Ship extends Component {

    public static final float GRAVITY_FACTOR = 200.0f;
    private static final double DELTA_ALPHA = 0.1;
    private static final float MAX_SPEED = 100.0f;
    private static final float TIME_CONSTANT = 1000.0f;
    private static final float X_SIDE_LENGTH = 5.0f;
    private static final float Y_SIDE_LENGTH = 5.0f;
    private static final float Z_SIDE_LENGTH = 10.0f;
    private static final Color MAIN_COLOR = Color.decode("#474A51");
    private static final Color SUP_COLOR = Color.decode("#A9A9A9");
    private static final float ACCELERATION = 50.0f;
    private float lastTime;
    private boolean isMoveForward;
    private float speed = 0.0f;
    private float alpha;
    private boolean isRotateLeft;
    private boolean isRotateRight;

    private Ship(final PApplet pApplet) {
        super(MAIN_COLOR, SUP_COLOR, pApplet, new PVector(0, 0, 0));
        this.lastTime = super.pApplet.millis();
    }

    public static Ship createShip(final PApplet pApplet) {
        return new Ship(pApplet);
    }

    @Override
    protected void drawCustomShape() {
        pApplet.translate(-Z_SIDE_LENGTH / 2, 0, 0);
        pApplet.rotateY(-alpha + PApplet.PI / 2);
        pApplet.rotateZ(PApplet.PI / 4);
        drawShipShape();
    }

    @Override
    public float getRadius() {
        return Z_SIDE_LENGTH;
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

        PVector forward = getForward(alpha);

        if (isMoveForward) {
            speed = speed < MAX_SPEED ? speed + deltaTime * ACCELERATION : MAX_SPEED;
        } else {
            speed = speed > 0 ? speed - deltaTime * ACCELERATION : 0;
        }

        forward.mult(deltaTime * speed);
        position.add(forward);

        if (isRotateLeft) {
            alpha -= DELTA_ALPHA;
        } else if (isRotateRight) {
            alpha += DELTA_ALPHA;
        }
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

    float getAlpha() {
        return alpha;
    }

}
