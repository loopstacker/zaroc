package be.kdg.integration.gameapplication.view.match;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public abstract class AbstractMatchView extends BorderPane {
    protected Circle playerIndicator;
    protected Circle enemyIndicator;

    protected BoardLayout boardLayout;

    protected ScrollPane movesHistoryScroll;
    protected VBox movesHistoryContent;

    protected Button exitButton;
    protected Label timer;

    protected BoardLayout getBoardLayout(){
        return boardLayout;
    }

    protected VBox getMovesHistoryContent(){
        return movesHistoryContent;
    }

    protected Button getExitButton(){
        return exitButton;
    }

    protected Label getTimer(){
        return timer;
    }

    protected Circle getEnemyIndicator(){
        return enemyIndicator;
    }

    protected Circle getPlayerIndicator(){
        return playerIndicator;
    }
}
