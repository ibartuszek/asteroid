package hu.elte.asteroid.camera;

import hu.elte.asteroid.Main;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {

    private final PApplet pApplet;

    private final PVector eye;
    private final PVector up;
    private PVector at;

    private PVector lastMousePos = null;

    private float alpha = 0;
    private float beta = PApplet.PI / 2.0f;

    private boolean is_move_up = false;
    private boolean is_move_down = false;
    private boolean is_move_forward = false;
    private boolean is_move_backward = false;
    private boolean is_move_right = false;
    private boolean is_move_left = false;

    private float speed = 500;
    private float last_time;

    private Camera(final PApplet pApplet) {
        this.pApplet = pApplet;
        eye = new PVector(0, 0, 0);
        up = new PVector(0, 1, 0);
        at = new PVector(1, 0, 0);
    }

    public static Camera createCamera(final PApplet pApplet) {
        Camera camera = new Camera(pApplet);
        camera.last_time = pApplet.millis();
        return camera;
    }

    void applyCamera() {
        pApplet.camera(
            eye.x, eye.y, eye.z,
            at.x, at.y, at.z,
            up.x, up.y, up.z);

        pApplet.perspective(PApplet.PI / 3, Main.WIDTH / (float) Main.HEIGHT, 0.01f, 1000.0f);
    }

    public void update() {
        float delta_time = (pApplet.millis() - last_time) / 1000.0f;
        last_time = pApplet.millis();

        if (is_move_forward) {
            PVector forward = GetForward().mult(delta_time * speed);
            eye.add(forward);
            at.add(forward);
        } else if (is_move_backward) {
            PVector forward = GetForward().mult(delta_time * speed);
            eye.sub(forward);
            at.sub(forward);
        }

        if (is_move_right) {
            PVector right = GetRight().mult(delta_time * speed);
            eye.add(right);
            at.add(right);
        } else if (is_move_left) {
            PVector right = GetRight().mult(delta_time * speed);
            eye.sub(right);
            at.sub(right);
        }

        if (pApplet.mousePressed) {
            if (lastMousePos == null) {
                lastMousePos = new PVector(pApplet.mouseX, pApplet.mouseY);
            } else {
                float dx = (pApplet.mouseX - lastMousePos.x) / 100.0f;
                float dy = (pApplet.mouseY - lastMousePos.y) / 100.0f;

                alpha += dx;
                beta -= dy;

                if (beta < 0.1f) {
                    beta = 0.1f;
                } else if (beta > 3.1) {
                    beta = 3.1f;
                }

                PVector P = new PVector(pApplet.sin(beta) * pApplet.cos(alpha), pApplet.cos(beta),
                    pApplet.sin(beta) * pApplet.sin(alpha));
                at = eye.copy().add(P);

                lastMousePos.x = pApplet.mouseX;
                lastMousePos.y = pApplet.mouseY;
            }
        } else {
            lastMousePos = null;
        }

        //kamera beállítása
        applyCamera();

    }

    PVector GetForward() {
        return at.copy().sub(eye).normalize();
    }

    PVector GetRight() {
        PVector forward = GetForward();
        return forward.copy().cross(up).normalize();
    }

    public void moveLeft(boolean on) {
        is_move_left = on;
    }

    public void moveRight(boolean on) {
        is_move_right = on;
    }

    public void moveForward(boolean on) {
        is_move_forward = on;
    }

    public void moveBackward(boolean on) {
        is_move_backward = on;
    }
}
