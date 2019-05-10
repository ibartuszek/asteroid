package hu.elte.asteroid.model;

import java.util.ArrayList;
import java.util.List;

import hu.elte.asteroid.camera.Camera;
import hu.elte.asteroid.components.Asteroid;
import hu.elte.asteroid.components.Beam;
import hu.elte.asteroid.components.Component;
import hu.elte.asteroid.components.ParametricComponent;
import hu.elte.asteroid.components.Ship;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class GameModel {

    private static final float DISTANCE = 300.0f;
    private static final int MINIMUM_NUMBER_OF_ASTEROID = 4;
    private final PApplet pApplet;
    private final Camera camera;
    private final PImage asteroidTexture;
    private final List<Beam> beamList;
    private final List<Asteroid> asteroidList;
    private Ship ship;
    private boolean gameOver = false;

    private GameModel(final GameModelBuilder gameModelBuilder) {
        this.pApplet = gameModelBuilder.pApplet;
        this.asteroidTexture = gameModelBuilder.asteroidTexture;
        this.ship = Ship.createShip(pApplet);
        this.camera = Camera.createCamera(this.pApplet, this.ship);
        this.beamList = new ArrayList<>();
        this.asteroidList = new ArrayList<>();
        this.asteroidList.add(new Asteroid.AsteroidBuilder()
            .withPapplet(this.pApplet)
            .withPosition(new PVector(100, 0, 100))
            .withShipPosition(ship.getPosition())
            .withAsteroidList(asteroidList)
            .withRandomSize()
            .withTexture(asteroidTexture)
            .build());
        this.asteroidList.add(new Asteroid.AsteroidBuilder()
            .withPapplet(this.pApplet)
            .withPosition(new PVector(-100, 0, 100))
            .withShipPosition(ship.getPosition())
            .withAsteroidList(asteroidList)
            .withRandomSize()
            .withTexture(asteroidTexture)
            .build());
    }

    public void draw() {
        if (!gameOver) {
            ship.draw();
        }
        beamList.forEach(Beam::draw);
        asteroidList.forEach(Asteroid::draw);
    }

    public void update() {
        camera.update();
        beamList.forEach(Beam::update);
        asteroidList.forEach(Asteroid::update);
        beamList.removeIf(Beam::isRemoveable);
        asteroidList.removeIf(Asteroid::isRemoveable);
        if (!gameOver) {
            ship.update();
            gameOver = collisionWithShip(ship);
        } else {
            ship = null;
        }
        collision(beamList);
        collision(asteroidList);
        makeNewAsteroids();
    }

    private boolean collisionWithShip(final Ship ship) {
        Component removable = asteroidList.stream()
            .filter(asteroid -> asteroid.isCollision(ship) != null)
            .findFirst()
            .orElse(null);
        return removable != null;
    }

    private void collision(final List<? extends ParametricComponent> componentList) {
        asteroidList.stream()
            .filter(asteroid -> asteroid.isCollision(componentList) != null)
            .findFirst()
            .ifPresent(Asteroid::createTwoSmallerAsteroids);
    }

    public void moveShipForward(boolean on) {
        if (!gameOver) {
            ship.moveForward(on);
        }
    }

    public void rotateShipLeft(boolean on) {
        if (!gameOver) {
            ship.rotateLeft(on);
        }
    }

    public void rotateShipRight(boolean on) {
        if (!gameOver) {
            ship.rotateRight(on);
        }
    }

    public void fire() {
        if (!gameOver) {
            beamList.add(Beam.createBeam(pApplet, ship));
        }
    }

    private void makeNewAsteroids() {
        if (asteroidList.size() < MINIMUM_NUMBER_OF_ASTEROID && !gameOver) {
            this.asteroidList.add(new Asteroid.AsteroidBuilder()
                .withPapplet(this.pApplet)
                .withPosition(ship.getRandomBackPosition(DISTANCE))
                .withShipPosition(ship.getPosition())
                .withAsteroidList(asteroidList)
                .withRandomSize()
                .withTexture(asteroidTexture)
                .build());
        }
    }

    public static class GameModelBuilder {
        private PApplet pApplet;
        private PImage asteroidTexture;

        public GameModel build() {
            return new GameModel(this);
        }

        public GameModelBuilder withPApplet(final PApplet pApplet) {
            this.pApplet = pApplet;
            return this;
        }

        public GameModelBuilder withAsteroidTexture(final PImage asteroidTexture) {
            this.asteroidTexture = asteroidTexture;
            return this;
        }
    }

}
