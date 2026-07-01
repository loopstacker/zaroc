package be.kdg.integration.gameapplication.model.gamematch.match;

import be.kdg.integration.gameapplication.model.gamematch.match.ai.EnemyMode;
import be.kdg.integration.gameapplication.model.gamematch.match.board.MoveOwner;

public class CustomGameSettings {
private EnemyMode enemyMode;
private MoveOwner whoBegins;
private float timeLimit;

    public CustomGameSettings(EnemyMode enemyMode, MoveOwner whoBegins, float timeLimit) {
        this.enemyMode = enemyMode;
        this.whoBegins = whoBegins;
        this.timeLimit = timeLimit;
    }

    public EnemyMode getEnemyMode() {
        return enemyMode;
    }

    public MoveOwner getWhoBegins() {
        return whoBegins;
    }

    public float getTimeLimit() {
        return timeLimit;
    }
}
