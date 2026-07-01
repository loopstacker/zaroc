package be.kdg.integration.gameapplication.model.user;

import be.kdg.integration.gameapplication.model.settings.*;
import java.util.ArrayList;
import java.util.List;

public abstract class User{
    private List<Config> configs = new ArrayList<>();
    private int currentConfig = 0;
    private String nickname;

    public User(String nickname){
        this.nickname = nickname;
    }

    public void setNickname(String nickname) throws PlayerConfigurationException{
        if (nickname.isEmpty()) throw new PlayerConfigurationException("Nickname cannot be empty");
        if (nickname.length() > 20) throw new PlayerConfigurationException("Nickname cannot be longer then 20 characters long");
        this.nickname = nickname;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    protected abstract void initializeConfigs();

    public void setCurrentConfigNumber(int currentConfig) {
        this.currentConfig = currentConfig;
    }

    public int getCurrentConfigNumber(){
        return currentConfig;
    }

    public Config getCurrentConfig() {
        return configs.get(currentConfig);
    }
    public String getNickname() {
        return nickname;
    }
}
