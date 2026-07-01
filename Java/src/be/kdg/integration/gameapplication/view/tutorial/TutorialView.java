package be.kdg.integration.gameapplication.view.tutorial;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;

public class TutorialView extends BorderPane{
    private Button button;

    public TutorialView(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        button = new Button("UNDERSTOOD");
    }

    private void layoutNodes(){
        Label label = new Label("TUTORIAL");
        TextArea textArea = new TextArea();

        // spacing
        Region right = new Region();
        right.setPrefSize(15, 15);
        Region left = new Region();
        left.setPrefSize(15, 15);

        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        HBox.setHgrow(textArea, Priority.ALWAYS);

        textArea.setText("There are 5 short, 4 medium and 4 long pegs, plus 16 playing pieces (8 dark, 8 light).\n" +
                "\n" +
                "In Casual, you can choose your color.\n" +
                "The goal is to have 3 pieces of your color in the top row.\n" +
                "\n" +
                "A TURN CONSISTS OF TWO MOVES.\n" +
                "The pieces moved on a turn may be of either or both colors.\n" +
                "You start by making two moves in succession, followed by the robot making two moves, and so on in turn.\n" +
                "Each move of a turn consists of relocating the uppermost playing piece on a peg, either A) sideways, or B) in certain cases, forward\n" +
                "\n" +
                "A) SIDEWAYS to an adjacent peg of the same height if there is space to thread it.\n" +
                "OR\n" +
                "B) FORWARD to a lower peg or indentation, along the directions indicated by the graphics on the board. -BUT ONLY IF, ON ITS CURRENT PEG, THE PIECE IS SITTING AT THE HIGHEST POSSIBLE POSITION (i.e. there is no room on the peg for another piece to be threaded over it.)\n" +
                "\n" +
                "Once a playing piece reaches the final row it may not move again.\n" +
                "Indentation to indentation movement is not allowed.\n" +
                "Backward movements (to higher pegs) are not allowed.");

        label.getStyleClass().add("tutorial-title");
        button.setPrefWidth(200);
        button.setPrefHeight(40);

        setPadding(new Insets(50));
        setTop(label);
        setLeft(left);
        setRight(right);
        setCenter(textArea);

        HBox bottom = new HBox(button);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(15));
        setBottom(bottom);
        setAlignment(label, Pos.TOP_CENTER);
    }

    Button getButton() {
        return button;
    }
}