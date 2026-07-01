package be.kdg.integration.gameapplication.view.customalert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class CustomAlert extends BorderPane{
    private Label description;
    private Label title;
    private BorderPane errorLayout;

    public CustomAlert(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        this.description = new Label("Unknown");
        this.title = new Label("Unknown");
        errorLayout = new BorderPane();
    }

    private void layoutNodes(){
        setBackground(null);
        errorLayout.setMinSize(250, 170);
        errorLayout.setMaxSize(250, 170);

        errorLayout.setTop(title);
        errorLayout.setCenter(description);
        errorLayout.setPadding(new Insets(20));
        description.setTextFill(Paint.valueOf("#e8d5b0"));
        description.setWrapText(true);
        setTop(errorLayout);
        BorderPane.setAlignment(errorLayout, Pos.TOP_CENTER);
        BorderPane.setAlignment(description, Pos.CENTER);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //to make screen blocked while alert is shown. Can be removed
    }

    Label getDescription(){
        return description;
    }

    BorderPane getErrorLayout(){
        return errorLayout;
    }

    Label getTitle(){
        return title;
    }
}
