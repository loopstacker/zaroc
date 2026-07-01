package be.kdg.integration.gameapplication.view.allgamesscreen;

import be.kdg.integration.gameapplication.view.gamesitementity.GamesItemView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
public class GamesEntitiesView extends BorderPane {
    private ListView<GamesItemView> gamesList;
    private ImageView backButton;
    private PieChart pieChart;
    private ComboBox<String> filterGameStatusChoices;

    public GamesEntitiesView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        gamesList = new ListView<>();
        backButton = new ImageView(new Image("/icons/exit_button.png"));
        pieChart = new PieChart();
        filterGameStatusChoices = new ComboBox<>();
    }

    private void layoutNodes() {
        pieChart.setTitle("Win/Loss Ratio");
        pieChart.lookup(".chart-title").setStyle("-fx-text-fill: white;");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setStyle("-fx-font-size: 14px; -fx-text-fill: white");
        pieChart.setLabelsVisible(false);
        filterGameStatusChoices.getItems().addAll("ALL", "GOING", "WIN", "LOSS");
        filterGameStatusChoices.setValue("ALL");
        filterGameStatusChoices.setStyle("-fx-font-size: 12px; -fx-padding: 5 10;");


        Label titleLabel = new Label("GAMES");
        titleLabel.setFont(Font.font(36));
        titleLabel.setStyle("-fx-text-fill: white;");

        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(20));
        backButton.setFitWidth(60);
        backButton.setFitHeight(60);
        topBox.setTranslateX(20);
        topBox.setTranslateY(20);
        topBox.getChildren().addAll(backButton, titleLabel);

        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));

        Label filterGameStatusLabel = new Label("Filter by: ");
        filterGameStatusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.TOP_RIGHT);
        filterBox.getChildren().addAll(filterGameStatusLabel, filterGameStatusChoices);

        VBox leftSideScreen = new VBox(20);
        leftSideScreen.setAlignment(Pos.TOP_CENTER);
        leftSideScreen.setPadding(new Insets(20));
        leftSideScreen.setStyle("-fx-background-color: #3c3c3c; -fx-background-radius: 10;");
        leftSideScreen.getChildren().addAll(pieChart, filterBox);
        leftSideScreen.setPrefWidth(300);
        leftSideScreen.setMaxWidth(350);
        leftSideScreen.setMinWidth(250);

        HBox mainContent = new HBox(10);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_LEFT);

        VBox.setVgrow(gamesList, Priority.ALWAYS);
        VBox rightSideScreen = new VBox(10);
        rightSideScreen.getChildren().add(gamesList);
        VBox.setVgrow(gamesList, Priority.ALWAYS);
        HBox.setHgrow(rightSideScreen, Priority.ALWAYS);

        mainContent.getChildren().addAll(leftSideScreen, rightSideScreen);

        setTop(topBox);
        setCenter(mainContent);
        setBottom(bottomBox);
        setStyle("-fx-background-color: #2b2b2b;");
    }

    ListView<GamesItemView> getGamesList() { return gamesList; }
    ImageView getBackButton() { return backButton; }
    PieChart getPieChart() { return pieChart; }
    ComboBox<String> getFilterGameStatusChoices() { return filterGameStatusChoices; }
}