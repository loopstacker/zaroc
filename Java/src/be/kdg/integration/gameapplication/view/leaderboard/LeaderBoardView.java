package be.kdg.integration.gameapplication.view.leaderboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
public class LeaderBoardView extends BorderPane{
    private TableView<String[]> leaderboardTable;
    private ImageView goBackButton;

    private final String[] HEADERS = {"#", "RANK", "NAME", "SCORE", "MINUTES PLAYED", "CAREER", "WIN RATE", "AVG(mvs)/game", "AVG(d)/move"};
    private final double[] WIDTHS = {20, 20, 150, 80, 100, 80, 60, 60, 60};

    public LeaderBoardView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        leaderboardTable = new TableView<>();
        goBackButton = new ImageView(new Image("/icons/exit_button.png"));
    }

    private void layoutNodes() {
        leaderboardTable.getStyleClass().add("leaderboard-table");
        Label leaderBoardLabel = new Label("LEADERBOARD");
        leaderBoardLabel.getStyleClass().add("leaderboard-title");

        Region leftBalance = new Region();
        Region rightBalance = new Region();
        rightBalance.setMinWidth(40);
        leftBalance.setMinWidth(40);
        for (int i = 0; i < HEADERS.length; i++) {
            final int col = i;
            TableColumn<String[], String> column = new TableColumn<>(HEADERS[i]);
            column.setPrefWidth(WIDTHS[i]);
            column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[col]));
            column.setReorderable(false);

            // only for career column index 5
            if (i == 5) {
                column.setCellFactory(tc -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            return;
                        }
                        // parse wins losses and tg
                        String[] parts = item.split("/");
                        Label wins = new Label(parts[0].trim());
                        Label sep1 = new Label(" / ");
                        Label losses = new Label(parts[1].trim());
                        Label sep2 = new Label(" / ");
                        Label total = new Label(parts[2].trim());

                        wins.setStyle("-fx-text-fill: #7ec8a0;");
                        losses.setStyle("-fx-text-fill: #e07070;");
                        total.setStyle("-fx-text-fill: #c0a070;");
                        sep1.setStyle("-fx-text-fill: rgba(212,168,83,0.3);");
                        sep2.setStyle("-fx-text-fill: rgba(212,168,83,0.3);");

                        HBox hBox = new HBox(wins, sep1, losses, sep2, total);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        setGraphic(hBox);
                        setText(null);
                    }
                });
            }

            if (i == 0 || i == 3 || i == 4 || i == 6 || i == 7 || i == 8) {
                column.setComparator((a, b) ->
                        Double.compare(Double.parseDouble(a), Double.parseDouble(b)));
            }
            leaderboardTable.getColumns().add(column);
        }
        leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(10, leaderBoardLabel, leaderboardTable);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(50);

        goBackButton.setFitWidth(60);
        goBackButton.setFitHeight(60);
        HBox hBox = new HBox(goBackButton);

        setAlignment(goBackButton, Pos.TOP_LEFT);
        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);
        borderPane.setLeft(leftBalance);
        borderPane.setRight(rightBalance);
        setCenter(borderPane);


        leaderBoardLabel.setAlignment(Pos.BASELINE_CENTER);
        this.setPadding(new Insets(25));

        double rowHeight = 40;
        double headerHeight = 300;
        int visibleRows = 5;

        leaderboardTable.setFixedCellSize(rowHeight);
        leaderboardTable.setPrefHeight(rowHeight * visibleRows + headerHeight);
        leaderboardTable.setMaxHeight(rowHeight * visibleRows + headerHeight);
    }

    TableView<String[]> getLeaderboardTable(){
        return leaderboardTable;
    }

    ImageView getReturn(){
        return goBackButton;
    }
}