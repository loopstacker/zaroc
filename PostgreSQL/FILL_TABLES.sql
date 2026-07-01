--First create tables, views and only then fill them in with the following mock data

TRUNCATE TABLE players RESTART IDENTITY CASCADE;
TRUNCATE TABLE configs RESTART IDENTITY CASCADE;
TRUNCATE TABLE games RESTART IDENTITY CASCADE;
TRUNCATE TABLE moves RESTART IDENTITY CASCADE;
TRUNCATE TABLE rank_conversion RESTART IDENTITY CASCADE;

--========
---------------PLAYERS----------------
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
--=============

---------------GAMES----------------
INSERT INTO games (player_id, player_points_before_game)
    (SELECT player_id,
            (COALESCE((SELECT points
                       FROM v_points_per_player
                       where player_id = p.player_id), 0))
     FROM generate_series(1, 20) as amount_of_times,
          players p
     where player_id BETWEEN 1 AND (SELECT count(*)
                                    FROM players));
--============
---------------CONFIGS----------------

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
  AND config_id = 2;

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

--============
---------------RANKS----------------
INSERT INTO rank_conversion

VALUES ('Servant', -(10^10)+1 , 0),
       ('Guard', 0, 200),
       ('Knight', 201, 400),
       ('Prince', 401, 500),
       ('King', 501, 800),
       ('Emperor', 801, (10^10)-1);

--=================
--==================
------------------MOVES-------------------
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

--==================
------------------WINS AND LOSSES-------------------
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
