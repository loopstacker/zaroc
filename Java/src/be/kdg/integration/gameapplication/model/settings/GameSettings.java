package be.kdg.integration.gameapplication.model.settings;

import be.kdg.integration.gameapplication.model.user.User;

import java.util.HashMap;
import java.util.Map;

public class GameSettings{
    private User user;
    private String configName;
    private final String configNameByDefault = "config";

    private ColorPaletteGenerator colorPaletteGenerator;
    private boolean isDefaultColorPaletteApplied;
    private AdjustableColor firstColorLeft;
    private AdjustableColor secondColorLeft;
    private AdjustableColor thirdColorLeft;
    private AdjustableColor firstColorRight;
    private AdjustableColor secondColorRight;
    private AdjustableColor thirdColorRight;

    private AdjustableColor enemyPieceColor;
    private AdjustableColor playerPieceColor;

    private GameSoundManager gameSoundManager;
    private double soundEffectsVolume;

    private GameSettings.ScreenSizes screenSize;

    public GameSettings(User user, GameSoundManager gameSoundManager) throws SoundException{
        this.user = user; //
        this.colorPaletteGenerator = new ColorPaletteGenerator();
        Config config = user.getCurrentConfig();
        try{
            this.gameSoundManager = gameSoundManager;
            updateMusicResources();
        }catch (SoundException e) {
            throw e;
        }

        playerPieceColor = new AdjustableColor(config.getPlayerPieceColor());
        enemyPieceColor = new AdjustableColor(config.getEnemyPieceColor());

        firstColorLeft = new AdjustableColor(config.getFirstColorLeft());
        firstColorRight = new AdjustableColor(config.getFirstColorRight());

        secondColorLeft = new AdjustableColor(config.getSecondColorLeft());
        secondColorRight = new AdjustableColor(config.getSecondColorRight());

        thirdColorLeft = new AdjustableColor(config.getThirdColorLeft());
        thirdColorRight = new AdjustableColor(config.getThirdColorRight());
        isDefaultColorPaletteApplied = false;
        setConfiguration(user.getCurrentConfig());
    }

    public void setConfiguration(Config config){
        gameSoundManager.setMusicVolume(config.getMusicVolume());
        gameSoundManager.setSoundEffectsVolume(config.getSoundEffectsVolume());
        firstColorLeft.setColorUsingHexValue(config.getFirstColorLeft());
        firstColorRight.setColorUsingHexValue(config.getFirstColorRight());
        secondColorLeft.setColorUsingHexValue(config.getSecondColorLeft());
        secondColorRight.setColorUsingHexValue(config.getSecondColorRight());
        thirdColorLeft.setColorUsingHexValue(config.getThirdColorLeft());
        thirdColorRight.setColorUsingHexValue(config.getThirdColorRight());
        playerPieceColor.setColorUsingHexValue(config.getPlayerPieceColor());
        enemyPieceColor.setColorUsingHexValue(config.getEnemyPieceColor());
        this.soundEffectsVolume = config.getSoundEffectsVolume();
        this.configName = config.getConfigName();
        this.screenSize = ScreenSizes.valueOf(config.getScreenSize());
        updateColors();
    }

    public void generateColor(){
        colorPaletteGenerator.generateColor(1, firstColorLeft.getHexFromRGB(), firstColorRight.getHexFromRGB());
        colorPaletteGenerator.generateColor(2, secondColorLeft.getHexFromRGB(), secondColorRight.getHexFromRGB());
        colorPaletteGenerator.generateColor(3, thirdColorLeft.getHexFromRGB(), thirdColorRight.getHexFromRGB());
        colorPaletteGenerator.rewrite();
    }

    public double getSoundEffectsVolume(){
        return soundEffectsVolume;
    }

    public void setSoundEffectsVolume(double sfxVolume){
        user.getCurrentConfig().setSoundEffectsVolume(sfxVolume);
        setConfiguration(user.getCurrentConfig());
    }

