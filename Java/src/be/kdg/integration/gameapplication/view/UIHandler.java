package be.kdg.integration.gameapplication.view;

import javafx.scene.Scene;

import java.net.MalformedURLException;
import java.nio.file.Paths;

public class UIHandler{
    private String variables;
    private String finalScreen;
    private String gameScreen;
    private String leaderboard;
    private String loginPage;
    private String modeSelection;
    private String startScreen;
    private String tutorialPage;
    private String settings;
    private String customAlert;
    private Scene scene;

    public UIHandler(Scene scene){
        this.scene = scene;
        assignDefaultColors();
    }

    public void assignCustomColors(){
        try{
            customAlert = Paths.get("resources/css/custom-alert.css").toUri().toURL().toExternalForm();
            variables = Paths.get("resources/css/color-variables.css").toUri().toURL().toExternalForm();
            finalScreen = Paths.get("resources/css/final-screen.css").toUri().toURL().toExternalForm();
            leaderboard = Paths.get("resources/css/leaderboard.css").toUri().toURL().toExternalForm();
            modeSelection = Paths.get("resources/css/mode-selection.css").toUri().toURL().toExternalForm();
            startScreen = Paths.get("resources/css/start-view.css").toUri().toURL().toExternalForm();
            tutorialPage = Paths.get("resources/css/tutorial-page.css").toUri().toURL().toExternalForm();
            gameScreen = Paths.get("resources/css/game-screen-style.css").toUri().toURL().toExternalForm();
            loginPage = Paths.get("resources/css/login-page.css").toUri().toURL().toExternalForm();
            settings = Paths.get("resources/css/settings-page.css").toUri().toURL().toExternalForm();
        }catch (MalformedURLException e) {
        }
        updateColors();
    }

    public void assignDefaultColors(){
        try{
            customAlert = Paths.get("resources/css-default/custom-alert.css").toUri().toURL().toExternalForm();
            variables = Paths.get("resources/css-default/color-variables.css").toUri().toURL().toExternalForm();
            finalScreen = Paths.get("resources/css-default/final-screen.css").toUri().toURL().toExternalForm();
            leaderboard = Paths.get("resources/css-default/leaderboard.css").toUri().toURL().toExternalForm();
            modeSelection = Paths.get("resources/css-default/mode-selection.css").toUri().toURL().toExternalForm();
            startScreen = Paths.get("resources/css-default/start-view.css").toUri().toURL().toExternalForm();
            tutorialPage = Paths.get("resources/css-default/tutorial-page.css").toUri().toURL().toExternalForm();
            gameScreen = Paths.get("resources/css-default/game-screen-style.css").toUri().toURL().toExternalForm();
            loginPage = Paths.get("resources/css-default/login-page.css").toUri().toURL().toExternalForm();
            settings = Paths.get("resources/css-default/settings-page.css").toUri().toURL().toExternalForm();
        }catch (MalformedURLException e) {
        }
        updateColors();
    }

    public void updateColors(){
        scene.getStylesheets().removeAll();
        scene.getStylesheets().addAll(customAlert,
                variables, finalScreen, gameScreen, leaderboard, loginPage, modeSelection, startScreen, tutorialPage, settings
        );
    }

    public Scene getScene(){
        return scene;
    }

    public String getTutorialPageStyle(){
        return tutorialPage;
    }

    public String getColorVariables(){
        return variables;
    }
}
