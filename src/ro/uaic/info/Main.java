package ro.uaic.info;

import javafx.application.Application;
import javafx.stage.Stage;
import ro.uaic.info.view.MainFrame;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainFrame mainFrame = new MainFrame(primaryStage);
        mainFrame.setTitle("Application");
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
