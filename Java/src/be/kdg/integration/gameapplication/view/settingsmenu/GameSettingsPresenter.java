package be.kdg.integration.gameapplication.view.settingsmenu;

import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.settings.GameSoundEntity;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.user.Player;
import be.kdg.integration.gameapplication.model.user.PlayerConfigurationException;
import be.kdg.integration.gameapplication.model.user.User;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.AdjustableColor;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.customalert.CustomAlertCall;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Map;

public class GameSettingsPresenter{
    private GameSoundPresenter gameSoundPresenter;
    private GameSettingsView view;
    private GameSettings model;
    private Stage primaryStage;
    private User user;
    private AuthentificationManager authentificationManager;
    private boolean initialized = false;
    private UIHandler uiHandler;

    public GameSettingsPresenter(GameSettings model, GameSettingsView view, GameSoundPresenter gameSoundPresenter, Scene currentScene, AuthentificationManager authentificationManager, UIHandler uiHandler){
        this.gameSoundPresenter = gameSoundPresenter;
        this.view = view;
        this.model = model;
        this.authentificationManager = authentificationManager;
        this.user = model.getUser();
        this.primaryStage = (Stage) currentScene.getWindow();
        this.uiHandler = uiHandler;
        reloadStyleSheets();
        initializeDependencies();
        updateView();
        updateDefaultLayoutCheckBoxAction();
    }

    private void initializeDependencies(){
        initializeComboBoxForScreenSizes();
        initializeSliders();
        addEventHandlers();
        initializeClickSoundForSliders();
        loadSelectedConfig();
        initializeMusicList();
    }

    private void initializeComboBoxForScreenSizes(){
        view.getSizesComboBox().getItems().addAll(GameSettings.ScreenSizes.values());
    }

    private void updateMusicCheckBoxes(){
        int z = 0;
        for(char x : user.getCurrentConfig().getAvailableSongsFlags().toCharArray()){
            CheckBox checkB = new CheckBox();
            if(view.getMusicCheckBoxField().getChildren().get(z) instanceof HBox hBox){
                if(hBox.getChildren().getFirst() instanceof CheckBox checkBox){
                    checkB = checkBox;

                }
            }
            checkB.setDisable(false);
            checkB.setSelected(x == '1');
            z++;
        }
        blockMusicBoxIfLastLeft();
    }

    private void blockMusicBoxIfLastLeft(){
        if(user.getCurrentConfig().isOnlyOneSongSelected()){
            int z = 0;
            for(char x : user.getCurrentConfig().getAvailableSongsFlags().toCharArray()){
                CheckBox checkB = new CheckBox();
                if(view.getMusicCheckBoxField().getChildren().get(z) instanceof HBox hBox){
                    if(hBox.getChildren().getFirst() instanceof CheckBox checkBox){
                        checkB = checkBox;
                    }
                }
                checkB.setDisable(x == '1');
                z++;
            }
        }else{

        }
    }

    private void initializeMusicList(){
        view.getMusicCheckBoxField().getChildren().clear();
        int i = 0;
        for(GameSoundEntity entry : model.getMusicCollection().values()){
            HBox entity = new HBox();
            CheckBox checkBox = new CheckBox();
            Label songName = new Label(entry.getName());
            checkBox.setSelected(entry.isAvailable());
            entity.getChildren().addAll(checkBox, songName);
            checkBox.setSelected(model.getMusicCollection().get(i).isAvailable());
            final int x = i;
            checkBox.setOnMouseClicked(e -> {
                if(checkBox.isSelected()){
                    user.getCurrentConfig().turnOnSongNumber(x);
                }else{
                    user.getCurrentConfig().turnOffSongNumber(x);
                }
                updateMusicCheckBoxes();
            });
            view.getMusicCheckBoxField().getChildren().add(entity);
            i++;
        }
        updateMusicCheckBoxes();
    }

