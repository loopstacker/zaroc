package be.kdg.integration.gameapplication;

import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.loginscreen.LoginScreenPresenter;
import be.kdg.integration.gameapplication.view.loginscreen.LoginScreenView;
import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameRun extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        Logger.getLogger("jakarta.mail").setLevel(Level.SEVERE); // to ignore the jakarta-mail missing config file warning
        LoginScreenView loginScreenView = new LoginScreenView();
        AuthentificationManager authentificationManager = new AuthentificationManager();
        Scene scene = new Scene(loginScreenView);
        UIHandler uiHandler = new UIHandler(scene);
        new LoginScreenPresenter(loginScreenView, authentificationManager, uiHandler);
        primaryStage.setTitle("Zaroc");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setScene(scene);
        try{
            primaryStage.getScene().setCursor(new ImageCursor(new Image(Paths.get("resources/cursor.png").toUri().toURL().toExternalForm())));
            primaryStage.getIcons().add(new Image(Paths.get("resources/zaroc_game_icon.png").toUri().toURL().toExternalForm()));
        }catch (MalformedURLException ignored){}
        primaryStage.show();
    }
}
