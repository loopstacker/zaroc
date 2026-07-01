package be.kdg.integration.gameapplication.model.gamematch.match;


import be.kdg.integration.gameapplication.model.gamematch.match.ai.DirectionDeterminant;
import be.kdg.integration.gameapplication.model.gamematch.match.ai.EnemyMode;
import be.kdg.integration.gameapplication.model.gamematch.match.board.BoardModel;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Move;
import be.kdg.integration.gameapplication.model.gamematch.match.board.MoveOwner;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Position;

/**
 * Represents a custom match with configurable settings such as time limit,
 * enemy difficulty, and who takes the first move.
 *
 * @see CustomGameSettings
 */
public class CustomMatch extends Match{
    private EnemyMode enemyMode;
    private CustomGameSettings customGameSettings;


    /**
     * Constructs a new custom match using the provided game settings.
     *
     * @param customGameSettings the settings defining the match configuration.
     */
    //------------ CUSTOM GAME ------------//
    public CustomMatch(CustomGameSettings customGameSettings){
        super();
        this.customGameSettings = customGameSettings;
        super.timerModel = new TimerModel(customGameSettings.getTimeLimit());
        this.enemyMode = customGameSettings.getEnemyMode();
        super.firstMoveOwner = customGameSettings.getWhoBegins();
    }

    /**
     * Reconstructs a custom match from an existing board state, used for replaying a game.
     *
     * @param boardModel the board state containing all previously made moves
     */
    //------------ TO PLAY THE GAME FROM THE REPLAY ------------//
    public CustomMatch(BoardModel boardModel){
        super(boardModel);
        float timeOfAllPreviousMoves = 0;
        for(Move move : boardModel.getMoves()){
            timeOfAllPreviousMoves += move.getTimePerMove();
        }
        super.timerModel = new TimerModel(TimerModel.START_AMOUNT_FOR_COMPETITIVE_MATCH - timeOfAllPreviousMoves);
        this.enemyMode = EnemyMode.SMART;
        super.firstMoveOwner = boardModel.getFirstMoveOwner();
    }

    /**
     * Executes the AI's move using the configured enemy mode and applies it to the board.
     *
     * @return the move made by the AI
     */
    @Override
    public Move startAiMoveIfApplicable(){
        DirectionDeterminant dt = getEnemy().getEnemyMovements().get(enemyMode);
        dt.findTheMove();
        Position from = dt.getPosFrom();
        Position to = dt.getPosTo();
        Move move = new Move(from, to, MoveOwner.ENEMY, super.getAiThinkingDelay());
        super.getBoardModel().applyMove(move);
        return move;
    }

    public CustomGameSettings getCustomGameSettings(){
        return customGameSettings;
    }

    /**
     * Custom matches do not save progress on exit.
     */
    @Override
    public void leaveTheGame(){
        //it has to be empty
    }

    /**
     * Finalizes the match by determining the game outcome.
     */
    @Override
    public void finishTheGame(){
        super.defineGameOutcome();
    }
}
