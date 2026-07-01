package be.kdg.integration.gameapplication.model.user;

import be.kdg.integration.gameapplication.model.settings.Config;

public class Guest extends User{
    public Guest(String guestNickname){
        super(guestNickname);
        try{
            initializeConfigs();
        }catch (PlayerConfigurationException e) {
            throw e;
        }
    }

    @Override
    protected void initializeConfigs(){
        for(int i = 0; i < 5; i++){
            super.getConfigs().add(new Config());
        }
    }
}
