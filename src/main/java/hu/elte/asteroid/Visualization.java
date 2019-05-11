package hu.elte.asteroid;

import java.awt.Color;

import hu.elte.asteroid.model.GameModel;

import processing.core.PApplet;
import processing.core.PImage;

public class Visualization extends PApplet {

    private static final String ASTEROID_TEXTURE = "asteroidTexture.jpg";
    private static final String SPACE_TEXTURE = "space.jpg";
    private PImage asteroidTexture;
    private PImage spaceTexture;
    private GameModel model;

    @Override
    public void draw() {
        update();
        model.draw();
    }

    @Override
    public void settings() {
        size(Main.WIDTH, Main.HEIGHT, P3D);
    }

    @Override
    public void setup() {
        this.asteroidTexture = loadImage(ASTEROID_TEXTURE);
        this.spaceTexture = loadImage(SPACE_TEXTURE);
        this.model = new GameModel.GameModelBuilder()
            .withPApplet(this)
            .withAsteroidTexture(asteroidTexture)
            .withSpaceTexture(spaceTexture)
            .build();
    }

    private void update() {
        drawBackground();
        model.update();
    }

    private void drawBackground() {
        final Color backgroundColor = Color.decode(Main.BACKGROUND_COLOR);
        background(color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
    }

    @Override
    public void keyPressed() {
        if (keyCode == LEFT) {
            model.rotateShipLeft(true);
        } else if (keyCode == RIGHT) {
            model.rotateShipRight(true);
        }

        if (key == ' ') {
            model.fire();
        }
    }

    @Override
    public void keyReleased() {
        if (keyCode == LEFT) {
            model.rotateShipLeft(false);
        } else if (keyCode == RIGHT) {
            model.rotateShipRight(false);
        }
    }

    @Override
    public void mousePressed() {
        if (mouseButton == RIGHT) {
            model.moveShipForward(true);
        }
    }

    @Override
    public void mouseReleased() {
        if (mouseButton == RIGHT) {
            model.moveShipForward(false);
        }
    }
}
