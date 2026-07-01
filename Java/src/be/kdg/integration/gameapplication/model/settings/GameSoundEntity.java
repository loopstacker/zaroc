package be.kdg.integration.gameapplication.model.settings;

import java.util.ArrayList;
import java.util.List;

public class GameSoundEntity{
    private String path;
    private String name;
    private boolean isAvailable;

    public GameSoundEntity(String name) throws SoundException{
        if(name.isEmpty()) throw new SoundException("Wrong file name for music provided");
        this.path = "/audio/music/" + name;
        this.name = createANiceName(name);
        this.isAvailable = true;
    }

    private String createANiceName(String path){
        StringBuilder clearedFileName = new StringBuilder(path);
        ArrayList<Character> charsToReplace = new ArrayList<>(
                List.of('-','/', '\\', '.', ',', '_'));

        for (int i = 0; i < clearedFileName.length(); i++) {
            if (charsToReplace.contains(clearedFileName.charAt(i))) {
                clearedFileName.setCharAt(i, ' ');
            }
        }

        StringBuilder result = new StringBuilder();
        final String format = "mp3";
        String[] words = clearedFileName.toString().split(" ");

        for(String string : words){
            if(string.isEmpty() || string.contains(format)) continue;
            clearedFileName.setLength(0);
            clearedFileName.append(string);
            clearedFileName.replace(0,1, (clearedFileName.charAt(0) + "").toUpperCase());
            result.append(clearedFileName).append(" ");
        }

        return result.toString();
    }

    public boolean isAvailable(){
        return isAvailable;
    }

    public String getName(){
        return name;
    }

    public String getPath(){
        return path;
    }

    public void setAvailable(boolean available){
        isAvailable = available;
    }
}
