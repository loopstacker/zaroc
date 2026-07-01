
package be.kdg.integration.gameapplication.model.databaseaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseChecker{
    private Connection connection;

    public DataBaseChecker(DataBaseConnection dataBaseConnection) throws SQLException{
        try{
            connection = dataBaseConnection.getConnection();
            checkForMissingTables();
            checkForMissingViews();
            checkForMissingPlayersData();
            checkForMissingGamesData();
            checkForMissingConfigsData();
            checkForMissingRanksData();
            checkForMissingMovesData();
        }catch (SQLException e){
            throw e;
        }
    }


    //==============================TABLES==============================//
    private void checkForMissingTables() throws SQLException{
        String createIfExists = """
               
                CREATE TABLE IF NOT EXISTS rank_conversion
                (
                    rank            varchar(20) PRIMARY KEY,
                    rank_min_points NUMERIC(10, 0) NOT NULL,
                    rank_max_points NUMERIC(10, 0) NOT NULL
                        constraint ch_rank_conversation
                            check (rank_max_points > rank_min_points)
                );
                
            
                CREATE TABLE IF NOT EXISTS players
                (
                    player_id       integer
                        constraint pk_players PRIMARY KEY GENERATED ALWAYS AS identity,
                    email           varchar(50) NOT NULL
                        constraint ch_email
                            CHECK (email like '%@%.%' AND
                                   email NOT LIKE '% %')
                        constraint un_email
                            unique,
                    hashed_password char(44)
                        constraint nn_hashed_password NOT NULL,
                    salt            char(24)
                        constraint nn_salt NOT NULL,
                    nickname        varchar(20)
                        constraint nn_nick_name NOT NULL
                        default 'Player'
                );
                
                  CREATE TABLE IF NOT EXISTS configs
                  (
                      config_id           NUMERIC(3)
                          constraint ch_config_id
                              CHECK (config_id between 1 and 5),
                      player_id           integer
                          constraint fk_configs_player_id
                              references players (player_id) NOT NULL,
                      config_name         varchar(40)
                          constraint nn_config_name
                              NOT NULL
                                       default 'config',
                      first_left_color CHAR(7)
                          constraint nn_first_left_color NOT NULL
                                       DEFAULT '#4A3E2A'
                          CONSTRAINT ch_first_left_color
                              CHECK (first_left_color ~ '^#[0-9A-F]{6}$'),
                      first_right_color CHAR(7)
                          constraint nn_first_right_color NOT NULL
                                       DEFAULT '#120D09'
                          CONSTRAINT ch_first_right_color
                              CHECK (first_right_color ~ '^#[0-9A-F]{6}$'),
                      second_left_color CHAR(7)
                          CONSTRAINT nn_second_left_color NOT NULL
                                       DEFAULT '#E8C860'
                          CONSTRAINT ch_second_left_color
                              CHECK (second_left_color ~ '^#[0-9A-F]{6}$'),
                      second_right_color CHAR(7)
                          CONSTRAINT nn_second_right_color NOT NULL
                                       DEFAULT '#4A2E10'
                          CONSTRAINT ch_second_right_color
                              CHECK (second_right_color ~ '^#[0-9A-F]{6}$'),
                      third_left_color CHAR(7)
                          CONSTRAINT nn_third_left_color NOT NULL
                                       DEFAULT '#FFFFFF'
                          CONSTRAINT ch_third_left_color
                              CHECK (third_left_color ~ '^#[0-9A-F]{6}$'),
                      third_right_color CHAR(7)
                          CONSTRAINT nn_third_right_color NOT NULL
                                       DEFAULT '#5A5040'
                          CONSTRAINT ch_third_right_color
                              CHECK (third_right_color ~ '^#[0-9A-F]{6}$'),
                      player_piece_color CHAR(7)
                                       DEFAULT '#CFB997'
                          constraint nn_player_piece_color NOT NULL
                          CONSTRAINT  ch_player_piece_color
                              CHECK (player_piece_color ~ '^#[0-9A-F]{6}$'),
                      enemy_piece_color CHAR(7)
                                       DEFAULT '#61554b'
                          constraint nn_enemy_piece_color NOT NULL,
                      music_volume         numeric(3, 2)
                          constraint nn_music_volume
                              NOT NULL default 1.0
                          constraint ch_music_volume
                              check ( music_volume BETWEEN 0.0 AND 1.0),
                      effects_volume         numeric(3, 2)
                          constraint nn_effects_volume
                              NOT NULL default 1.0,
                      constraint ch_effects_volume
                          check ( effects_volume between 0 and 1.0),
                      screen_size_preferences varchar(20)
                          constraint nn_screen_size_preferences
                              NOT NULL default 'FULLSCREEN'
                      constraint ch_fullscreen
                          check ( screen_size_preferences IN ('TINY','SMALL','NORMAL','BIG','HUGE','FULLSCREEN')),
                              enabled_songs_flags bit(9)
                                  constraint nn_enabled_songs_flags
                                      NOT NULL default '111111111',
                      CONSTRAINT pk_configs primary key (config_id, player_id)
                  );
                
                CREATE TABLE IF NOT EXISTS games
                (
                    game_id                   integer
                        constraint pk_games PRIMARY KEY
                        GENERATED ALWAYS AS identity,
                    player_id                 INTEGER
                        constraint fk_games_player_id
                            REFERENCES players (player_id),
                    game_status               varchar(9)
                        constraint nn_game_status
                            not null
                        constraint ch_game_status
                            check (game_status IN ('GOING', 'PAUSED', 'WIN', 'LOSS'))
                                                              default 'GOING',
                    game_start_date           timestamp
                        constraint nn_game_start_date
                            not null
                             constraint ch_game_start_date
                                    check (game_start_date >= to_date('01-01-2025', 'dd-MM-YYYY')
                                    AND game_start_date <= now() AT TIME ZONE 'Europe/Brussels')
                                    default now(),
                    player_points_before_game numeric(10)
                        constraint nn_player_points_before_game
                            NOT NULL,
                    reward                    numeric(5)
                        constraint ch_reward
                            check (case when game_status IN ('WIN', 'LOSS') then reward is not null else reward is null end),
                    undo_left numeric(1)
                        constraint ch_undo_left
                            check (undo_left BETWEEN 0 AND 3) default 3
                );
                
                CREATE TABLE IF NOT EXISTS moves
                (
                    move_id          numeric(5),
                    game_id          integer
                        constraint fk_moves_game_id
                            references games (game_id),
                    x_position_from  numeric(2)
                        constraint nn_moves_x_position_from
                            NOT NULL,
                    y_position_from  numeric(2)
                        constraint nn_moves_y_position_from
                            NOT NULL,
                
                    x_position_to    numeric(2)
                        constraint nn_moves_x_position_to
                            NOT NULL,
                    y_position_to    numeric(2)
                        constraint nn_moves_y_position_to
                            NOT NULL,
                    duration_of_move interval
                        constraint nn_moves_duration_of_move
                            NOT NULL
                        default '0 seconds',
                    PRIMARY KEY (move_id, game_id),
                    move_owner varchar(40)
                        constraint nn_moves_move_owner
                            NOT NULL
                );
                """;

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(createIfExists);
            statement.close();
        }catch (SQLException e) {
            throw new SQLException("Table creation failed");
        }
    }

    //==============================VIEWS==============================//

    private void checkForMissingViews() throws SQLException{
        String createOrReplaceViews =
                """
DROP VIEW IF EXISTS v_leaderboard;
DROP VIEW IF EXISTS v_game_win_percentage;
DROP VIEW IF EXISTS v_total_played_time;
DROP VIEW IF EXISTS v_average_number_of_moves_per_player;
DROP VIEW IF EXISTS v_ranks_per_id;
DROP VIEW IF EXISTS v_points_per_player;
DROP VIEW IF EXISTS v_number_of_games_per_player;
DROP VIEW IF EXISTS v_average_duration_of_game_per_player;
DROP VIEW IF EXISTS v_all_games_durations_per_game;
DROP VIEW IF EXISTS v_number_of_wins_per_player;
DROP VIEW IF EXISTS v_number_of_losses_per_player;
DROP VIEW IF EXISTS v_average_duration_of_move_per_player;
DROP VIEW IF EXISTS v_configs_per_player;
DROP VIEW IF EXISTS v_player_moves;
DROP VIEW IF EXISTS v_calculate_moves_quartiles;
DROP VIEW IF EXISTS v_players_for_analysis;

CREATE OR REPLACE VIEW v_points_per_player AS
(
SELECT p.player_id, COALESCE(SUM(reward),0) points
FROM players p
         LEFT JOIN games g ON p.player_id = g.player_id
GROUP BY p.player_id
    );

CREATE OR REPLACE VIEW v_all_games_durations_per_game AS
(
SELECT g.game_id, SUM(duration_of_move) duration
FROM moves m
         JOIN games g on g.game_id = m.game_id
WHERE g.game_status IN ('WIN', 'LOSS')
GROUP BY g.game_id
    );

CREATE OR REPLACE VIEW v_number_of_games_per_player AS
(
SELECT player_id, COUNT(game_id) number_of_games
FROM games
WHERE game_status IN ('WIN', 'LOSS')
GROUP BY player_id
    );

CREATE OR REPLACE VIEW v_number_of_wins_per_player AS
(
SELECT player_id, COUNT(g.game_id) number_of_wins
FROM games g
WHERE g.game_status = 'WIN'
GROUP BY player_id
    );

CREATE OR REPLACE VIEW v_number_of_losses_per_player AS
(
SELECT player_id, COUNT(game_id) as number_of_losses
FROM games
WHERE game_status = 'LOSS'
GROUP BY player_id
    );

CREATE OR REPLACE VIEW v_game_win_percentage AS
(
SELECT g.player_id,
       (coalesce(w.number_of_wins, 0)::numeric / coalesce(g.number_of_games, 1)::numeric) * 100.0 percentage
FROM v_number_of_games_per_player g
         INNER JOIN v_number_of_wins_per_player w on g.player_id = w.player_id
    );

CREATE OR REPLACE VIEW v_total_played_time AS
(
SELECT g.player_id, SUM(v.duration) total_played_time
FROM v_all_games_durations_per_game v
         JOIN games g on v.game_id = g.game_id
WHERE g.game_status IN ('WIN', 'LOSS')
GROUP BY g.player_id
    );


CREATE OR REPLACE VIEW v_average_number_of_moves_per_player AS
(
SELECT g.player_id, AVG(moves_per_game.counted_moves) average_moves
FROM games g
         JOIN (SELECT gi.game_id, count(m.move_id) counted_moves
               FROM games gi
                        JOIN moves m on gi.game_id = m.game_id
               where gi.game_status IN ('WIN', 'LOSS')
               GROUP BY gi.game_id) moves_per_game
              ON g.game_id = moves_per_game.game_id
where g.game_status IN ('WIN', 'LOSS')
GROUP BY g.player_id
    );


CREATE OR REPLACE VIEW v_average_duration_of_move_per_player AS
(
SELECT g.player_id, AVG(m.duration_of_move) duration
FROM moves m
         JOIN games g ON g.game_id = m.game_id
WHERE g.game_status IN ('WIN','LOSS')
  AND m.duration_of_move > INTERVAL '0 seconds'
GROUP BY g.player_id
    );

CREATE OR REPLACE VIEW v_average_duration_of_game_per_player AS
(
SELECT g.player_id, AVG(v.duration) duration
FROM v_all_games_durations_per_game v
         JOIN games g ON v.game_id = g.game_id
WHERE g.game_status IN ('WIN','LOSS')
GROUP BY g.player_id
    );

CREATE OR REPLACE VIEW v_ranks_per_id AS
(
SELECT pl.player_id, rank
FROM rank_conversion rc
         JOIN v_points_per_player pl ON pl.points BETWEEN rc.rank_min_points AND rc.rank_max_points
GROUP BY pl.player_id, rank
    );

CREATE OR REPLACE VIEW v_leaderboard AS
(
SELECT COALESCE(rc.rank, 'Unknown') rank, --1
       p.nickname nick, --2
       COALESCE(pv.points,0) points, --3
       EXTRACT(EPOCH FROM COALESCE(tpt.total_played_time, INTERVAL '0 seconds')) "total played time", --4
       COALESCE(nowi.number_of_wins,0) "number of wins", --5
       COALESCE(nol.number_of_losses,0) "number of loses", --6
       COALESCE(gwp.percentage,0) "win rate", --7
       COALESCE(nog.number_of_games,0) "number of games", --8
       COALESCE(anom.average_moves,0) "avg moves", --9
       EXTRACT(EPOCH FROM COALESCE(adpm.duration, INTERVAL '0 seconds')) "avg moves time",--10
       EXTRACT(EPOCH FROM COALESCE(adgp.duration, INTERVAL '0 seconds')) "avg game duration" --11
FROM players p
         LEFT JOIN v_points_per_player pv ON p.player_id = pv.player_id
         LEFT JOIN rank_conversion rc ON pv.points BETWEEN rc.rank_min_points AND rc.rank_max_points
         LEFT JOIN v_average_duration_of_move_per_player adpm on pv.player_id = adpm.player_id
         LEFT JOIN v_average_number_of_moves_per_player anom on pv.player_id = anom.player_id
         LEFT JOIN v_game_win_percentage gwp on pv.player_id = gwp.player_id
         LEFT JOIN v_number_of_games_per_player nog on pv.player_id = nog.player_id
         LEFT JOIN v_number_of_wins_per_player nowi on pv.player_id = nowi.player_id
         LEFT JOIN v_total_played_time tpt on pv.player_id = tpt.player_id
         LEFT JOIN v_number_of_losses_per_player nol on pv.player_id = nol.player_id
         LEFT JOIN v_average_duration_of_game_per_player adgp on p.player_id = adgp.player_id
ORDER BY 2
    );


CREATE OR REPLACE VIEW v_configs_per_player AS
(
SELECT player_id, config_id, config_name,
       first_left_color, first_right_color,
       second_left_color, second_right_color,
       third_left_color, third_right_color,
       player_piece_color, enemy_piece_color,
       music_volume, effects_volume, screen_size_preferences,
       enabled_songs_flags
FROM configs
GROUP BY player_id, config_id
    );


------------------------------------------------ FOR DETECTING THE OUTLIERS---------------------------------------------------------------
CREATE OR REPLACE VIEW v_calculate_moves_quartiles AS (
                                                      SELECT
                                                                  percentile_cont(0.75) WITHIN GROUP ( ORDER BY
                                                              date_part('hours', duration_of_move) * 3600 + date_part('minutes', duration_of_move) * 60 + date_part('seconds', duration_of_move)) as q3,
                                                                  percentile_cont(0.25) WITHIN GROUP ( ORDER BY
                                                                      date_part('hours', duration_of_move) * 3600 + date_part('minutes', duration_of_move) * 60 + date_part('seconds', duration_of_move)) as q1
                                                      FROM moves
                                                          );
                        CREATE OR REPLACE VIEW v_player_moves AS (
                                                    SELECT p.nickname as player,
                                                           TO_CHAR(g.game_start_date, 'DD/MM/YYYY HH24:MI') as game,
                                                           CASE g.game_status WHEN 'WIN' THEN 'W'
                                                                              WHEN 'LOSS' THEN 'L'
                                                                              ELSE ' ' END as outcome,
                                                           date_part('hours', m.duration_of_move) * 3600 + date_part('minutes', m.duration_of_move) * 60 + date_part('seconds', m.duration_of_move) as duration,
                                                           CASE
                                                               WHEN (
                                                                   (SELECT q1 - 1.5 * (q3 - q1)
                                                                    FROM v_calculate_moves_quartiles
                                                                   ) > (date_part('hours', m.duration_of_move) * 3600 + date_part('minutes', m.duration_of_move) * 60 + date_part('seconds', m.duration_of_move))
                                                                       OR
                                                                   (
                                                                       SELECT q3 + 1.5 * (q3 - q1)
                                                                       FROM v_calculate_moves_quartiles
                                                                   ) < (date_part('hours', m.duration_of_move) * 3600 + date_part('minutes', m.duration_of_move) * 60 + date_part('seconds', m.duration_of_move)))
                                                                   THEN 'X'
                                                               ELSE ' '
                                                               END as outlier
                                                    FROM moves m
                                                             JOIN games g ON m.game_id = g.game_id
                                                             JOIN players p ON p.player_id = g.player_id
                                                    WHERE move_owner = 'PLAYER' AND (g.game_status = 'WIN' OR g.game_status = 'LOSS')
                                                );
---------------------------------- FOR ORANGE ----------------------------------
CREATE OR REPLACE VIEW v_players_for_analysis AS
(
SELECT p.player_id,
       g.game_id,
       reward                  AS points,
       AVG(m.duration_of_move) AS avg_move_duration
FROM games g
         JOIN players p ON g.player_id = p.player_id
         JOIN moves m ON m.game_id = g.game_id
WHERE m.move_owner = 'PLAYER'
  AND g.game_status IN ('WIN', 'LOSS')
GROUP BY p.player_id, g.game_id
    );
""";
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(createOrReplaceViews);
            statement.close();
        }catch (SQLException e) {
            throw new SQLException("View creation failed");
        }
    }


    //==============================PLAYERS==============================//

    private void checkForMissingPlayersData() throws SQLException{

        String checkPlayers = "SELECT case when (SELECT 'x' FROM players LIMIT 1) = 'x' THEN true ELSE FALSE END";
        try{
            Statement checkIfPresent = connection.createStatement();
            ResultSet rs = checkIfPresent.executeQuery(checkPlayers);
            rs.next();
            if(rs.getBoolean(1)) return;
            rs.close();

            String insertPlayers = """
INSERT INTO players (email, hashed_password, salt, nickname)
VALUES ('alex.smith@gmail.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'ZoracKing1337'),
       ('maria.johnson@yahoo.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'EXTERMINATUS'),
       ('david.brown@hotmail.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'DVDavid'),
       ('sarah.wilson@outlook.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'SarWil'),
       ('chris.evans@gmail.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'Terminator222'),
       ('laura.clark@yahoo.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'Wizard999'),
       ('james.miller@hotmail.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'BESTPLAYEREVER'),
       ('emma.davis@outlook.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'Armagedon'),
       ('daniel.moore@gmail.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==', 'Dan'),
       ('olivia.taylor@yahoo.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==', 'Livia'),
       ('liam.anderson@hotmail.com', 'G5bYy+lxlKpR5yLDBipg+p7qAgIoPXXPqob8eqeGnJc=', '2CUAcw46L52NFqXV4bdXFA==',
        'Mial');
                    """;
            Statement insertData = connection.createStatement();
            insertData.executeUpdate(insertPlayers);
            insertData.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during inserting mock-up data for players accounts");
        }
    }

    //==============================GAMES==============================//
    private void checkForMissingGamesData() throws SQLException{

        String checkGames = "SELECT case when (SELECT 'x' FROM games LIMIT 1) = 'x' THEN true ELSE FALSE END";
        try{
            Statement checkIfPresent = connection.createStatement();
            ResultSet rs = checkIfPresent.executeQuery(checkGames);
            rs.next();
            if(rs.getBoolean(1)) return;

            String insertGames = """
                   
                    INSERT INTO games (player_id, player_points_before_game)
                   
                       (SELECT player_id,
                               (COALESCE((SELECT points
                                          FROM v_points_per_player
                                          where player_id = p.player_id), 0))
                        FROM generate_series(1, 20) as amount_of_times,
                             players p
                        where player_id BETWEEN 1 AND (SELECT count(*)
                                                       FROM players));
                    """;
            Statement insertData = connection.createStatement();
            insertData.executeUpdate(insertGames);
            insertData.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during inserting mock-up data for games");
        }
        updateGameResult();
    }

    //==============================GAME RESULTS==============================//
    private void updateGameResult() throws SQLException{
        try{
            String updateGameResults = """
                    UPDATE games o
                SET game_status = 'LOSS',
                    reward      = (-15 + random() * 15) :: integer,
                    undo_left = 0
                WHERE game_id between 1 and 10;
                UPDATE games o
                SET game_status = 'WIN',
                    reward      = (10 + random() * 10) :: integer,
                    undo_left = (random()*3) :: numeric
                WHERE game_id between 25 and 220;
                """;
            Statement updateResults = connection.createStatement();
            updateResults.executeUpdate(updateGameResults);
            updateResults.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during inserting mock-up data for game results");
        }
    }

    //==============================CONFIGS==============================//
    private void checkForMissingConfigsData() throws SQLException{

        String checkConfigs = "SELECT case when (SELECT 'x' FROM configs LIMIT 1) = 'x' THEN true ELSE FALSE END";
        try{
            Statement checkIfPresent = connection.createStatement();
            ResultSet rs = checkIfPresent.executeQuery(checkConfigs);
            rs.next();
            if(rs.getBoolean(1)) return;
            String insertGames = """
INSERT INTO configs(config_id, player_id)
    (SELECT confs, ids
     FROM generate_series(1, 5) AS confs,
          generate_series(1, (SELECT count(*)
                              FROM players)) as ids);

UPDATE configs c
SET
    enemy_piece_color = '#4A2B9C',
    player_piece_color = '#4A2B9C',
    effects_volume = 1,
    music_volume = 0.1,
    config_name = 'To play at night',
    screen_size_preferences = 'HUGE',
    first_left_color = '#FF5733',
    first_right_color = '#C70039',
    second_left_color = '#900C3F',
    second_right_color = '#581845',
    third_left_color = '#1C2833',
    third_right_color = '#2E4057'
FROM generate_series(1,5) AS gs(player_id)
WHERE gs.player_id = c.player_id
  AND config_id IN (2,3);

UPDATE configs c
SET
    enemy_piece_color = '#4A2B9C',
    player_piece_color = '#4A2B9C',
    effects_volume = 1,
    music_volume = 0.8,
    config_name = 'Light white',
    screen_size_preferences = 'TINY',
    first_left_color = '#A8E6CF',
    first_right_color = '#DCEDC1',
    second_left_color = '#FFD3B6',
    second_right_color = '#FFAAA5',
    third_left_color = '#FF8B94',
    third_right_color = '#F67280'
FROM generate_series(1,5) AS gs(player_id)
WHERE gs.player_id = c.player_id
  AND config_id = 3;

UPDATE configs c
SET
    enemy_piece_color = '#4A5F9C',
    player_piece_color = '#4B2C9C',
    effects_volume = 1,
    music_volume = 0.3,
    config_name = 'Cyan color',
    screen_size_preferences = 'SMALL',
    first_left_color = '#0B3D91',
    first_right_color = '#1A6B8A',
    second_left_color = '#17A589',
    second_right_color = '#1E8449',
    third_left_color = '#7D3C98',
    third_right_color = '#2E86C1'
FROM generate_series(1,5) AS gs(player_id)
WHERE gs.player_id = c.player_id
  AND config_id = 4;

""";
            Statement insertData = connection.createStatement();
            insertData.executeUpdate(insertGames);
            insertData.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during inserting mock-up data for configs");
        }
    }


    //==============================RANKS==============================//
    private void checkForMissingRanksData() throws SQLException{

        String checkRanks = "SELECT case when (SELECT 'x' FROM rank_conversion LIMIT 1) = 'x' THEN true ELSE FALSE END";
        try{
            Statement checkIfPresent = connection.createStatement();
            ResultSet rs = checkIfPresent.executeQuery(checkRanks);
            rs.next();
            if(rs.getBoolean(1)) return;
            String insertGames = """
                    INSERT INTO rank_conversion
                    VALUES ('Servant', -(10^10)+1 , 0),
                           ('Guard', 0, 200),
                           ('Knight', 201, 400),
                           ('Prince', 401, 500),
                           ('King', 501, 800),
                           ('Emperor', 801, (10^10)-1);
                    """;
            Statement insertData = connection.createStatement();
            insertData.executeUpdate(insertGames);
            insertData.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during inserting mock-up data for ranks");
        }
    }


    //==============================MOVES==============================//
    private void checkForMissingMovesData() throws SQLException{

        String checkMoves = "SELECT case when (SELECT 'x' FROM moves LIMIT 1) = 'x' THEN true ELSE FALSE END";
        try{
            Statement checkIfPresent = connection.createStatement();
            ResultSet rs = checkIfPresent.executeQuery(checkMoves);
            rs.next();
            if(rs.getBoolean(1)) return;
            String insertGames = """
                    INSERT INTO moves (move_id, game_id, x_position_to, x_position_from, y_position_to, y_position_from,
                                       duration_of_move, move_owner)
                        (SELECT DISTINCT move_num,
                                         games,
                                         (floor(random() * 4 + 1))::int,
                                         (floor(random() * 4 + 1))::int,
                                         (floor(random() * 4 + 1))::int,
                                         (floor(random() * 4 + 1))::int,
                                         (floor(random() * 20 + 1)::text || ' seconds')::interval,
                                         CASE WHEN mod(move_num, 4) IN (1, 2) THEN 'PLAYER' ELSE 'ENEMY' END
                         FROM generate_series(1, (SELECT count(*)
                                                  FROM games)) AS games,
                              generate_series(1, 10, 1) AS move_num);
                    """;
            Statement insertData = connection.createStatement();
            insertData.executeUpdate(insertGames);
            insertData.close();
        }catch (SQLException e) {
            throw new SQLException("Something went wrong during inserting mock-up data for moves");
        }
    }
}