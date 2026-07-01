DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS configs CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS moves CASCADE;
DROP TABLE IF EXISTS pieces CASCADE;
DROP TABLE IF EXISTS rank_conversion CASCADE;

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

CREATE TABLE IF NOT EXISTS rank_conversion
(
    rank            varchar(20) PRIMARY KEY,
    rank_min_points NUMERIC(10, 0) NOT NULL,
    rank_max_points NUMERIC(10, 0) NOT NULL
        constraint ch_rank_conversation
            check (rank_max_points > rank_min_points)
);

ALTER TABLE rank_conversion
    ALTER COLUMN rank_min_points TYPE NUMERIC(20, 0)
        USING ROUND(rank_min_points);

ALTER TABLE rank_conversion
    ALTER COLUMN rank_max_points TYPE NUMERIC(20, 0)
        USING ROUND(rank_max_points);

CREATE TABLE IF NOT EXISTS games
(
    game_id                   integer
        constraint pk_games PRIMARY KEY
        GENERATED ALWAYS AS identity,
    player_id                 INTEGER
        constraint fk_games_player_id
            REFERENCES players (player_id)
        constraint nn_games_player_id
            NOT NULL,
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
                AND game_start_date <= (now() AT TIME ZONE 'Europe/Brussels'))
        default (now() AT TIME ZONE 'Europe/Brussels'),
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


