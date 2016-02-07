package carSimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String APP_TITLE = "CarSimulator";
    private static final double PREF_WINDOW_WIDTH = 800;
    private static final double PREF_WINDOW_HEIGHT = 600;
    private static final String BACKGROUND_PATH = "file:carSimulator/texture.jpg";
    private static final String CAR_IMAGE_PATH = "file:carSimulator/redCar.png";
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private CarModel carModel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(APP_TITLE);

        rootLayout = new AnchorPane();
        rootLayout.setPrefWidth(PREF_WINDOW_WIDTH);
        rootLayout.setPrefHeight(PREF_WINDOW_HEIGHT);

        Scene scene = new Scene(rootLayout);
        this.primaryStage.setScene(scene);

        Rectangle background = new Rectangle();
        background.widthProperty().bind(scene.widthProperty());
        background.heightProperty().bind(scene.heightProperty());
        Image texture = new Image(BACKGROUND_PATH);
        background.setFill(new ImagePattern(texture, 0, 0, texture.getWidth(), texture.getHeight(), false));
        rootLayout.getChildren().add(background);

        carModel = new CarModel(new Image(CAR_IMAGE_PATH));
        carModel.bindSceneWidth(scene.widthProperty());
        carModel.bindSceneHeight(scene.heightProperty());
        rootLayout.getChildren().add(carModel.getImageView());

        Controller controller = new Controller(primaryStage, carModel);
        controller.playTimeline();

        this.primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
