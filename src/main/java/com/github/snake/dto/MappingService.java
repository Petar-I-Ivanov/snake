package com.github.snake.dto;

import com.github.snake.api.encoder.IdEncoder;
import com.github.snake.models.Game;
import com.github.snake.models.GameStatusEnum;
import com.github.snake.services.GameService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MappingService {

  private GameService gameService;
  private IdEncoder encoder;

  public MappingService(GameService gameService, IdEncoder encoder) {

    this.gameService = gameService;
    this.encoder = encoder;
  }

  public GameDTO findById(String encodedGameId) {

    Long decodedGameId = encoder.decode(encodedGameId);
    return convertGameToDto(gameService.findById(decodedGameId));
  }

  public GameDTO startNewGame() {
    return convertGameToDto(gameService.startNewGame());
  }

  public GameDTO makeAction(String encodedGameId, char action) {

    Long decodedGameId = encoder.decode(encodedGameId);
    return convertGameToDto(gameService.gameLoop(decodedGameId, action));
  }

  private GameDTO convertGameToDto(Game game) {

    GameDTO dto = new GameDTO();
    String encodedId = encoder.encode(game.getId());

    dto.setId(encodedId);
    dto.setStatus(game.getStatus());
    dto.setTurn(game.getTurn());

    if (isGameOngoing(game)) {
      dto.setMap(gameService.getGameboard(game.getId()));
    }

    return dto;
  }

  private static boolean isGameOngoing(Game game) {
    return game.getStatus() != GameStatusEnum.WON && game.getStatus() != GameStatusEnum.LOST;
  }
}
