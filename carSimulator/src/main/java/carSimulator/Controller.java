package carSimulator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {
    public static final int FRAMES_PER_SECOND = 60;
    private Stage eventHolder;
    private Timeline repeatable;
    private CarModel car;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Controller(Stage eventHolder, CarModel car) {
        this.eventHolder = eventHolder;
        this.car = car;

        this.eventHolder.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            if (code.isArrowKey())
                switch (code) {
                    case UP:
                        upPressed = true;
                        break;
                    case DOWN:
                        downPressed = true;
                        break;
                    case LEFT:
                        leftPressed = true;
                        break;
                    case RIGHT:
                        rightPressed = true;
                        break;
                }
        });

        this.eventHolder.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            KeyCode code = event.getCode();
            if (code.isArrowKey())
                switch (code) {
                    case UP:
                        upPressed = false;
                        break;
                    case DOWN:
                        downPressed = false;
                        break;
                    case LEFT:
                        leftPressed = false;
                        break;
                    case RIGHT:
                        rightPressed = false;
                        break;
                }
        });

        initializeTimeline();
    }

    private void initializeTimeline() {
        repeatable = new Timeline(new KeyFrame(Duration.seconds(1d / FRAMES_PER_SECOND), event -> {
            boolean leftOrRightOnly = leftPressed ^ rightPressed;
            boolean upOrDownOnly = upPressed ^ downPressed;
            boolean movingWithNoAcceleration = !upPressed && !downPressed && car.isMoving();

            if (leftOrRightOnly) {
                CarModel.Direction direction = null;
                if (upOrDownOnly) {
                    direction = (upPressed && leftPressed || downPressed && rightPressed) ?
                            CarModel.Direction.LEFT : CarModel.Direction.RIGHT;
                } else if (movingWithNoAcceleration) {
                    direction = (car.lastAccGranted() == CarModel.AccelerationType.FORWARD && leftPressed) ||
                            (car.lastAccGranted() == CarModel.AccelerationType.BACKWARD && rightPressed) ?
                            CarModel.Direction.LEFT : CarModel.Direction.RIGHT;
                }
                if (direction != null) car.setRoute(direction);
            }

            if (upOrDownOnly) {
                CarModel.AccelerationType acceleration =
                        upPressed ? CarModel.AccelerationType.FORWARD : CarModel.AccelerationType.BACKWARD;
                car.setSpeed(acceleration);
            }

            car.makeMove();
            car.decreaseSpeed();
        }));

        repeatable.setCycleCount(Timeline.INDEFINITE);
    }

    public void playTimeline() {
        repeatable.play();
    }
}
