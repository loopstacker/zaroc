package be.kdg.integration.gameapplication.model.leaderboard;

import be.kdg.integration.gameapplication.model.databaseaccess.LeaderBoardDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Manages the leaderboard by retrieving, sorting, and showing
 * {@link LeaderBoardEntry} records from the underlying data source.
 *
 * @see LeaderBoardDatabase
 */

public class LeaderBoardManager{
    private ArrayList<LeaderBoardEntry> allEntries;
    private LeaderBoardDatabase leaderBoardDataBase;

    /**
     * Constructs a new {@code LeaderBoardManager} and performs an initial
     * load of leaderboard data from the provided database.
     *
     * @param leaderBoardDataBase the data source to retrieve leaderboard entries from
     * @throws LeaderBoardException if the leaderboard data cannot be retrieved
     */
    public LeaderBoardManager(LeaderBoardDatabase leaderBoardDataBase) throws LeaderBoardException{
        this.leaderBoardDataBase = leaderBoardDataBase;
        try{
            refreshLeaderBoard();
        }catch (LeaderBoardException e) {
            throw e;
        }
    }

    /**
     * Refreshes the leaderboard by re-fetching all entries from the database
     * and re-sorting them in descending order by score.
     *
     * @throws LeaderBoardException if the leaderboard data cannot be reached or retrieved
     */
    public void refreshLeaderBoard() throws LeaderBoardException{
        try{
            allEntries = leaderBoardDataBase.getLeaderBoardEntries();
            sortedByPoints();
            reverseCurrentSorting();
        }catch (SQLException e) {
            throw new LeaderBoardException("Cannot reach the leaderboard info");
        }
    }

    private void sortedByPoints(){
        allEntries.sort(Comparator.comparingInt(r -> r.getScore()));
    }

    private void reverseCurrentSorting(){
        Collections.reverse(allEntries);
    }

    public ArrayList<LeaderBoardEntry> getAllEntries() {
        return allEntries;
    }
}
