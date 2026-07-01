package be.kdg.integration.gameapplication.view.tutorial;


import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TutorialPresenter {
    private TutorialView tutorialView;
    private GameSettings gameSettings;
    private GameSoundPresenter gameSoundPresenter;
    private AuthentificationManager authentificationManager;
    private UIHandler uiHandler;

    public TutorialPresenter(TutorialView tutorialView, GameSettings gameSettings, GameSoundPresenter gameSoundPresenter, AuthentificationManager authentificationManager, UIHandler uiHandler){
        this.tutorialView = tutorialView;
        this.gameSettings = gameSettings;
        this.gameSoundPresenter = gameSoundPresenter;
        this.authentificationManager = authentificationManager;
        this.uiHandler = uiHandler;
        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){
        tutorialView.getButton().setOnAction(e -> {
            Stage stage = (Stage) tutorialView.getScene().getWindow();
            if(stage.getModality() == Modality.APPLICATION_MODAL) closeWindow();
            goBack();
        });
    }

    private void updateView(){

    }

    private void closeWindow(){
        Stage stage = (Stage) tutorialView.getScene().getWindow();
        stage.close();
    }

    private void goBack(){
        StartView startView = new StartView();
        new StartPresenter(startView, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
        tutorialView.getScene().setRoot(startView);
    }

}
