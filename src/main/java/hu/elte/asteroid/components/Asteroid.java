package hu.elte.asteroid.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Asteroid extends ParametricSphere {

    private static final float MAX_DISTANCE = 300.0f;
    private static final float TIME_CONSTANT = 1000.0f;
    private static final Color MAIN_COLOR = Color.decode("#A59692");
    private static final Color SUP_COLOR = Color.decode("#A59692");
    private static final float SPEED = 25.0f;
    private static final Random RANDOM = new Random();

    // private final PImage asteroidTexture;
    private final PVector shipPosition;
    private final List<Asteroid> asteroidList;
    private final AsteroidSize size;
    private final PVector beta;
    private float time;

    private Asteroid(final AsteroidBuilder asteroidBuilder) {
        super(asteroidBuilder.parametricSphereBuilder);
        super.u_detailness = 7.0f;
        super.v_detailness = 6.0f;
        this.shipPosition = asteroidBuilder.shipPosition;
        this.asteroidList = asteroidBuilder.asteroidList;
        this.size = asteroidBuilder.size;
        this.distance = 0.0f;
        this.lastTime = super.pApplet.millis();
        this.beta = new PVector(getRandomFloat(0.5f), getRandomFloat(0.5f), getRandomFloat(0.5f));
    }

    private static Asteroid createSmallerAsteroid(final PVector position, final Asteroid asteroid) {
        Asteroid result = null;
        if (asteroid != null) {
            AsteroidSize asteroidSize = AsteroidSize.getSmallerSize(asteroid.size);
            if (asteroidSize != null) {
                result = new AsteroidBuilder()
                    .withPapplet(asteroid.pApplet)
                    .withPosition(position)
                    .withShipPosition(asteroid.shipPosition)
                    .withAsteroidList(asteroid.asteroidList)
                    .withSize(asteroidSize)
                    .withTexture(asteroid.texture)
                    .build();
            }
        }
        return result;
    }

    public static void createSmallerAsteroids(final Asteroid asteroid) {
        if (asteroid.size.equals(AsteroidSize.NORMAL)) {
            createTwoSmallerAsteroids(asteroid, asteroid.size.getRadius() * 2, 0);
        } else if (asteroid.size.equals(AsteroidSize.BIG)) {
            createTwoSmallerAsteroids(asteroid, asteroid.size.getRadius() * 2, 0);
            asteroid.asteroidList.add(createSmallerAsteroid(asteroid.position.copy().
                add(new PVector(asteroid.size.getRadius() * 2, 0, asteroid.size.getRadius() * 2)), asteroid));
        }
    }

    private static void createTwoSmallerAsteroids(Asteroid asteroid, float v, float i) {
        asteroid.asteroidList.add(createSmallerAsteroid(asteroid.position.copy().add(new PVector(v, 0, i)), asteroid));
        asteroid.asteroidList.add(createSmallerAsteroid(asteroid.position.copy().add(new PVector(i, 0, v)), asteroid));
    }

    private float getRandomFloat(final float differenceFromOne) {
        return (RANDOM.nextFloat() - differenceFromOne) * 2 * pApplet.PI;
    }

    @Override
    protected void drawCustomShape() {
        pApplet.beginShape(PApplet.TRIANGLES);
        pApplet.texture(texture);
        pApplet.textureMode(PApplet.NORMAL);
        pApplet.noStroke();
        pApplet.rotateX(beta.x * pApplet.millis() / TIME_CONSTANT);
        pApplet.rotateY(beta.y * pApplet.millis() / TIME_CONSTANT);
        pApplet.rotateZ(beta.z * pApplet.millis() / TIME_CONSTANT);
        drawParametricShape();
        pApplet.endShape();
    }

    public Component isCollision(Component other) {
        Component component = null;
        float distance = other.getPosition().copy().sub(position).mag();
        if (distance < other.getRadius() + getRadius()) {
            removeAble = true;
            component = other;
        }
        return component;
    }

    public Component isCollision(final List<? extends ParametricComponent> componentList) {
        Component component = null;
        for (int index = 0; index < componentList.size() && !removeAble; index++) {
            if (componentList.get(index) != this) {
                component = isCollision(componentList.get(index));
                if (component instanceof ParametricComponent) {
                    ((ParametricComponent) component).removeAble = true;
                }
            }
        }
        return component;
    }

    public void update() {
        float deltaTime = (pApplet.millis() - lastTime) / TIME_CONSTANT;
        lastTime = pApplet.millis();

        if (lastTime - time > TIME_CONSTANT * 2) {
            time = lastTime;
            changeDirection();
        }
        updatePostioion(deltaTime, alpha, SPEED);
        distance = position.copy().sub(shipPosition).mag();
        removeAble = removeAble || distance > MAX_DISTANCE;
    }

    private void changeDirection() {
        List<PVector> differences = new ArrayList<>();
        differences.add(shipPosition.copy().mult(Ship.GRAVITY_FACTOR).sub(getSizedPosition()));
        asteroidList.stream()
            .filter(asteroid -> !asteroid.equals(this))
            .forEach(asteroid -> differences.add(asteroid.getSizedPosition().sub(getSizedPosition())));
        PVector result = new PVector(0, 0, 0);
        differences.stream().forEach(result::add);
        alpha = PApplet.atan2(result.z, result.x);
    }

    private PVector getSizedPosition() {
        return position.copy().mult(size.getGravityFactor());
    }

    public int getPoint() {
        return (int) size.getRadius();
    }

    public static class AsteroidBuilder {
        private ParametricSphereBuilder parametricSphereBuilder;
        private PApplet pApplet;
        private PVector position;
        private PVector shipPosition;
        private List<Asteroid> asteroidList;
        private AsteroidSize size;
        private PImage asteroidTexture;

        public Asteroid build() {
            parametricSphereBuilder = new ParametricSphereBuilder();
            parametricSphereBuilder.withMainColor(MAIN_COLOR)
                .withSupColor(SUP_COLOR)
                .withPapplet(pApplet)
                .withPosition(position)
                .withRadius(size.getRadius())
                .withTexture(asteroidTexture);
            return new Asteroid(this);
        }

        public AsteroidBuilder withPapplet(final PApplet papplet) {
            this.pApplet = papplet;
            return this;
        }

        public AsteroidBuilder withPosition(final PVector position) {
            this.position = position;
            return this;
        }

        public AsteroidBuilder withShipPosition(final PVector shipPosition) {
            this.shipPosition = shipPosition;
            return this;
        }

        public AsteroidBuilder withAsteroidList(final List<Asteroid> asteroidList) {
            this.asteroidList = asteroidList;
            return this;
        }

        public AsteroidBuilder withSize(final AsteroidSize size) {
            this.size = size;
            return this;
        }

        public AsteroidBuilder withRandomSize() {
            this.size = AsteroidSize.getRandomSize();
            return this;
        }

        public AsteroidBuilder withTexture(final PImage texture) {
            this.asteroidTexture = texture;
            return this;
        }

    }
}
