package be.kdg.integration.gameapplication.model.user;

import be.kdg.integration.gameapplication.model.databaseaccess.ConfigsDatabase;

import java.sql.SQLException;

public class Player extends User{
    private int playerID;
    private String hashedPassword;
    private String email;
    private String salt;
    protected int points;
    private ConfigsDatabase configsDataBase;

    @Override
    protected void initializeConfigs() throws PlayerConfigurationException{
        try{
            super.getConfigs().addAll(configsDataBase.getConfigs(playerID));
        }catch (SQLException e){
            throw new PlayerConfigurationException(e.getMessage());
        }
    }

    public Player(String email, String hashedPassword, String salt, int id, String nickname, int points, ConfigsDatabase configsDataBase) throws PlayerConfigurationException{
        super(nickname);
        this.points = points;
        this.email = email;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.playerID = id;
        this.configsDataBase = configsDataBase;
        try{
            initializeConfigs();
        }catch (PlayerConfigurationException e) {
            throw e;
        }
    }

    public String getEmail(){
        return email;
    }

    public String getHashedPassword(){
        return hashedPassword;
    }

    public int getPlayerID(){
        return playerID;
    }

    public String getSalt(){
        return salt;
    }

    public int getPoints(){
        return points;
    }

    public void updateConfigsInDB() throws PlayerConfigurationException{
        try{
            configsDataBase.updateConfigs(playerID, super.getConfigs());
        }catch (SQLException e) {
            throw new PlayerConfigurationException(e.getMessage());
        }
    }
}
