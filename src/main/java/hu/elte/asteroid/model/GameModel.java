package hu.elte.asteroid.model;

import hu.elte.asteroid.components.Ship;
import processing.core.PApplet;

public class GameModel {

    private final PApplet pApplet;
    private final Ship ship;

    private GameModel(final GameModelBuilder gameModelBuilder){
        this.pApplet = gameModelBuilder.pApplet;
        this.ship = Ship.createShip(pApplet);
    }

    public void draw() {
        ship.draw();
    }

    public void update() {
        ship.update();
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