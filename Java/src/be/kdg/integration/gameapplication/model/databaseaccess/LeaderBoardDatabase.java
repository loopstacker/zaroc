package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.leaderboard.LeaderBoardEntry;

import java.sql.*;
import java.util.ArrayList;

public class LeaderBoardDatabase {
    private Connection connection;

    public LeaderBoardDatabase(DataBaseConnection dataBaseConnection) {
        this.connection = dataBaseConnection.getConnection();
    }

    public ArrayList<LeaderBoardEntry> getLeaderBoardEntries() throws SQLException{
        try {
            ArrayList<LeaderBoardEntry> leaderBoard = new ArrayList<>();
            String sql = """
                    SELECT *
                    FROM v_leaderboard
                    """;

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String rank = resultSet.getString("rank");
                String nickname = resultSet.getString("nick");
                int points = resultSet.getInt("points");
                long totalPlayedTime = resultSet.getLong("total played time");;
                int wins = resultSet.getInt("number of wins");
                int loses = resultSet.getInt( "number of loses");
                int totalGames = resultSet.getInt("number of games");
                float winRate = resultSet.getFloat("win rate");
                float avgMoves = resultSet.getFloat("avg moves");
                long averageTimePerMove =  resultSet.getLong("avg moves time");
                long averageGameDuration = resultSet.getLong("avg game duration");
                leaderBoard.add(new LeaderBoardEntry(rank, nickname, points, totalPlayedTime/60, wins, loses,totalGames, winRate, avgMoves, averageTimePerMove, averageGameDuration));
            }
            resultSet.close();
            statement.close();
            return leaderBoard;
        } catch (SQLException e) {
            throw e;
        }
    }
}