    private void initializeSliders(){
        view.getRedSliderEnemyPiece().setMin(AdjustableColor.MIN_COLOR);
        view.getRedSliderEnemyPiece().setMax(AdjustableColor.MAX_COLOR);
        view.getGreenSliderEnemyPiece().setMin(AdjustableColor.MIN_COLOR);
        view.getGreenSliderEnemyPiece().setMax(AdjustableColor.MAX_COLOR);
        view.getBlueSliderEnemyPiece().setMin(AdjustableColor.MIN_COLOR);
        view.getBlueSliderEnemyPiece().setMax(AdjustableColor.MAX_COLOR);

        view.getRedSliderPlayerPiece().setMin(AdjustableColor.MIN_COLOR);
        view.getRedSliderPlayerPiece().setMax(AdjustableColor.MAX_COLOR);
        view.getGreenSliderPlayerPiece().setMin(AdjustableColor.MIN_COLOR);
        view.getGreenSliderPlayerPiece().setMax(AdjustableColor.MAX_COLOR);
        view.getBlueSliderPlayerPiece().setMin(AdjustableColor.MIN_COLOR);
        view.getBlueSliderPlayerPiece().setMax(AdjustableColor.MAX_COLOR);

        for(Slider slider : view.getLeftColorGradientsSliders()){
            slider.setMin(AdjustableColor.MIN_COLOR);
            slider.setMax(AdjustableColor.MAX_COLOR);
        }

        for(Slider slider : view.getRightColorGradientsSliders()){
            slider.setMin(AdjustableColor.MIN_COLOR);
            slider.setMax(AdjustableColor.MAX_COLOR);
        }

        view.getMusicVolume().setMin(0);
        view.getMusicVolume().setMax(1);
        view.getSoundEffectsVolumeSlider().setMin(0);
        view.getSoundEffectsVolumeSlider().setMax(1);
    }

