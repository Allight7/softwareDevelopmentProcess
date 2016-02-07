package carSimulator;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

/**
 * User: allight
 * Date: 28.01.2016 1:57
 */

public class CarModel {

    public static final double DELTA_ROUTE_DEGREE = 3;
    public static final double ACCELERATION = 1;
    public static final double FRICTION = 0.1;
    public static final double MIN_SENSIBLE_SPEED = 1;
    public static final double START_ANGLE = Math.PI / 2;
    public static final double START_SPEED = 0;
    public static final double DEGREES_IN_PI = Math.PI / 180;

    private ImageView car;
    private double curAngle = START_ANGLE;
    private double curSpeedY = START_SPEED;
    private double curSpeedX = START_SPEED;
    private DoubleProperty sceneWidth = new SimpleDoubleProperty();
    private DoubleProperty sceneHeight = new SimpleDoubleProperty();
    private AccelerationType lastAccGranted = AccelerationType.FORWARD;

    public enum Direction {
        LEFT, RIGHT
    }

    public enum AccelerationType {
        FORWARD, BACKWARD
    }

    public CarModel(Image img) {
        this.car = new ImageView(img);

        sceneWidth.addListener(observable ->
                getImageView().setLayoutX((sceneWidth.get() - getImageView().getImage().getWidth()) / 2));
        sceneHeight.addListener(observable ->
                getImageView().setLayoutY((sceneHeight.get() - getImageView().getImage().getHeight()) / 2));
    }

    public ImageView getImageView() {
        return car;
    }

    public Image getImage() {
        return car.getImage();
    }

    public void setRoute(Direction direction) {
        double sign = direction == Direction.LEFT ? -1 : 1;
        curAngle += sign * DEGREES_IN_PI * DELTA_ROUTE_DEGREE;
        car.getTransforms().add(new Rotate(sign * DELTA_ROUTE_DEGREE,
                getImage().getWidth() / 2, getImage().getHeight() / 2));
    }

    public void setSpeed(AccelerationType acceleration) {
        double sign = acceleration == AccelerationType.FORWARD ? -1 : 1;
        curSpeedX += sign * Math.cos(curAngle) * ACCELERATION;
        curSpeedY += sign * Math.sin(curAngle) * ACCELERATION;
        lastAccGranted = acceleration;
    }

    public void makeMove() {
        double curPosX = car.getTranslateX() + curSpeedX;
        if (Math.abs(curPosX) > (sceneWidth.get() / 2))
            curPosX = -Math.signum(curPosX) * (sceneWidth.get() / 2 - 1);
        car.setTranslateX(curPosX);

        double curPosY = car.getTranslateY() + curSpeedY;
        if (Math.abs(curPosY) > (sceneHeight.get() / 2))
            curPosY = -Math.signum(curPosY) * (sceneHeight.get() / 2 - 1);
        car.setTranslateY(curPosY);

    }

    public void decreaseSpeed() {
        curSpeedX *= 1 - FRICTION;
        curSpeedY *= 1 - FRICTION;
    }

    public boolean isMoving() {
        return lastAccGranted != null &&
                (Math.abs(curSpeedX) > MIN_SENSIBLE_SPEED ||
                        Math.abs(curSpeedY) > MIN_SENSIBLE_SPEED);
    }

    /**
     * @return null - до старта
     */
    public AccelerationType lastAccGranted() {
        return lastAccGranted;
    }

    public void bindSceneWidth(ReadOnlyDoubleProperty sceneWidth) {
        this.sceneWidth.bind(sceneWidth);
    }

    public void bindSceneHeight(ReadOnlyDoubleProperty sceneHeight) {
        this.sceneHeight.bind(sceneHeight);
    }

    public void printCurrentState() {
        System.out.println("curPosX: " + car.getTranslateX()
                + "\tcurPosY: " + car.getTranslateX()
                + "\tangle: " + curAngle
        );
    }
}
