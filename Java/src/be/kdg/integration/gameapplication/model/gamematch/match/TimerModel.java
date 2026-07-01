package be.kdg.integration.gameapplication.model.gamematch.match;

public class TimerModel {
    public static final long START_AMOUNT_FOR_COMPETITIVE_MATCH = 300;

    private final float timeLimit;
    private boolean isRunning = true;
    private float secondsLeft;

    public TimerModel(float timeLimit){
        this.timeLimit = timeLimit;
        this.secondsLeft = timeLimit;
    }

    public void decreaseEachSecond(){
        if (isRunning){
            secondsLeft--;
        }
    }

    public float getTimeLimit(){
        return timeLimit;
    }

    public float getSecondsLeft(){
        return secondsLeft;
    }

    public boolean isFinished(){
        return secondsLeft <= 0;
    }

    public String getFormattedTime() {
        long minutes = (long) secondsLeft/60;
        long seconds = (long) secondsLeft%60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void switchOnOff(){
        this.isRunning = !isRunning;
    }
}