    private void addEventHandlers(){
        //----------------- ADDING 3-LAYER SLIDERS LEFT -----------------------//
        //----------- LAYER 1 -----------//
        view.getLeftFirstColorSlider1().setOnMouseDragged(mouseEvent -> {
            model.getFirstColorLeft().setRed(view.getLeftFirstColorSlider1().getValue());
            updatePalette();
        });
        view.getLeftFirstColorSlider2().setOnMouseDragged(mouseEvent -> {
            model.getFirstColorLeft().setGreen(view.getLeftFirstColorSlider2().getValue());
            updatePalette();
        });
        view.getLeftFirstColorSlider3().setOnMouseDragged(mouseEvent -> {
            model.getFirstColorLeft().setBlue(view.getLeftFirstColorSlider3().getValue());
            updatePalette();
        });
        //----------- LAYER 2 -----------//
        view.getLeftSecondColorSlider1().setOnMouseDragged(mouseEvent -> {
            model.getSecondColorLeft().setRed(view.getLeftSecondColorSlider1().getValue());
            updatePalette();
        });
        view.getLeftSecondColorSlider2().setOnMouseDragged(mouseEvent -> {
            model.getSecondColorLeft().setGreen(view.getLeftSecondColorSlider2().getValue());
            updatePalette();
        });
        view.getLeftSecondColorSlider3().setOnMouseDragged(mouseEvent -> {
            model.getSecondColorLeft().setBlue(view.getLeftSecondColorSlider3().getValue());
            updatePalette();
        });
        //----------- LAYER 3 -----------//
        view.getLeftThirdColorSlider1().setOnMouseDragged(mouseEvent -> {
            model.getThirdColorLeft().setRed(view.getLeftThirdColorSlider1().getValue());
            updatePalette();
        });
        view.getLeftThirdColorSlider2().setOnMouseDragged(mouseEvent -> {
            model.getThirdColorLeft().setGreen(view.getLeftThirdColorSlider2().getValue());
            updatePalette();
        });
        view.getLeftThirdColorSlider3().setOnMouseDragged(mouseEvent -> {
            model.getThirdColorLeft().setBlue(view.getLeftThirdColorSlider3().getValue());
            updatePalette();
        });
        //--------------------------- END --------------------------------//

        //----------------- ADDING 3-LAYER SLIDERS RIGHT -----------------------//
        //----------- LAYER 1 -----------//
        view.getRightFirstColorSlider1().setOnMouseDragged(mouseEvent -> {
            model.getFirstColorRight().setRed(view.getRightFirstColorSlider1().getValue());
            model.updateColors();
            updatePalette();
        });
        view.getRightFirstColorSlider2().setOnMouseDragged(mouseEvent -> {
            model.getFirstColorRight().setGreen(view.getRightFirstColorSlider2().getValue());
            model.updateColors();
            updatePalette();
        });
        view.getRightFirstColorSlider3().setOnMouseDragged(mouseEvent -> {
            model.getFirstColorRight().setBlue(view.getRightFirstColorSlider3().getValue());
            model.updateColors();
            updatePalette();
        });
        //----------- LAYER 2 -----------//
        view.getRightSecondColorSlider1().setOnMouseDragged(mouseEvent -> {
            model.getSecondColorRight().setRed(view.getRightSecondColorSlider1().getValue());
            model.updateColors();
            updatePalette();
        });
        view.getRightSecondColorSlider2().setOnMouseDragged(mouseEvent -> {
            model.getSecondColorRight().setGreen(view.getRightSecondColorSlider2().getValue());
            model.updateColors();
            updatePalette();
        });
        view.getRightSecondColorSlider3().setOnMouseDragged(mouseEvent -> {
            model.getSecondColorRight().setBlue(view.getRightSecondColorSlider3().getValue());
            model.updateColors();
            updatePalette();
        });
        //----------- LAYER 3 -----------//
        view.getRightThirdColorSlider1().setOnMouseDragged(mouseEvent -> {
            model.getThirdColorRight().setRed(view.getRightThirdColorSlider1().getValue());
            model.updateColors();
            updatePalette();
        });
        view.getRightThirdColorSlider2().setOnMouseDragged(mouseEvent -> {
            model.getThirdColorRight().setGreen(view.getRightThirdColorSlider2().getValue());
            model.updateColors();
            updatePalette();
        });
        view.getRightThirdColorSlider3().setOnMouseDragged(mouseEvent -> {
            model.getThirdColorRight().setBlue(view.getRightThirdColorSlider3().getValue());
            model.updateColors();
            updatePalette();
        });
        //--------------------------- END --------------------------------//

        //Player piece sliders
        view.getRedSliderPlayerPiece().setOnMouseDragged(mouseEvent -> {
            model.getPlayerPieceColor().setRed(view.getRedSliderPlayerPiece().getValue());
            model.updateColors();
            updateView();
        });

        view.getBlueSliderPlayerPiece().setOnMouseDragged(mouseEvent -> {
            model.getPlayerPieceColor().setBlue(view.getBlueSliderPlayerPiece().getValue());
            model.updateColors();
            updateView();
        });

        view.getGreenSliderPlayerPiece().setOnMouseDragged(mouseEvent -> {
            model.getPlayerPieceColor().setGreen(view.getGreenSliderPlayerPiece().getValue());
            model.updateColors();
            updateView();
        });

        //Enemy piece sliders
        view.getRedSliderEnemyPiece().setOnMouseDragged(mouseEvent -> {
            model.getEnemyPieceColor().setRed(view.getRedSliderEnemyPiece().getValue());
            model.updateColors();
            updateView();
        });

        view.getGreenSliderEnemyPiece().setOnMouseDragged(mouseEvent -> {
            model.getEnemyPieceColor().setGreen(view.getGreenSliderEnemyPiece().getValue());
            model.updateColors();
            updateView();
        });

        view.getBlueSliderEnemyPiece().setOnMouseDragged(mouseEvent -> {
            model.getEnemyPieceColor().setBlue(view.getBlueSliderEnemyPiece().getValue());
            model.updateColors();
            updateView();
        });

        primaryStage.setOnCloseRequest(windowEvent -> {
            updateConfigsInDB(view);
        });

        view.getSizesComboBox().setOnAction(actionEvent -> {
            model.setScreenSize(GameSettings.ScreenSizes.valueOf(
                    view.getSizesComboBox().getSelectionModel().getSelectedItem().toString()));
            updateView();
        });

        view.getDefaultColorCheckBox().setSelected(model.isDefaultColorPaletteApplied());
        view.getDefaultColorCheckBox().setOnAction(actionEvent -> {
            model.setDefaultColorPaletteApplied(view.getDefaultColorCheckBox().isSelected());
            updateDefaultLayoutCheckBoxAction();
        });


        view.getMusicVolume().setOnMouseDragged(dragEvent -> {
            model.setMusicVolume(view.getMusicVolume().getValue());
            updateView();
        });

        view.getSoundEffectsVolumeSlider().setOnMouseDragged(dragEvent -> {
            model.setSoundEffectsVolume(view.getSoundEffectsVolumeSlider().getValue());
            updateView();
        });

        view.getSaveButton().setOnMouseClicked(mouseEvent -> {
            String configName = view.getTextField().getText();
            if(!configName.isEmpty()){
                model.setConfigName(configName);
                updateView();
            }
        });

        view.getDefaultColorCheckBox().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });
        view.getDefaultConfigNameButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });
        view.getSaveButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });
        view.getDefaultConfigNameButton().setOnMouseClicked(mouseEvent -> {
            model.applyConfigNameByDefault();
            updateView();
        });

        view.getGoBackButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });
        view.getGoBackButton().setOnMouseClicked(mouseEvent -> {
            updateView();
            returnToMenu();
        });

        for(int i = 0; i < 5; i++){
            int finalI = i;
            view.getConfigButtons()[i].setOnMousePressed(event -> {
                gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            });

            view.getConfigButtons()[i].setOnAction(actionEvent -> {
                for(ToggleButton tButton : view.getConfigButtons()){
                    tButton.setSelected(false);
                }
                user.setCurrentConfigNumber(finalI);
                view.getConfigButtons()[finalI].setSelected(true);
                loadSelectedConfig();
            });
        }
    }

    private void initializeClickSoundForSliders(){
        for(Slider sl : view.getLeftColorGradientsSliders()){
            sl.setOnMousePressed(e -> {
                gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            });
        }

        for(Slider sl : view.getRightColorGradientsSliders()){
            sl.setOnMousePressed(e -> {
                gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            });
        }
    }

    private void returnToMenu(){
        StartView startView = new StartView();
        startView.getStylesheets().clear();
        uiHandler.updateColors();
        new StartPresenter(startView, authentificationManager, model, gameSoundPresenter, uiHandler);
        view.getScene().setRoot(startView);
        updateConfigsInDB(view);
    }


    private void reloadStyleSheets(){
        view.getStylesheets().removeAll();
        view.getStylesheets().clear();
        if(view.getScene() == null) return;
        view.getScene().getStylesheets().clear();
        view.getScene().getStylesheets().removeAll();
        uiHandler.updateColors();
        view.applyCss();
    }

    private void updateDefaultLayoutCheckBoxAction(){
        if(model.isDefaultColorPaletteApplied()){
            for(Slider slider : view.getLeftColorGradientsSliders()){
                slider.setDisable(true);
            }
            for(Slider slider : view.getRightColorGradientsSliders()){
                slider.setDisable(true);
            }
            uiHandler.assignDefaultColors();
            updatePalette();
            uiHandler.updateColors();
        }else{
            for(Slider slider : view.getLeftColorGradientsSliders()){
                slider.setDisable(false);
            }
            for(Slider slider : view.getRightColorGradientsSliders()){
                slider.setDisable(false);
            }
            uiHandler.assignCustomColors();
            updatePalette();
            uiHandler.updateColors();
        }
    }

    private void updateView(){
        view.getCurrentConfigName().setText(model.getConfigName());
        view.getTextField().setText("");
        view.getSizesComboBox().getSelectionModel().select(model.getSize().ordinal());
        if(model.getSize() != GameSettings.ScreenSizes.FULLSCREEN){
            primaryStage.setFullScreen(false);
            primaryStage.setHeight(model.getWindowHeight());
            primaryStage.setWidth(model.getWindowWidth());
            primaryStage.centerOnScreen();
        }else{
            primaryStage.setFullScreen(true);
        }
        model.updateColors();
        view.getPiecePlayerToCheckColor().setFill(Color.valueOf(model.getPlayerPieceColor().getHexFromRGB()));
        view.getPieceEnemyToCheckColor().setFill(Color.valueOf(model.getEnemyPieceColor().getHexFromRGB()));
        if(initialized){
            gameSoundPresenter.updateVolume();
        }
        initialized = true;
    }

    private void loadSelectedConfig(){
        for(ToggleButton toggleButton : view.getConfigButtons()){
            toggleButton.setSelected(false);
        }
        view.getConfigButtons()[user.getCurrentConfigNumber()].setSelected(true);
        model.setConfiguration(user.getCurrentConfig());
        model.updateMusicResources();

        initializeMusicList();
        updateMusicCheckBoxes();

        view.getRedSliderPlayerPiece().setValue(model.getPlayerPieceColor().getRed());
        view.getGreenSliderPlayerPiece().setValue(model.getPlayerPieceColor().getGreen());
        view.getBlueSliderPlayerPiece().setValue(model.getPlayerPieceColor().getBlue());

        view.getRedSliderEnemyPiece().setValue(model.getEnemyPieceColor().getRed());
        view.getGreenSliderEnemyPiece().setValue(model.getEnemyPieceColor().getGreen());
        view.getBlueSliderEnemyPiece().setValue(model.getEnemyPieceColor().getBlue());
        view.getMusicVolume().setValue(model.getMusicVolume());
        view.getSoundEffectsVolumeSlider().setValue(model.getSoundEffectsVolume());

        view.getLeftFirstColorSlider1().setValue(model.getFirstColorLeft().getRed());
        view.getLeftFirstColorSlider2().setValue(model.getFirstColorLeft().getGreen());
        view.getLeftFirstColorSlider3().setValue(model.getFirstColorLeft().getBlue());

        view.getLeftSecondColorSlider1().setValue(model.getSecondColorLeft().getRed());
        view.getLeftSecondColorSlider2().setValue(model.getSecondColorLeft().getGreen());
        view.getLeftSecondColorSlider3().setValue(model.getSecondColorLeft().getBlue());

        view.getLeftThirdColorSlider1().setValue(model.getThirdColorLeft().getRed());
        view.getLeftThirdColorSlider2().setValue(model.getThirdColorLeft().getGreen());
        view.getLeftThirdColorSlider3().setValue(model.getThirdColorLeft().getBlue());

        view.getRightFirstColorSlider1().setValue(model.getFirstColorRight().getRed());
        view.getRightFirstColorSlider2().setValue(model.getFirstColorRight().getGreen());
        view.getRightFirstColorSlider3().setValue(model.getFirstColorRight().getBlue());

        view.getRightSecondColorSlider1().setValue(model.getSecondColorRight().getRed());
        view.getRightSecondColorSlider2().setValue(model.getSecondColorRight().getGreen());
        view.getRightSecondColorSlider3().setValue(model.getSecondColorRight().getBlue());

        view.getRightThirdColorSlider1().setValue(model.getThirdColorRight().getRed());
        view.getRightThirdColorSlider2().setValue(model.getThirdColorRight().getGreen());
        view.getRightThirdColorSlider3().setValue(model.getThirdColorRight().getBlue());

        model.updateColors();
        updatePalette();
        uiHandler.updateColors();
        updateView();
    }

    private void updatePalette(){
        model.generateColor();
        reloadStyleSheets();
    }

    private void updateConfigsInDB(Pane view){
        try{
            if(user instanceof Player player){
                player.updateConfigsInDB();
            }
        }catch (PlayerConfigurationException ex) {
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting("Game interrupter", ex.getMessage()), view);
        }
    }


}
