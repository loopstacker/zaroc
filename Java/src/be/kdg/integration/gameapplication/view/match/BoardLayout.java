package be.kdg.integration.gameapplication.view.match;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
public class BoardLayout extends VBox{
    public static final int UPPER_HALF_FIELD_WIDTH = 5;
    public static final int LOWER_HALF_FIELD_WIDTH = 4;

    private static final int HALF_FIELD_HEIGHT = 2;
    private static final int CIRCLE_RADIUS = 50;
    private final int HALF_BOARD_PIXEL_HEIGHT = 120 * HALF_FIELD_HEIGHT; //480
    private static final int UPPER_BOARD_PIXEL_WIDTH = 120 * UPPER_HALF_FIELD_WIDTH; //600
    private static final int PIECE_SIZE_HEIGHT = 25;

    static final Color DEFAULT_PEG_COLOR = Color.rgb(213, 204, 171);

    public static final Color DEFAULT_CIRCLE_COLOR = Color.rgb(99, 32, 36);
    public static final Color CIRCLE_AVAILABLE_FOR_MOVE = Color.rgb(139, 86, 51);
    public static final Color ON_HOVER = Color.rgb(163, 123, 71);
    public static final Color SELECTED_CIRCLE = Color.rgb(150, 90, 90);

    private ObservableList<Circle> circles = FXCollections.observableArrayList();
    private ObservableList<Rectangle> pieces = FXCollections.observableArrayList();

    private GridPane upperPiecesGrid;
    private GridPane lowerPiecesGrid;

    public BoardLayout(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        ObservableList<Rectangle> pegs = FXCollections.observableArrayList();
        this.upperPiecesGrid = new GridPane();
        this.lowerPiecesGrid = new GridPane();
        for(int i = 0; i < 18; i++){
            circles.add(new Circle(CIRCLE_RADIUS));
            pegs.add(new Rectangle());
        }

    }


    private void applyGridSettingsModel(GridPane grid, boolean showLines){
        grid.setGridLinesVisible(showLines);
        grid.setHgap(4);
        grid.setVgap(4);
    }

    private ObservableDoubleValue getSmallestSideOfStackToBind(StackPane stackPane){//we need it to make all our objects inside stack pane use the same resize behaviour
        if(stackPane.heightProperty().get() > stackPane.widthProperty().get()){
            return stackPane.heightProperty().multiply(0.4);
        }else{
            return stackPane.widthProperty().multiply(0.4);
        }
    }

    private StackPane layoutCircleInCell(Circle circle, Rectangle peg, double pegHeight){

        StackPane stack = new StackPane();
        stack.setAlignment(Pos.CENTER);
        stack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        circle.setFill(DEFAULT_CIRCLE_COLOR);
        circle.radiusProperty().bind(getSmallestSideOfStackToBind(stack));

        peg.setFill(DEFAULT_PEG_COLOR);
        peg.setArcWidth(20);
        peg.setArcHeight(15);
        peg.setMouseTransparent(true);

        peg.widthProperty().bind(circle.radiusProperty().multiply(0.5));
        peg.heightProperty().bind(circle.radiusProperty().multiply(pegHeight / 60));

        DoubleProperty circleRadius = circle.radiusProperty();
        DoubleBinding basePos = circleRadius.divide(2);

//we need those lines to make our pegs use the same perspective and resize behaviour
        float x = pegHeight <= 5 + PIECE_SIZE_HEIGHT ? 0.5F : 0;
        x = pegHeight <= 5 + PIECE_SIZE_HEIGHT * 2 ? 0.20F : x;

        SimpleDoubleProperty perspectiveFactor = new SimpleDoubleProperty(0.001F - x);
        ObservableDoubleValue depthShift = circleRadius.multiply(perspectiveFactor);
        peg.translateYProperty().bind(basePos.multiply(-1).subtract(depthShift));

        stack.getChildren().addAll(circle, peg);
        return stack;

    }

