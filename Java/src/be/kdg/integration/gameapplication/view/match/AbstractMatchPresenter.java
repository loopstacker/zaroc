package be.kdg.integration.gameapplication.view.match;

import be.kdg.integration.gameapplication.model.gamematch.match.board.*;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.view.UIHandler;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMatchPresenter {
    protected GameSettings gameSettings;
    protected Parent previousWindow;
    protected BoardModel boardModel;
    protected AbstractMatchView view;
    protected HashMap<Circle, Position> circlesField = new HashMap<>();
    protected UIHandler uiHandler;

    public AbstractMatchPresenter(AbstractMatchView view, UIHandler uiHandler){
        this.view = view;
        this.uiHandler = uiHandler;
    }

    protected void initializeCells(){
        ObservableList<Circle> circles = view.getBoardLayout().getCircles();
        int x = 0;
        int y = 1;

        for(int i = 0; i < 10; i++){
            Circle cir = circles.get(i);
            x++;
            circlesField.put(cir, new Position(x, y));
            if(x == BoardLayout.UPPER_HALF_FIELD_WIDTH){
                x = 0;
                y++;
            }
        }
        for(int i = 10; i < 18; i++){
            Circle cir = circles.get(i);
            x++;
            circlesField.put(cir, new Position(x, y));
            if(x == BoardLayout.LOWER_HALF_FIELD_WIDTH){
                x = 0;
                y++;
            }
        }
    }

    protected void updateMoveHistoryView(){
        view.getMovesHistoryContent().getChildren().clear();
        for(Move move : boardModel.getMoves()){
            int xFrom = move.getPositionFrom().getX();
            int yFrom = move.getPositionFrom().getY();
            int xTo = move.getPositionTo().getX();
            int yTo = move.getPositionTo().getY();
            Label label = new Label(String.format("-= %d : %d => %d : %d =-", xFrom, yFrom, xTo, yTo));
            label.getStyleClass().add("label-move-history");
            view.getMovesHistoryContent().getChildren().addAll(label);
        }
    }

    protected void applyTurnChanges(){
        if(boardModel.getPlayerIndicatorStatus()){
            switchTheLightToPlayer();
            return;
        }
        switchTheLightToEnemy();
    }

    protected void updatePieceView(){
        clearCells();
        for(Map.Entry<Position, PegStack> entry : boardModel.getGameBoard().entrySet()){
            PegStack peg = entry.getValue();
            Position pos = entry.getKey();
            if(!peg.isEmpty()){
                for(int z = peg.size() - 1; z != -1; z--){
                    Piece piece = peg.getStack().get(z);
                    if(piece != null){
                        boolean isPlayer = piece.getPieceOwner() == PieceOwner.PLAYER;
                        addPiece(isPlayer, pos.getX() - 1, pos.getY() - 1, z);
                    }
                }
            }
        }
    }

    protected Circle findCircle(Position position){
        for(Map.Entry<Circle, Position> entry : circlesField.entrySet()){
            if(entry.getValue().equals(position)){
                return entry.getKey();
            }
        }
        return null;
    }

    //this method is called each time players replace the piece
    private void addPiece(boolean isPlayer, int xPeg, int yPeg, int zOfPiece){

        Color color = isPlayer ? Color.valueOf(gameSettings.getPlayerPieceColor().getHexFromRGB()) : Color.valueOf(gameSettings.getEnemyPieceColor().getHexFromRGB());
        VBox peg = new VBox();
        peg.setSpacing(0);
        peg.prefWidthProperty().bind(view.getBoardLayout().getCircles().getFirst().radiusProperty());

        //creating new pieces, since we clear them after each move
        view.getBoardLayout().getPieces().add(new Rectangle());
        Rectangle piece = view.getBoardLayout().getPieces().getLast();
        piece.widthProperty().bind(view.getBoardLayout().getCircles().getFirst().radiusProperty().multiply(0.8));
        piece.heightProperty().bind(view.getBoardLayout().getCircles().getFirst().radiusProperty().multiply(0.4));
        piece.setArcHeight(20);
        piece.setArcWidth(10);

        piece.setStrokeWidth(2);
        piece.setStroke(Color.BLACK);

        piece.setFill(color);

        while(peg.getChildren().size() <= zOfPiece){ //zOfPiece is the position of piece within the peg
            Region empty = new Region();
            empty.setBackground(null);
            empty.setMouseTransparent(true);
            empty.setVisible(false);
            empty.prefHeightProperty().bind(piece.heightProperty());
            empty.prefWidthProperty().bind(piece.widthProperty());
            VBox.setVgrow(empty, Priority.NEVER);
            peg.getChildren().add(empty);  //we are settings empty blocks with the same size as piece has
            //afterward we will replace the highest one with actual piece
        }

        peg.getChildren().set(zOfPiece, piece); //replacing. Notice that the objects inside the VBox goes from top to bottom
        peg.setAlignment(Pos.BOTTOM_CENTER);

        //No we reverse all objects inside the VBox because otherwise all pieces will be set upside down
        ObservableList<Node> original = FXCollections.observableArrayList(peg.getChildren());
        Collections.reverse(original);
        peg.getChildren().setAll(original);

        GridPane.setHalignment(peg, HPos.CENTER);
        GridPane.setValignment(peg, VPos.CENTER);


        SimpleDoubleProperty perspectiveFactor = new SimpleDoubleProperty(0.35F);
        DoubleBinding basePos = view.getBoardLayout().getCircles().getFirst().radiusProperty().divide(2);  //that how many pixels we are going to shift VBox with pegs to align it
        DoubleProperty circleRadius = view.getBoardLayout().getCircles().getFirst().radiusProperty();
        ObservableDoubleValue depthShift = circleRadius.multiply(perspectiveFactor);

        peg.translateYProperty().bind(
                (basePos.multiply(-1).subtract(depthShift))
        );
        if(yPeg < 2){
            GridPane.setRowIndex(peg, yPeg);
            GridPane.setColumnIndex(peg, xPeg);
            view.getBoardLayout().getUpperPiecesGrid().getChildren().add(peg);
            return;
        }
        GridPane.setRowIndex(peg, yPeg - 2);
        GridPane.setColumnIndex(peg, xPeg);
        view.getBoardLayout().getLowerPiecesGrid().add(peg, xPeg, yPeg - 2);

    }

    protected void clearHover(ArrayList<Circle> invalidCircles){
        for(Circle circle : view.getBoardLayout().getCircles()){
            if(!invalidCircles.contains(circle)) circle.setFill(BoardLayout.DEFAULT_CIRCLE_COLOR);
        }
    }

    protected void clearSelect(){
        for(Circle circle : view.getBoardLayout().getCircles()){
            circle.setStrokeWidth(0);
        }
    }

    private void switchTheLightToEnemy(){
        view.getEnemyIndicator().setFill(Color.GREEN);
        view.getPlayerIndicator().setFill(Color.RED);
    }

    private void switchTheLightToPlayer(){
        view.getEnemyIndicator().setFill(Color.RED);
        view.getPlayerIndicator().setFill(Color.GREEN);
    }

    private void clearCells(){
        view.getBoardLayout().getLowerPiecesGrid().getChildren().clear();
        view.getBoardLayout().getUpperPiecesGrid().getChildren().clear();
    }

    protected abstract void returnToMainMenu();

    public void setPreviousWindow(Parent parent){
        this.previousWindow = parent;
    }
}
