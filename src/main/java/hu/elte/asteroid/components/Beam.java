package hu.elte.asteroid.components;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;

public class Beam extends ParametricComponent {

    private static final float MAX_DISTANCE = 300.0f;
    private static final float TIME_CONSTANT = 1000.0f;
    private static final Color MAIN_COLOR = Color.decode("#90EE90");
    private static final Color SUP_COLOR = Color.decode("#90EE90");
    private static final float LENGTH = 5.0f;
    private static final float DIAMETER = 0.5f;
    private static final float SPEED = 200.0f;
    private final PVector startPosition;

    private Beam(final PApplet pApplet, final Ship ship) {
        super(MAIN_COLOR, SUP_COLOR, pApplet, ship.position.copy());
        this.startPosition = ship.position.copy();
        this.alpha = ship.getAlpha();
        this.lastTime = super.pApplet.millis();
        this.distance = 0.0f;
        this.removeAble = false;
    }

    public static Beam createBeam(final PApplet pApplet, final Ship ship) {
        return new Beam(pApplet, ship);
    }

    @Override
    protected void drawCustomShape() {
        pApplet.translate(-LENGTH / 2, 0, 0);
        pApplet.rotateZ(-PApplet.PI / 2);
        pApplet.rotateX(alpha);
        drawTop();
        pApplet.pushMatrix();
        pApplet.translate(0, LENGTH, 0);
        drawTop();
        pApplet.popMatrix();
        pApplet.beginShape(PApplet.TRIANGLES);
        drawParametricShape();
        pApplet.endShape();
    }

    @Override
    public float getRadius() {
        return LENGTH;
    }

    private void drawTop() {
        pApplet.beginShape(PApplet.TRIANGLES);
        PVector p1 = createVector(0);
        for (int i = 0; i <= u_detailness; i++) {
            PVector p2 = createVector(2 * PApplet.PI / u_detailness * i);
            pApplet.vertex(0, 0, 0);
            pApplet.vertex(p1.x, p1.y, p1.z);
            pApplet.vertex(p2.x, p2.y, p2.z);
            p1 = p2;
        }
        pApplet.endShape();
    }

    private PVector createVector(float i2) {
        return new PVector(DIAMETER / 2.0f * PApplet.cos(i2),
            0.0f,
            DIAMETER / 2.0f * PApplet.sin(i2));
    }

    @Override
    PVector getPosition(float u, float v) {
        float alpha = u * 2 * PApplet.PI;
        PVector vector = new PVector(DIAMETER / 2 * PApplet.cos(alpha),
            LENGTH * v,
            DIAMETER / 2 * PApplet.sin(alpha));
        return vector;
    }

    @Override
    PVector getNormal(float u, float v) {
        float alpha = u * 2 * PApplet.PI;
        PVector vector = new PVector(PApplet.cos(alpha), 0, PApplet.sin(alpha));
        return vector;
    }

    public void update() {
        float deltaTime = (pApplet.millis() - lastTime) / TIME_CONSTANT;
        lastTime = pApplet.millis();
        updatePostioion(deltaTime, alpha, SPEED);
        distance = position.copy().sub(startPosition).mag();
        removeAble = removeAble || distance > MAX_DISTANCE;
    }

}
