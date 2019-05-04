package hu.elte.asteroid.camera;

import hu.elte.asteroid.Main;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {

    private static final float TIME_CONSTANT = 1000.0f;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 500.0f;
    private static final float FOVY = PApplet.PI / 3;
    private static final float ASPECT = Main.WIDTH / (float) Main.HEIGHT;
    private static final int SPEED = 500;
    private static final float MOUSE_POSITION_FACTOR = 100.0f;
    private static final float BETA_MIN = 0.1f;
    private static final float BETA_MAX = 3.1f;

    private final PApplet pApplet;
    private final PVector eye;
    private final PVector up;
    private PVector at;

    private PVector lastMousePos = null;

    private float alpha = 0;
    private float beta = PApplet.PI / 2.0f;

    private boolean isMoveForward = false;
    private boolean isMoveBackward = false;
    private boolean isMoveRight = false;
    private boolean isMoveLeft = false;

    private float speed = SPEED;
    private float lastTime;

    private Camera(final PApplet pApplet) {
        this.pApplet = pApplet;
        eye = new PVector(0, 0, 0);
        up = new PVector(0, 1, 0);
        at = new PVector(1, 0, 0);
    }

    public static Camera createCamera(final PApplet pApplet) {
        Camera camera = new Camera(pApplet);
        camera.lastTime = pApplet.millis();
        return camera;
    }

    private void applyCamera() {
        pApplet.camera(
            eye.x, eye.y, eye.z,
            at.x, at.y, at.z,
            up.x, up.y, up.z);
        pApplet.perspective(FOVY, ASPECT, Z_NEAR, Z_FAR);
    }

    public void update() {
        float passedTime = (pApplet.millis() - lastTime) / TIME_CONSTANT;
        lastTime = pApplet.millis();

        if (isMoveForward) {
            PVector forward = getForward().mult(passedTime * speed);
            eye.add(forward);
            at.add(forward);
        } else if (isMoveBackward) {
            PVector forward = getForward().mult(passedTime * speed);
            eye.sub(forward);
            at.sub(forward);
        }

        if (isMoveRight) {
            PVector right = getRight().mult(passedTime * speed);
            eye.add(right);
            at.add(right);
        } else if (isMoveLeft) {
            PVector right = getRight().mult(passedTime * speed);
            eye.sub(right);
            at.sub(right);
        }

        if (pApplet.mousePressed) {
            if (lastMousePos == null) {
                lastMousePos = new PVector(pApplet.mouseX, pApplet.mouseY);
            } else {
                float dx = (pApplet.mouseX - lastMousePos.x) / MOUSE_POSITION_FACTOR;
                float dy = (pApplet.mouseY - lastMousePos.y) / MOUSE_POSITION_FACTOR;

                alpha += dx;
                beta -= dy;

                if (beta < BETA_MIN) {
                    beta = BETA_MIN;
                } else if (beta > BETA_MAX) {
                    beta = BETA_MAX;
                }

                PVector P = new PVector(
                    PApplet.sin(beta) * PApplet.cos(alpha),
                    PApplet.cos(beta),
                    PApplet.sin(beta) * PApplet.sin(alpha));
                at = eye.copy().add(P);

                lastMousePos.x = pApplet.mouseX;
                lastMousePos.y = pApplet.mouseY;
            }
        } else {
            lastMousePos = null;
        }
        applyCamera();
    }

    private PVector getForward() {
        return at.copy().sub(eye).normalize();
    }

    private PVector getRight() {
        PVector forward = getForward();
        return forward.copy().cross(up).normalize();
    }

    public void moveLeft(final boolean on) {
        isMoveLeft = on;
    }

    public void moveRight(final boolean on) {
        isMoveRight = on;
    }

    public void moveForward(final boolean on) {
        isMoveForward = on;
    }

    public void moveBackward(final boolean on) {
        isMoveBackward = on;
    }
}
