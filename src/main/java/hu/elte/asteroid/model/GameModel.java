package hu.elte.asteroid.model;

import java.util.ArrayList;
import java.util.List;

import hu.elte.asteroid.components.Asteroid;
import hu.elte.asteroid.components.Beam;
import hu.elte.asteroid.components.Ship;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class GameModel {

    private final PApplet pApplet;
    private final PImage asteroidTexture;
    private final Ship ship;
    private final List<Beam> beamList;
    private final List<Asteroid> asteroidList;

    private GameModel(final GameModelBuilder gameModelBuilder) {
        this.pApplet = gameModelBuilder.pApplet;
        this.asteroidTexture = gameModelBuilder.asteroidTexture;
        this.ship = Ship.createShip(pApplet);
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
        ship.draw();
        beamList.forEach(Beam::draw);
        asteroidList.forEach(Asteroid::draw);
    }

    public void update() {
        ship.update();
        beamList.forEach(Beam::update);
        asteroidList.forEach(Asteroid::update);
        beamList.removeIf(Beam::isRemoveable);
        // beamList.stream().forEach(beam -> asteroidList.stream().forEach());
    }

    public void moveShipForward(boolean on) {
        ship.moveForward(on);
    }

    public void rotateShipLeft(boolean on) {
        ship.rotateLeft(on);
    }

    public void rotateShipRight(boolean on) {
        ship.rotateRight(on);
    }

    public void fire() {
        beamList.add(Beam.createBeam(pApplet, ship));
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
