package be.kdg.integration.gameapplication.model.settings;

import be.kdg.integration.gameapplication.model.user.PlayerConfigurationException;

import java.util.ArrayList;
import java.util.Iterator;

public class Config{
    private String configName;
    private String firstColorLeft;
    private String secondColorLeft;
    private String thirdColorLeft;

    private String firstColorRight;
    private String secondColorRight;
    private String thirdColorRight;

    private String playerPieceColor;
    private String enemyPieceColor;
    private String screenSize;
    private double soundEffectsVolume;
    private double musicVolume;
    private ArrayList<Integer> availableSongs = new ArrayList<>();

    public Config(String configName, String playerPieceColor, String enemyPieceColor, double musicVolume, double soundEffectsVolume, String screenSize, String firstColorLeft, String secondColorLeft, String thirdColorLeft,
                  String firstColorRight, String secondColorRight, String thirdColorRight, String songFlags){
        this.firstColorLeft = firstColorLeft;
        this.secondColorLeft = secondColorLeft;
        this.thirdColorLeft = thirdColorLeft;
        this.firstColorRight = firstColorRight;
        this.secondColorRight = secondColorRight;
        this.thirdColorRight = thirdColorRight;
        this.configName = configName;
        this.playerPieceColor = playerPieceColor;
        this.enemyPieceColor = enemyPieceColor;
        this.soundEffectsVolume = soundEffectsVolume;
        this.musicVolume = musicVolume;
        this.screenSize = screenSize;
        defineAvailableSongs(songFlags);
    }

    public Config() throws PlayerConfigurationException{ //default one
        this.firstColorLeft = "#4A3E2A";
        this.secondColorLeft = "#E8C860";
        this.thirdColorLeft = "#FFFFFF";
        this.firstColorRight = "#120D09";
        this.secondColorRight = "#4A2E10";
        this.thirdColorRight = "#5A5040";
        this.configName = "config";
        this.playerPieceColor = "#CFB997";
        this.enemyPieceColor = "#61554b";
        this.soundEffectsVolume = 0.5;
        this.musicVolume = 0.5;
        this.screenSize = "FULLSCREEN";
        defineAvailableSongs("111111111");
    }

    public ArrayList<Integer> getAvailableSongs(){
        return availableSongs;
    }

    private void defineAvailableSongs(String bytes){
        this.availableSongs = new ArrayList<>();
        for(int i = 0; i < bytes.length() && i < GameSoundManager.getAmountOfSongs(); i++){
            if(bytes.charAt(i) == '1') availableSongs.add(i);
        }
    }

    public void turnOnSongNumber(int x){
        if(availableSongs.contains(x)) return;
        availableSongs.add(x);
    }

    public void turnOffSongNumber(int x){
        if(!availableSongs.contains(x)) return;
        Iterator<Integer> it = availableSongs.iterator();
        while(it.hasNext()){
            if(it.next().equals(x)) it.remove();
        }
    }

    public boolean isOnlyOneSongSelected(){
        return availableSongs.size() == 1;
    }

    public String getAvailableSongsFlags(){
        StringBuilder flags = new StringBuilder();
        flags.repeat("0", GameSoundManager.getAmountOfSongs());
        for(int x : availableSongs){
            flags.setCharAt(x, '1');
        }
        return flags.toString();
    }

    public String getFirstColorLeft(){
        return firstColorLeft;
    }

    public void setFirstColorLeft(String firstColorLeft){
        this.firstColorLeft = firstColorLeft;
    }

    public String getFirstColorRight(){
        return firstColorRight;
    }

    public void setFirstColorRight(String firstColorRight){
        this.firstColorRight = firstColorRight;
    }

    public String getSecondColorLeft(){
        return secondColorLeft;
    }

    public void setSecondColorLeft(String secondColorLeft){
        this.secondColorLeft = secondColorLeft;
    }

    public String getSecondColorRight(){
        return secondColorRight;
    }

    public void setSecondColorRight(String secondColorRight){
        this.secondColorRight = secondColorRight;
    }

    public String getThirdColorLeft(){
        return thirdColorLeft;
    }

    public void setThirdColorLeft(String thirdColorLeft){
        this.thirdColorLeft = thirdColorLeft;
    }

    public String getThirdColorRight(){
        return thirdColorRight;
    }

    public void setThirdColorRight(String thirdColorRight){
        this.thirdColorRight = thirdColorRight;
    }

    public String getScreenSize(){
        return screenSize;
    }

    public void setScreenSize(String screenSize){
        this.screenSize = screenSize;
    }

    public String getConfigName(){
        return configName;
    }

    public void setConfigName(String configName){
        this.configName = configName;
    }

    public String getPlayerPieceColor(){
        return playerPieceColor;
    }

    public void setPlayerPieceColor(String playerPieceColor){
        this.playerPieceColor = playerPieceColor;
    }

    public String getEnemyPieceColor(){
        return enemyPieceColor;
    }

    public void setEnemyPieceColor(String enemyPieceColor){
        this.enemyPieceColor = enemyPieceColor;
    }

    public double getMusicVolume(){
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume){
        this.musicVolume = musicVolume;
    }

    public double getSoundEffectsVolume(){
        return soundEffectsVolume;
    }

    public void setSoundEffectsVolume(double soundEffectsVolume){
        this.soundEffectsVolume = soundEffectsVolume;
    }
}
