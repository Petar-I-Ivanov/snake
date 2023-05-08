package com.github.snake.dto;

import com.github.snake.models.Game;
import com.github.snake.services.GameService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MappingService {

  private GameService gameService;

  public MappingService(GameService gameService) {
    this.gameService = gameService;
  }

  public GameDTO findById(Long gameId) {
    return convertGameToDto(gameService.findById(gameId));
  }

  public GameDTO startNewGame() {
    return convertGameToDto(gameService.startNewGame());
  }

  public GameDTO makeAction(Long gameId, char action) {
    return convertGameToDto(gameService.gameLoop(gameId, action));
  }

  private GameDTO convertGameToDto(Game game) {

    GameDTO dto = new GameDTO();

    dto.setId(game.getId());
    dto.setStatus(game.getStatus());
    dto.setTurn(game.getTurn());
    dto.setMap(gameService.getGameboard(game.getId()));

    return dto;
  }
}
