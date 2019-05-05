package hu.elte.asteroid.model;

import java.util.ArrayList;
import java.util.List;

import hu.elte.asteroid.components.Beam;
import hu.elte.asteroid.components.Ship;

import processing.core.PApplet;

public class GameModel {

    private final PApplet pApplet;
    private final Ship ship;
    private final List<Beam> beamList;

    private GameModel(final GameModelBuilder gameModelBuilder) {
        this.pApplet = gameModelBuilder.pApplet;
        this.ship = Ship.createShip(pApplet);
        this.beamList = new ArrayList<>();
    }

    public void draw() {
        ship.draw();
        beamList.forEach(Beam::draw);
    }

    public void update() {
        ship.update();
        beamList.forEach(Beam::update);
        beamList.removeIf(Beam::isRemoveable);
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

        public GameModel build() {
            return new GameModel(this);
        }

        public GameModelBuilder withPApplet(final PApplet pApplet) {
            this.pApplet = pApplet;
            return this;
        }

    }

}
