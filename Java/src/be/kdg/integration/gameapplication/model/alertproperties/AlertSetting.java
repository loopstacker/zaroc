package be.kdg.integration.gameapplication.model.alertproperties;

public class AlertSetting{
    private final float DEFAULT_DURATION = 3;
    private float duration;
    private String title;
    private String message;
    private boolean isScreenBlocked;

    public AlertSetting(String title, String message){
        if(message.isEmpty()) message = "Unknown";
        this.message = message;
        this.title = title;
        this.duration = DEFAULT_DURATION;
        this.isScreenBlocked = false;
    }

    public AlertSetting(float duration, String message){
        if(duration < 0) duration = DEFAULT_DURATION;
        if(message.isEmpty()) message = "Unknown";
        this.duration = duration;
        this.message = message;
        this.title = ""; //will be set by presenter according to alert type
        this.isScreenBlocked = false;
    }

    public AlertSetting(float duration, String title, String message){
        if(duration < 0) duration = DEFAULT_DURATION;
        if(message.isEmpty()) message = "Unknown";
        this.duration = duration;
        this.message = message;
        this.title = title;
        this.isScreenBlocked = false;
    }

    public AlertSetting(String title, String message, boolean isScreenBlocked){
        if(message.isEmpty()) message = "Unknown";
        this.message = message;
        this.title = title;
        this.duration = DEFAULT_DURATION;
        this.isScreenBlocked = isScreenBlocked;
    }

    public AlertSetting(float duration, String message, boolean isScreenBlocked){
        if(duration < 0) duration = DEFAULT_DURATION;
        if(message.isEmpty()) message = "Unknown";
        this.duration = duration;
        this.message = message;
        this.title = ""; //will be set by presenter according to alert type
        this.isScreenBlocked = isScreenBlocked;
    }

    public AlertSetting(float duration, String title, String message, boolean isScreenBlocked){
        if(duration < 0) duration = DEFAULT_DURATION;
        if(message.isEmpty()) message = "Unknown";
        this.duration = duration;
        this.message = message;
        this.title = title;
        this.isScreenBlocked = isScreenBlocked;
    }

    public AlertSetting(String message){
        this.message = message;
        this.duration = DEFAULT_DURATION; //default
        this.title = ""; //will be set by presenter according to alert type
    }

    public float getDuration(){
        return duration;
    }

    public String getMessage(){
        return message;
    }

    public String getTitle(){
        return title;
    }

    public boolean isScreenBlocked(){
        return isScreenBlocked;
    }
}