    public void setScreenSize(GameSettings.ScreenSizes size){
        user.getCurrentConfig().setScreenSize(size.name());
        setConfiguration(user.getCurrentConfig());
    }

    public void applyConfigNameByDefault(){
        user.getCurrentConfig().setConfigName(configNameByDefault);
        setConfiguration(user.getCurrentConfig());
    }

    public User getUser(){
        return user;
    }

    public double getMusicVolume(){
        return gameSoundManager.getMusicVolume();
    }

    public void setMusicVolume(double volume){
        user.getCurrentConfig().setMusicVolume(volume);
        setConfiguration(user.getCurrentConfig());
    }

    public String getConfigName(){
        return configName;
    }

    public void setConfigName(String configName){
        user.getCurrentConfig().setConfigName(configName);
        setConfiguration(user.getCurrentConfig());
    }

    public GameSettings.ScreenSizes getSize(){
        return screenSize;
    }

    public float getWindowWidth(){
        return screenSize.getWidth();
    }

    public float getWindowHeight(){
        return screenSize.getHeight();
    }

    public AdjustableColor getPlayerPieceColor(){
        return playerPieceColor;
    }

    public AdjustableColor getEnemyPieceColor(){
        return enemyPieceColor;
    }

    public AdjustableColor getFirstColorLeft(){
        return firstColorLeft;
    }

    public AdjustableColor getSecondColorLeft(){
        return secondColorLeft;
    }

    public AdjustableColor getThirdColorLeft(){
        return thirdColorLeft;
    }

    public AdjustableColor getThirdColorRight(){
        return thirdColorRight;
    }

    public AdjustableColor getSecondColorRight(){
        return secondColorRight;
    }

    public void resetAmountOfSongs(){
        gameSoundManager.resetAmountOfSongs();
    }

    public AdjustableColor getFirstColorRight(){
        return firstColorRight;
    }

    public boolean isDefaultColorPaletteApplied(){
        return isDefaultColorPaletteApplied;
    }

    public void setDefaultColorPaletteApplied(boolean defaultColorPaletteApplied){
        isDefaultColorPaletteApplied = defaultColorPaletteApplied;
    }

    public HashMap<Integer, GameSoundEntity> getMusicCollection(){
        return gameSoundManager.getMusicCollection();
    }

    public void updateMusicResources(){
        for(GameSoundEntity gameSoundEntity: getMusicCollection().values()){
            gameSoundEntity.setAvailable(false);
        }
        for(int i : user.getCurrentConfig().getAvailableSongs()){
            gameSoundManager.getMusicCollection().get(i).setAvailable(true);
        }
    }

    public void updateColors(){
        Config config = user.getCurrentConfig();
        config.setFirstColorLeft(firstColorLeft.getHexFromRGB());
        config.setFirstColorRight(firstColorRight.getHexFromRGB());
        config.setSecondColorLeft(secondColorLeft.getHexFromRGB());
        config.setSecondColorRight(secondColorRight.getHexFromRGB());
        config.setThirdColorLeft(thirdColorLeft.getHexFromRGB());
        config.setThirdColorRight(thirdColorRight.getHexFromRGB());
        config.setPlayerPieceColor(playerPieceColor.getHexFromRGB());
        config.setEnemyPieceColor(enemyPieceColor.getHexFromRGB());
    }

    public enum ScreenSizes{
        TINY(800F, 600F),
        SMALL(1000F, 700F),
        NORMAL(1200F, 750F),
        BIG(1600F, 900F),
        HUGE(1875F, 1000F),
        FULLSCREEN(null, null);

        private final Float width;
        private final Float height;

        ScreenSizes(Float width, Float height){
            this.width = width;
            this.height = height;
        }

        private Float getWidth(){
            return width;
        }

        private Float getHeight(){
            return height;
        }
    }

}