package be.kdg.integration.gameapplication.model.leaderboard;

/**
 * Represents a single entry in the leaderboard, containing a player's
 * statistics and performance metrics for ranking purposes.
 */

public class LeaderBoardEntry{
    private String rank;
    private String playerName;
    private int score;
    private long totalMinutesPlayed;
    private int wins;
    private int loses;
    private int totalGames;
    private float winRate;
    private float averageNumberOfTurns;
    private long averageDurationPerTurn;
    private long averageDurationOfGame;

    /**
     * Creates/Constructs a new {@code LeaderBoardEntry} with all player statistics.
     *
     * @param rank                   the player's current leaderboard rank
     * @param playerName             the display name of the player
     * @param score                  the player's total score
     * @param totalMinutesPlayed     the total minutes the player has played
     * @param wins                   the total number of games won
     * @param loses                  the total number of games lost
     * @param totalGames             the total number of games played
     * @param winRate                the win rate as a percentage (0.0–100.0)
     * @param averageNumberOfTurns   the average number of turns per game
     * @param averageDurationPerTurn the average duration of a single turn in seconds
     * @param averageDurationOfGame  the average duration of a complete game in seconds
     */

    public LeaderBoardEntry(String rank, String playerName, int score, long totalMinutesPlayed, int wins, int loses, int totalGames, float winRate, float averageNumberOfTurns, long averageDurationPerTurn, long averageDurationOfGame){
        this.rank = rank;
        this.playerName = playerName;
        this.score = score;
        this.totalMinutesPlayed = totalMinutesPlayed;
        this.wins = wins;
        this.loses = loses;
        this.totalGames = totalGames;
        this.winRate = winRate;
        this.averageNumberOfTurns = averageNumberOfTurns;
        this.averageDurationPerTurn = averageDurationPerTurn;
        this.averageDurationOfGame = averageDurationOfGame;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getScore(){
        return score;
    }

    public long getAverageDurationPerTurn(){
        return averageDurationPerTurn;
    }

    public float getAverageNumberOfTurns(){
        return averageNumberOfTurns;
    }

    public int getLoses(){
        return loses;
    }

    public int getTotalGames(){
        return totalGames;
    }

    public long getTotalMinutesPlayed(){
        return totalMinutesPlayed;
    }

    public float getWinRate(){
        return winRate;
    }

    public int getWins(){
        return wins;
    }

    public String getRank(){
        return rank;
    }


    /**
     * Returns a formatted string representation of this leaderboard entry,
     * displaying all player statistics aligned in columns.
     *
     * @return a formatted summary of the player's leaderboard statistics
     */

    @Override
    public String toString() {
        return String.format(
                "Rank: %2d | Player: %20s | Score: %10d | Games: %10d | Wins: %10d | Losses: %10d | WinRate: %10.2f | AvgTurns: %10.2f | AvgTurnDuration: %10d | MinutesPlayed: %10d",
                rank, playerName, score, totalGames, wins, loses, winRate,
                averageNumberOfTurns, averageDurationPerTurn, totalMinutesPlayed
        );
    }

}

