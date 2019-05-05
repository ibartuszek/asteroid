package hu.elte.asteroid;

import java.awt.Color;

import hu.elte.asteroid.camera.Camera;
import hu.elte.asteroid.model.GameModel;

import processing.core.PApplet;
import processing.core.PImage;

public class Visualization extends PApplet {

    private PImage asteroidTexture;
    private GameModel model;
    private Camera camera;

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
        this.asteroidTexture = loadImage("asteroidTexture.jpg");
        this.model = new GameModel.GameModelBuilder()
            .withPApplet(this)
            .withAsteroidTexture(asteroidTexture)
            .build();
        this.camera = Camera.createCamera(this);

    }

    private void update() {
        drawBackground();
        model.update();
        camera.update();
    }

    private void drawBackground() {
        final Color backgroundColor = Color.decode(Main.BACKGROUND_COLOR);
        background(color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
    }

    @Override
    public void keyPressed() {
        if (key == 'w') {
            camera.moveForward(true);
        } else if (key == 's') {
            camera.moveBackward(true);
        } else if (key == 'a') {
            camera.moveLeft(true);
        } else if (key == 'd') {
            camera.moveRight(true);
        }

        if (keyCode == UP) {
            model.moveShipForward(true);
        } else if (keyCode == LEFT) {
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
        if (key == 'w') {
            camera.moveForward(false);
        } else if (key == 's') {
            camera.moveBackward(false);
        } else if (key == 'a') {
            camera.moveLeft(false);
        } else if (key == 'd') {
            camera.moveRight(false);
        }

        if (keyCode == UP) {
            model.moveShipForward(false);
        } else if (keyCode == LEFT) {
            model.rotateShipLeft(false);
        } else if (keyCode == RIGHT) {
            model.rotateShipRight(false);
        }

    }
}