    private void layoutNodes(){
        GridPane upperPaneCirclesAndPegs = new GridPane();
        GridPane lowerPaneCircleAndPegs = new GridPane();
        ObservableList<Rectangle> pegs = FXCollections.observableArrayList();
        for(int i = 0; i < 18; i++){
            pegs.add(new Rectangle());
        }

        setPrefSize(UPPER_BOARD_PIXEL_WIDTH, HALF_BOARD_PIXEL_HEIGHT * 2);

        upperPaneCirclesAndPegs.prefHeightProperty().bind(prefHeightProperty().multiply(0.5));
        lowerPaneCircleAndPegs.prefHeightProperty().bind(prefHeightProperty().multiply(0.5));
        lowerPaneCircleAndPegs.prefWidthProperty().bind(upperPaneCirclesAndPegs.widthProperty().multiply((double) LOWER_HALF_FIELD_WIDTH / UPPER_HALF_FIELD_WIDTH));
        lowerPiecesGrid.prefWidthProperty().bind(upperPiecesGrid.widthProperty().multiply((double) LOWER_HALF_FIELD_WIDTH / UPPER_HALF_FIELD_WIDTH));
        lowerPiecesGrid.prefHeightProperty().bind(prefHeightProperty().multiply(0.5));
        applyGridSettingsModel(upperPaneCirclesAndPegs, false);
        applyGridSettingsModel(lowerPaneCircleAndPegs, false);
        applyGridSettingsModel(lowerPiecesGrid, true);  //true or false is used to show or hide grid lines for debugging
        applyGridSettingsModel(upperPiecesGrid, true);

        int x = 0;
        int y = 0;
        for(int i = 0; i < 10; i++){
            double pegHeight = 5 + PIECE_SIZE_HEIGHT * (i < 5 ? 1 : 2);   //we check on which row are we, to calculate suitable peg height
            StackPane circleWithPegOnTop = layoutCircleInCell(circles.get(i), pegs.get(i), pegHeight);
            circleWithPegOnTop.prefHeightProperty().bind(upperPaneCirclesAndPegs.widthProperty().multiply(1));
            upperPaneCirclesAndPegs.add(circleWithPegOnTop, x, y);
            x++;
            if(x == 5){
                x = 0;
                y++;
            }
        }

        x = 0;
        y = 0;
        for(int i = 10; i < 18; i++){
            double pegHeight = 5 + PIECE_SIZE_HEIGHT * (i < 14 ? 3 : 4); //we check on which row are we, to calculate suitable peg height
            StackPane circleWithPegOnTop = layoutCircleInCell(circles.get(i), pegs.get(i), pegHeight);
            lowerPaneCircleAndPegs.add(circleWithPegOnTop, x, y);
            x++;
            if(x == LOWER_HALF_FIELD_WIDTH){
                x = 0;
                y++;
            }
        }

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS); //all elements inside would extend to fill the cell
        columnConstraints.prefWidthProperty().bind(circles.getFirst().radiusProperty().multiply(1.2));

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS); //all elements inside would extend to fill the cell
        rowConstraints.prefHeightProperty().bind(circles.getFirst().radiusProperty().multiply(1.2));

        for(int i = 0; i < HALF_FIELD_HEIGHT; i++){
            upperPaneCirclesAndPegs.getRowConstraints().add(rowConstraints);
            lowerPaneCircleAndPegs.getRowConstraints().add(rowConstraints);
            upperPiecesGrid.getRowConstraints().add(rowConstraints);
            lowerPiecesGrid.getRowConstraints().add(rowConstraints);
        }

        for(int i = 0; i < UPPER_HALF_FIELD_WIDTH; i++){
            upperPaneCirclesAndPegs.getColumnConstraints().add(columnConstraints);
            upperPiecesGrid.getColumnConstraints().add(columnConstraints);
        }

        for(int i = 0; i < LOWER_HALF_FIELD_WIDTH; i++){
            lowerPaneCircleAndPegs.getColumnConstraints().add(columnConstraints);
            lowerPiecesGrid.getColumnConstraints().add(columnConstraints);
        }

        StackPane upperStackCirclesPegsPieces = new StackPane(); //gathering and settings pegs on top of circles
        upperStackCirclesPegsPieces.getChildren().addAll(upperPaneCirclesAndPegs, upperPiecesGrid);

        StackPane lowerStackCirclesPegsPieces = new StackPane(); //gathering and settings pegs on top of circles
        lowerStackCirclesPegsPieces.getChildren().addAll(lowerPaneCircleAndPegs, lowerPiecesGrid);

        upperStackCirclesPegsPieces.prefHeightProperty().bind(heightProperty().multiply(0.5));
        upperStackCirclesPegsPieces.prefWidthProperty().bind(widthProperty());
        lowerStackCirclesPegsPieces.prefHeightProperty().bind(heightProperty().multiply(0.5));

        //setting the width property lower pane to (upperPaneWidth * 5 / 4)
        lowerStackCirclesPegsPieces.prefWidthProperty().bind(upperPaneCirclesAndPegs.widthProperty().multiply((double) LOWER_HALF_FIELD_WIDTH / UPPER_HALF_FIELD_WIDTH));
        lowerStackCirclesPegsPieces.setMaxWidth(Region.USE_PREF_SIZE);


        StackPane.setAlignment(upperPaneCirclesAndPegs, Pos.CENTER);
        StackPane.setAlignment(lowerPaneCircleAndPegs, Pos.CENTER);
        StackPane.setAlignment(lowerPiecesGrid, Pos.CENTER);
        StackPane.setAlignment(upperPiecesGrid, Pos.CENTER);


        lowerPiecesGrid.setMouseTransparent(true);
        upperPiecesGrid.setMouseTransparent(true);
        setAlignment(Pos.CENTER);
        getChildren().addAll(upperStackCirclesPegsPieces, lowerStackCirclesPegsPieces);


        setBackground(new Background(new BackgroundFill(
                null, new CornerRadii(20), Insets.EMPTY
        )));
        getStyleClass().add("board-color");
    }

    ObservableList<Rectangle> getPieces(){
        return pieces;
    }

    ObservableList<Circle> getCircles(){
        return circles;
    }

    GridPane getLowerPiecesGrid(){
        return lowerPiecesGrid;
    }

    GridPane getUpperPiecesGrid(){
        return upperPiecesGrid;
    }
}