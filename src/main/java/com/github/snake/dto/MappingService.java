package com.github.snake.dto;

public interface MappingService {

  GameDTO findById(String encodedGameId);

  GameDTO startNewGame();

  GameDTO makeAction(String encodedGameId, char action);
}
