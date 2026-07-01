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

SELECT *
FROM v_configs_per_player;

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

SELECT * FROM v_player_moves;


/*
CREATE OR REPLACE VIEW v_player_avg_move_quartiles AS
SELECT
            percentile_cont(0.25) WITHIN GROUP (
        ORDER BY
        date_part('hours', duration) * 3600 +
        date_part('minutes', duration) * 60 +
        date_part('seconds', duration)
        ) AS q1,

            percentile_cont(0.75) WITHIN GROUP (
                ORDER BY
                date_part('hours', duration) * 3600 +
                date_part('minutes', duration) * 60 +
                date_part('seconds', duration)
                ) AS q3

FROM v_average_duration_of_move_per_player;


CREATE OR REPLACE VIEW v_player_outliers AS
SELECT
    player_id,
    date_part('hours', duration) * 3600 +
    date_part('minutes', duration) * 60 +
    date_part('seconds', duration) AS avg_move_seconds,
    CASE
        WHEN
            (
                date_part('hours', duration) * 3600 +
                date_part('minutes', duration) * 60 +
                date_part('seconds', duration)
                )
                <
            (
                SELECT q1 - 1.5 * (q3 - q1)
                FROM v_player_avg_move_quartiles
            )
                OR
            (
                date_part('hours', duration) * 3600 +
                date_part('minutes', duration) * 60 +
                date_part('seconds', duration)
                )
                >
            (
                SELECT q3 + 1.5 * (q3 - q1)
                FROM v_player_avg_move_quartiles
            )

            THEN 'YES'
        ELSE 'NO'
        END AS outlier
FROM v_average_duration_of_move_per_player;
*/

-- FOR CSV --

-- Just specify the email/id of the player you used to play for
-- after you created and retrieved data you need that remove that view for next team-member
-- just remove the old view

DROP VIEW IF EXISTS v_players_for_analysis;
CREATE VIEW v_players_for_analysis AS
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

SELECT *
FROM v_players_for_analysis
WHERE player_id IN (1,2,3);

----------------------------------------------
