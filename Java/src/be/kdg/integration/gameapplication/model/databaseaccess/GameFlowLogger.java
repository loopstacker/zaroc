package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Move;
import be.kdg.integration.gameapplication.model.user.Player;
import org.postgresql.util.PGInterval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;


public class GameFlowLogger{
    private Connection connection;

    public GameFlowLogger(DataBaseConnection dataBaseConnection){
        this.connection = dataBaseConnection.getConnection();
    }

    public void onGameFinish(CompetitiveMatch match) throws SQLException{
        String gameOutcome = match.isWin() ? "WIN" : "LOSS";
        String sql = "UPDATE games SET game_status = ?, reward = ? WHERE game_id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, gameOutcome);
            preparedStatement.setInt(2, match.getReward());
            preparedStatement.setInt(3, match.getGameId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during saving the game");
        }
    }

    private Integer getIdOfLastGame(int playerId) throws SQLException{
        String sql = "SELECT game_id FROM games WHERE player_id = ? ORDER BY 1 desc FETCH NEXT 1 ROW ONLY ";
        Integer gameId;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            gameId = resultSet.getInt(1);
            preparedStatement.close();
            return gameId;
        }catch (SQLException e) {
            throw new SQLException("Unable to find the games");
        }
    }


    private void clearMoves(int gameId) throws SQLException{
        try{
            String clearMoves = "DELETE FROM moves WHERE game_id = ?;";
            PreparedStatement deleteMoves = connection.prepareStatement(clearMoves);
            deleteMoves.setInt(1, gameId);
            deleteMoves.execute();
            deleteMoves.close();
        }catch (SQLException e){
            throw e;
        }
    }


    public void saveGameProgress(CompetitiveMatch match) throws SQLException{
        try{
            saveAllMovesForGame(match.getGameId(), match.getBoardModel().getMoves());
            updateUndo(match.getUndoLeft(), match.getGameId());
        }catch (SQLException e) {
            throw e;
        }
    }

    private void updateUndo(int amount, int gameId) throws SQLException{
        String sql = "UPDATE games SET undo_left = ? WHERE game_id = ?";
        try{
            PreparedStatement undoUpdate = connection.prepareStatement(sql);
            undoUpdate.setInt(1, amount);
            undoUpdate.setInt(2, gameId);
            undoUpdate.executeUpdate();
            undoUpdate.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong while updating the amount of undo's");
        }
    }

    private void saveAllMovesForGame(int gameId, Stack<Move> moves) throws SQLException{ //this method is called at the end of the game or if players leaves in the middle of going game

        try{
            clearMoves(gameId);
        }catch (SQLException e) {
            throw new RuntimeException("Something went wrong during clearing moves");
        }

        try{
            String sql = "INSERT INTO moves (game_id ,move_id, x_position_from, x_position_to, y_position_from, y_position_to, duration_of_move, move_owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement moveInserting = connection.prepareStatement(sql);
            for(int i = 0; i < moves.size(); i++){
                moveInserting.setInt(1, gameId);
                Move move = moves.get(i);
                moveInserting.setInt(2, i);
                moveInserting.setInt(3, move.getPositionFrom().getX());
                moveInserting.setInt(4, move.getPositionTo().getX());
                moveInserting.setInt(5, move.getPositionFrom().getY());
                moveInserting.setInt(6, move.getPositionTo().getY());

                PGInterval pgInterval = new PGInterval();
                pgInterval.setSeconds(move.getTimePerMove());
                moveInserting.setObject(7, pgInterval);
                moveInserting.setString(8, move.getMoveOwner().name());
                moveInserting.addBatch();
            }

            moveInserting.executeBatch();
            moveInserting.close();
        }catch (SQLException e) {
            throw new RuntimeException("Something went wrong during saving game progress");
        }
    }

    public Integer newGame(Player player) throws SQLException{
        try{
            addAGame(player);
            return getIdOfLastGame(player.getPlayerID());
        }catch (SQLException e) {
            throw e;
        }
    }
    private void addAGame(Player player) throws SQLException{
        String sql = "INSERT INTO games(player_id, player_points_before_game) VALUES (?, (SELECT points FROM v_points_per_player WHERE player_id = ?))";

        int playerId = player.getPlayerID();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, playerId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e) {
            throw new RuntimeException("Something went wrong during game creation");
        }
    }
    public int getMoveCountForGame(int gameId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM moves WHERE game_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, gameId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't retrieve the amount of moves for this game.");
        }
        return 0;
    }

    // for who wants to know, "EPOCH" converts it straight to seconds instead of doing minutes * 60 + seconds
    public float getAvgMoveDurationPerGame(int gameId) throws SQLException {
        String sql = "SELECT AVG(EXTRACT(EPOCH FROM duration_of_move)) FROM moves WHERE game_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getFloat(1);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't retrieve the average duration of moves for this game");
        }
        return 0f;
    }
}
