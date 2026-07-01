package be.kdg.integration.gameapplication.view.gamesound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

public class GameSoundView{
    private MediaPlayer musicMediaPlayer;
    private MediaPlayer soundEffectsMediaPlayer;

    public GameSoundView(){

    }

    MediaPlayer getSoundEffectsMediaPlayer(){
        return soundEffectsMediaPlayer;
    }

    MediaPlayer getMusicMediaPlayer(){
        return musicMediaPlayer;
    }

    void newMusicPlayer(String path) throws URISyntaxException {
        this.musicMediaPlayer = new MediaPlayer(
                new Media(
                        getClass().getResource(path).toExternalForm()
                ));
        //there is no way to change music without creating a new MediaPlayer object
    }

    void newSoundEffectsPlayer(String path) throws URISyntaxException {
        this.soundEffectsMediaPlayer = new MediaPlayer(
                new Media(
                        getClass().getResource(path).toExternalForm()
                ));
    }
}
