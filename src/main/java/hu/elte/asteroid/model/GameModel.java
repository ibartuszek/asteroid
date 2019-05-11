package hu.elte.asteroid.model;

import java.util.ArrayList;
import java.util.List;

import hu.elte.asteroid.Main;
import hu.elte.asteroid.camera.Camera;
import hu.elte.asteroid.components.Asteroid;
import hu.elte.asteroid.components.Beam;
import hu.elte.asteroid.components.Component;
import hu.elte.asteroid.components.ParametricComponent;
import hu.elte.asteroid.components.ParametricSphere;
import hu.elte.asteroid.components.Ship;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class GameModel {

    private static final float DISTANCE = 300.0f;
    private static final float SKY_RADIUS = 300.0f;
    private static final int INIT_NUMBER_OF_ASTEROIDS = 2;
    private static final int POINTS_ASTEROID_NUMBER_SCALE_FACTOR = 50;
    private final PImage spaceTexture;
    private final PImage asteroidTexture;
    private final PApplet pApplet;
    private final Camera camera;
    private final ParametricSphere sky;
    private final List<Beam> beamList;
    private final List<Asteroid> asteroidList;
    private Ship ship;
    private boolean gameOver = false;
    private int points = 0;
    private int minimumNumberOfAsteroid = 3;
    private int startEndTime = 0;

    private GameModel(final GameModelBuilder gameModelBuilder) {
        this.pApplet = gameModelBuilder.pApplet;
        this.spaceTexture = gameModelBuilder.spaceTexture;
        this.asteroidTexture = gameModelBuilder.asteroidTexture;
        this.ship = Ship.createShip(pApplet);
        this.camera = Camera.createCamera(this.pApplet, this.ship);
        this.sky = ParametricSphere.createSphere(pApplet, SKY_RADIUS, spaceTexture);
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
        drawSky();
        if (!gameOver) {
            ship.draw();
        }
        beamList.forEach(Beam::draw);
        asteroidList.forEach(Asteroid::draw);
        drawGUI();
    }

    private void drawGUI() {
        pApplet.hint(PApplet.DISABLE_DEPTH_TEST);
        pApplet.camera();
        pApplet.textSize(15);
        pApplet.fill(255);
        pApplet.text("Time: " + pApplet.millis() / 10 / 100.0, 50, 50);
        pApplet.fill(0, 255, 0);
        pApplet.text("Points: " + points, 50, 80);
        if (gameOver) {
            pApplet.textSize(64);
            pApplet.fill(150);
            pApplet.text("GAME OVER", Main.WIDTH / 2.0f, Main.HEIGHT - 100.0f);
        }
        pApplet.hint(PApplet.ENABLE_DEPTH_TEST);
    }

    private void drawSky() {
        pApplet.pushMatrix();
        PVector camPos = camera.getEye();
        pApplet.translate(camPos.x, camPos.y, camPos.z);
        pApplet.rotateY(pApplet.PI);
        sky.draw();
        pApplet.popMatrix();
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
            startEndTime = pApplet.millis();
        } else {
            ship = null;
            finishGame();
        }
        collision(beamList);
        collision(asteroidList);
        makeNewAsteroids();
        calculateMinimumNumberOfAsteroids();
    }

    private void finishGame() {
        if (pApplet.millis() - startEndTime > 5000) {
            System.exit(0);
        }
    }

    private void calculateMinimumNumberOfAsteroids() {
        minimumNumberOfAsteroid = points / POINTS_ASTEROID_NUMBER_SCALE_FACTOR + INIT_NUMBER_OF_ASTEROIDS;
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
            .filter(asteroid -> collisionCheck(componentList, asteroid))
            .findFirst()
            .ifPresent(Asteroid::createSmallerAsteroids);
    }

    private boolean collisionCheck(List<? extends ParametricComponent> componentList, Asteroid asteroid) {
        boolean result = false;
        Component component = asteroid.isCollision(componentList);
        if (component != null) {
            result = true;
            if (component instanceof Beam) {
                points += asteroid.getPoint();
            }
        }
        return result;
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
        if (asteroidList.size() < minimumNumberOfAsteroid && !gameOver) {
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
        private PImage spaceTexture;

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

        public GameModelBuilder withSpaceTexture(final PImage spaceTexture) {
            this.spaceTexture = spaceTexture;
            return this;
        }
    }

}
