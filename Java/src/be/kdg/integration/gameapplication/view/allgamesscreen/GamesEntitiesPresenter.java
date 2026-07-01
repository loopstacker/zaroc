package be.kdg.integration.gameapplication.view.allgamesscreen;

import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.stats.AllGamesManager;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.stats.GameData;
import be.kdg.integration.gameapplication.model.stats.GameStatus;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.gamesitementity.GamesItemPresenter;
import be.kdg.integration.gameapplication.view.gamesitementity.GamesItemView;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

public class GamesEntitiesPresenter{
    private GamesEntitiesView view;
    private AllGamesManager gamesManager;
    private String currentFilter = "ALL";
    private GameSettings gameSettings;
    private AuthentificationManager authentificationManager;
    private UIHandler uiHandler;
    private GameSoundPresenter gameSoundPresenter;

    public GamesEntitiesPresenter(GamesEntitiesView view, AllGamesManager gamesManager, GameSettings gameSettings, AuthentificationManager authentificationManager, UIHandler uiHandler, GameSoundPresenter gameSoundPresenter){
        this.view = view;
        this.gamesManager = gamesManager;
        this.gameSettings = gameSettings;
        this.authentificationManager = authentificationManager;
        this.uiHandler = uiHandler;
        this.gameSoundPresenter = gameSoundPresenter;
        addEventHandlers();
        loadGames();
        updatePieChartView();
    }

    private void addEventHandlers(){
        view.getBackButton().setOnMouseClicked(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            returnToStartPage();
        });

//        view.getRefreshButton().setOnAction(e -> {
//            gamesManager.refresh();
//            loadGames();
//            filterGameList();
//        });

        view.getFilterGameStatusChoices().setOnAction(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            currentFilter = view.getFilterGameStatusChoices().getValue();
            filterGameList();
        });
    }

    private void filterGameList(){
        view.getGamesList().getItems().clear();

        for(GameData gd : gamesManager.getAllGamesMetadata().values()){
            if(gameStatusToShow(gd)){
                GamesItemView gamesItemView = new GamesItemView();
                new GamesItemPresenter(gamesItemView, gd, gamesManager, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
                view.getGamesList().getItems().add(gamesItemView);
            }
        }
    }

    private boolean gameStatusToShow(GameData gameData){
        return switch(currentFilter){
            case "GOING" -> gameData.isGameGoing();
            case "WIN" -> gameData.getGameStatus() == GameStatus.WIN;
            case "LOSS" -> gameData.getGameStatus() == GameStatus.LOSS;
            default -> true;
        };
    }

    private void returnToStartPage(){
        StartView startView = new StartView();
        new StartPresenter(startView, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
        uiHandler.getScene().setRoot(startView);
    }

    private void loadGames(){
        view.getGamesList().getItems().clear();
        for(GameData gd : gamesManager.getAllGamesMetadata().values()){
            GamesItemView itemView = new GamesItemView();
            new GamesItemPresenter(itemView, gd, gamesManager, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
            view.getGamesList().getItems().add(itemView);
        }
        filterGameList();
    }

    private void updatePieChartView(){
        int wins = 0;
        int losses = 0;

        for(GameData gd : gamesManager.getAllGamesMetadata().values()){
            if(!gd.isGameGoing()){
                if(gd.getGameStatus() == GameStatus.WIN){
                    wins++;
                }else if(gd.getGameStatus() == GameStatus.LOSS){
                    losses++;
                }
            }
        }

        view.getPieChart().setData(FXCollections.observableArrayList(
                new PieChart.Data("Wins", wins),
                new PieChart.Data("Losses", losses)
        ));

        Platform.runLater(() -> {
            PieChart.Data winsData = view.getPieChart().getData().get(0);
            PieChart.Data lossesData = view.getPieChart().getData().get(1);

            winsData.getNode().setStyle("-fx-pie-color: #008000;");
            lossesData.getNode().setStyle("-fx-pie-color: #ff0000;");

            Node winsLegend = view.getPieChart().lookup(".data0.chart-legend-item-symbol");
            Node lossesLegend = view.getPieChart().lookup(".data1.chart-legend-item-symbol");

            if(winsLegend != null){
                winsLegend.setStyle("-fx-background-color: #008000;");
            }
            if(lossesLegend != null){
                lossesLegend.setStyle("-fx-background-color: #ff0000;");
            }
        });
    }

}