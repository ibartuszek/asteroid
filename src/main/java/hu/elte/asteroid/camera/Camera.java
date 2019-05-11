package hu.elte.asteroid.camera;

import hu.elte.asteroid.Main;
import hu.elte.asteroid.components.Ship;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {

    private static final float TIME_CONSTANT = 1000.0f;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 600.0f;
    private static final float FOVY = PApplet.PI / 3;
    private static final float ASPECT = Main.WIDTH / (float) Main.HEIGHT;
    private static final PVector EYE_DISTANCE_VECTOR = new PVector(25, -100, 0);
    private static final PVector AT_DISTANCE_VECTOR = new PVector(1, 0, 0);

    private final PApplet pApplet;
    private final Ship ship;
    private final PVector eye;
    private final PVector up;
    private final PVector at;

    private float lastTime;

    private Camera(final PApplet pApplet, final Ship ship) {
        this.pApplet = pApplet;
        this.ship = ship;
        eye = EYE_DISTANCE_VECTOR.copy();
        up = new PVector(0, 1, 0);
        at = AT_DISTANCE_VECTOR.copy();
    }

    public static Camera createCamera(final PApplet pApplet, final Ship ship) {
        return new Camera(pApplet, ship);
    }

    private void applyCamera() {
        pApplet.camera(
            eye.x, eye.y, eye.z,
            at.x, at.y, at.z,
            up.x, up.y, up.z);
        pApplet.perspective(FOVY, ASPECT, Z_NEAR, Z_FAR);
    }

    public void update() {

        eye.x = EYE_DISTANCE_VECTOR.x + ship.getPosition().x;
        eye.y = EYE_DISTANCE_VECTOR.y + ship.getPosition().y;
        eye.z = EYE_DISTANCE_VECTOR.z + ship.getPosition().z;

        at.x = AT_DISTANCE_VECTOR.x + ship.getPosition().x;
        at.y = AT_DISTANCE_VECTOR.y + ship.getPosition().y;
        at.z = AT_DISTANCE_VECTOR.z + ship.getPosition().z;

        applyCamera();
    }

    public PVector getEye() {
        return eye;
    }
}
