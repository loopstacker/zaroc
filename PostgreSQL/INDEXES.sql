CREATE INDEX idx_games_player_date ON games(player_id, game_start_date DESC);
CREATE INDEX idx_moves_game_id ON moves(game_id);

-- TO OPTIMIZE THE QUERIES--
