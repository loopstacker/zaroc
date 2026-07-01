package be.kdg.integration.gameapplication.view.gamesound;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.settings.GameSoundManager;
import be.kdg.integration.gameapplication.model.settings.SoundException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.net.URISyntaxException;

public class GameSoundPresenter{
    private GameSoundView gameSoundView;
    private GameSoundManager gameSoundManager;

    public GameSoundPresenter(GameSoundView gameSoundView, GameSoundManager gameSoundManager){
        this.gameSoundView = gameSoundView;
        this.gameSoundManager = gameSoundManager;
    }

    public void initializeMusicThread(){
        Platform.runLater(() -> changeSong());
    }

    public void playSoundEffect(GameSoundFX gameSoundFX){
        String soundTOPlay = gameSoundManager.getSound(gameSoundFX);
        Platform.runLater(() -> playSoundEffect(soundTOPlay, gameSoundManager.getSoundEffectsVolume()));
    }

    private void playSoundEffect(String path, double volume){
        try{
            gameSoundView.newSoundEffectsPlayer(path);
        }catch (URISyntaxException e) {
            throw new SoundException("Sound effects could not be loaded");
        }
        gameSoundView.getSoundEffectsMediaPlayer().setVolume(volume);
        gameSoundView.getSoundEffectsMediaPlayer().setCycleCount(1);
        gameSoundView.getSoundEffectsMediaPlayer().play();
    }

    public void updateVolume(){
        if(gameSoundView.getMusicMediaPlayer() != null){
            gameSoundView.getMusicMediaPlayer().setVolume(gameSoundManager.getMusicVolume());
        }
    }

    private void changeSong(){
        try{
            playBackgroundMusic(gameSoundManager.nextSong(), gameSoundManager.getMusicVolume());
            gameSoundView.getMusicMediaPlayer().setOnEndOfMedia(this::changeSong);
            updateVolume();
        }catch (SoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait(); //that errors means that there is something very wrong
        }
    }

    private void playBackgroundMusic(String path, double volume){
        try{
            gameSoundView.newMusicPlayer(path);
        }catch (URISyntaxException e) {
            throw new SoundException("Background music could not be loaded");
        }
        gameSoundView.getMusicMediaPlayer().setVolume(volume);
        gameSoundView.getMusicMediaPlayer().setCycleCount(1);
        gameSoundView.getMusicMediaPlayer().play();
    }

    public void stopMusicAndSound(){
        if(gameSoundView.getMusicMediaPlayer() != null){
            gameSoundView.getMusicMediaPlayer().stop();
            gameSoundView.getMusicMediaPlayer().dispose();
        }
        if(gameSoundView.getSoundEffectsMediaPlayer() != null){
            gameSoundView.getSoundEffectsMediaPlayer().stop();
            gameSoundView.getSoundEffectsMediaPlayer().dispose();
        }
    }
}
