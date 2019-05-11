package hu.elte.asteroid.components;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ParametricSphere extends ParametricComponent {

    private static final Color MAIN_COLOR = Color.decode("#90EE90");
    private static final Color SUP_COLOR = Color.decode("#90EE90");
    final PImage texture;

    private final float radius;

    private ParametricSphere(final PApplet pApplet, final float radius, final PImage texture) {
        super(MAIN_COLOR, SUP_COLOR, pApplet, new PVector(0, 0, 0));
        this.radius = radius;
        this.texture = texture;
    }

    ParametricSphere(final ParametricSphereBuilder builder) {
        super(builder.mainColor, builder.supColor, builder.pApplet, builder.position);
        this.radius = builder.radius;
        this.texture = builder.texture;
    }

    public static ParametricSphere createSphere(final PApplet pApplet, final float radius, final PImage texture) {
        return new ParametricSphere(pApplet, radius, texture);
    }

    @Override
    public PVector getPosition(final float u, final float v) {
        float alpha = u * 2 * PApplet.PI;
        float beta = (1 - v) * PApplet.PI;
        PVector vector = new PVector(
            radius * PApplet.sin(beta) * PApplet.cos(alpha),
            radius * PApplet.cos(beta),
            radius * PApplet.sin(beta) * PApplet.sin(alpha));
        return vector;
    }

    @Override
    public PVector getNormal(float u, float v) {
        float alpha = u * 2 * PApplet.PI;
        float beta = (v) * PApplet.PI;
        PVector vector = new PVector(
            PApplet.sin(beta) * PApplet.cos(alpha),
            PApplet.cos(beta),
            PApplet.sin(beta) * PApplet.sin(alpha));
        return vector;
    }

    @Override
    protected void drawCustomShape() {
        pApplet.beginShape(PApplet.TRIANGLES);
        pApplet.texture(texture);
        pApplet.textureMode(PApplet.NORMAL);
        pApplet.noStroke();
        drawParametricShape();
        pApplet.endShape();
    }

    @Override
    public float getRadius() {
        return radius;
    }

    public static class ParametricSphereBuilder {
        private Color mainColor;
        private Color supColor;
        private PApplet pApplet;
        private PVector position;
        private float radius;
        private PImage texture;

        public ParametricSphereBuilder withMainColor(final Color mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public ParametricSphereBuilder withSupColor(final Color supColor) {
            this.supColor = supColor;
            return this;
        }

        public ParametricSphereBuilder withPapplet(final PApplet pApplet) {
            this.pApplet = pApplet;
            return this;
        }

        public ParametricSphereBuilder withPosition(final PVector position) {
            this.position = position;
            return this;
        }

        public ParametricSphereBuilder withRadius(final float radius) {
            this.radius = radius;
            return this;
        }

        public ParametricSphereBuilder withTexture(final PImage texture) {
            this.texture = texture;
            return this;
        }

        public ParametricSphere build() {
            return new ParametricSphere(this);
        }

    }
}
