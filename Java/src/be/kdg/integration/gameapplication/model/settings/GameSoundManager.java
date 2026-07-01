package be.kdg.integration.gameapplication.model.settings;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameSoundManager{
    private HashMap<Integer, GameSoundEntity> musicCollection = new HashMap<>();
    private HashMap<GameSoundFX, String> soundsPath = new HashMap();
    private int currentSong;
    private double musicVolume;
    private double soundEffectsVolume;

    //private static field to make it globally accessible from the getter
    //but preventing the field from changing outside the class
    private static int amountOfSongs;

    public GameSoundManager() throws SoundException{
        this.musicVolume = 0.2;
        this.soundEffectsVolume = 0.3;
        try{
            initializeMusicResources();
        }catch (SoundException e) {
            throw e;
        }
        this.soundsPath.put(GameSoundFX.CLICK_SOUND, "/audio/soundfx/clicking-sound.mp3");
        this.soundsPath.put(GameSoundFX.LOSS_SOUND, "/audio/soundfx/game-finish-loss.mp3");
        this.soundsPath.put(GameSoundFX.WIN_SOUND, "/audio/soundfx/game-finish-won.mp3");
        this.soundsPath.put(GameSoundFX.ERROR, "/audio/soundfx/ingame-error-sound.mp3");
        this.soundsPath.put(GameSoundFX.PIECE_MOVE, "/audio/soundfx/piece-move-sound.mp3");
        this.soundsPath.put(GameSoundFX.AUTH_DONE, "/audio/soundfx/you-logged-in-sound.mp3");
    }

    public static int getAmountOfSongs(){
        return amountOfSongs;
    }

/*    private void initializeMusicResources() throws SoundException{
        URL folderUrl = getClass().getResource("/audio/music/");
        Path folderAsPath;
        try{
            folderAsPath = Paths.get(folderUrl.toURI());
        }catch (URISyntaxException e) {
            throw new SoundException("Cannot resolve path to music folder");
        }
        ArrayList<File> files = new ArrayList<>(List.of(folderAsPath.toFile().listFiles()));
        if(files.isEmpty()) throw new SoundException("No files were found in provided folder");
        for(File file : files){
            if(file.getName().endsWith(".mp3")){
                try{
                    musicCollection.put(amountOfSongs, new GameSoundEntity(file.getName()));
                }catch (SoundException e) {
                    throw e;
                }
                amountOfSongs++;
            }
        }
        Random random = new Random();
        currentSong = random.nextInt(0, amountOfSongs);
    }*/

    private void initializeMusicResources() throws SoundException {

        ArrayList<File> files = new ArrayList<>();
        try {
            URL folderUrl = new File("resources/audio/music/").toURI().toURL();
            URI uri = folderUrl.toURI();
            Path folderAsPath;
            if (uri.getScheme().equals("jar")) {
                String jarPath = uri.toString().split("!")[0];
                try (java.nio.file.FileSystem fs = java.nio.file.FileSystems.newFileSystem(URI.create(jarPath), java.util.Collections.emptyMap())) {
                    folderAsPath = fs.getPath("/audio/music/");
                    java.nio.file.Files.list(folderAsPath).forEach(p -> files.add(new File(p.getFileName().toString())));
                }
            } else {
                folderAsPath = Paths.get(uri);
                files.addAll(List.of(folderAsPath.toFile().listFiles()));
            }
        } catch (Exception e) {
            throw new SoundException("Cannot resolve path to music folder");
        }
        if (files.isEmpty()) throw new SoundException("No files were found in provided folder");
        for (File file : files) {
            if (file.getName().endsWith(".mp3")) {
                try {
                    musicCollection.put(amountOfSongs, new GameSoundEntity(file.getName()));
                } catch (SoundException e) {
                    throw e;
                }
                amountOfSongs++;
            }
        }
        Random random = new Random();
        currentSong = random.nextInt(0, amountOfSongs);
    }

    public double getSoundEffectsVolume(){
        return soundEffectsVolume;
    }

    public void setSoundEffectsVolume(double sfxVolume){
        if(soundEffectsVolume >= 0.0 && soundEffectsVolume <= 1.0){
            this.soundEffectsVolume = sfxVolume;
        }else{
            this.soundEffectsVolume = 0.5;
        }
    }

    public void resetAmountOfSongs(){
        amountOfSongs = 0;
    }

    public String getSound(GameSoundFX gameSoundFX){
        return soundsPath.get(gameSoundFX);
    }

    public double getMusicVolume(){
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume){
        if(musicVolume >= 0.0 && musicVolume <= 1.0){
            this.musicVolume = musicVolume;
        }else{
            this.musicVolume = 0.5;
        }
    }

    private boolean isAnySongSelected(){
        for(GameSoundEntity entity : musicCollection.values()){
            if(entity.isAvailable()) return true;
        }
        return false;
    }

    ;

    public String nextSong() throws SoundException{
        if(musicCollection.isEmpty()) throw new SoundException("No music were found");
        if(!isAnySongSelected()) throw new SoundException("No songs are selected");
        currentSong++;
        if(currentSong >= musicCollection.size()){
            currentSong = -1;
            return nextSong();
        }
        GameSoundEntity entry = musicCollection.get(currentSong);
        ;
        return entry.isAvailable() ? entry.getPath() : nextSong();
    }

    public HashMap<Integer, GameSoundEntity> getMusicCollection(){
        return musicCollection;
    }
}
