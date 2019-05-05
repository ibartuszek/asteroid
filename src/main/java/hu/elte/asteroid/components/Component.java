package hu.elte.asteroid.components;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Component {

    final PApplet pApplet;
    private final Color mainColor;
    private final Color supColor;
    PVector position;

    Component(final Color mainColor, final Color supColor, final PApplet pApplet, final PVector position) {
        this.mainColor = mainColor;
        this.supColor = supColor;
        this.pApplet = pApplet;
        this.position = position;
    }

    public void draw() {
        pApplet.fill(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue());
        pApplet.stroke(supColor.getRed(), supColor.getGreen(), supColor.getBlue());

        pApplet.pushMatrix();
        pApplet.translate(position.x, position.y, position.z);
        drawCustomShape();
        pApplet.popMatrix();
    }

    protected abstract void drawCustomShape();

    PVector getForward(final float alpha) {
        return new PVector(PApplet.cos(alpha), 0, PApplet.sin(alpha));
    }

    public PVector getPosition() {
        return position;
    }

    public abstract float getRadius();

}
