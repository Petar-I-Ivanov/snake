package com.github.snake.dto;

import com.github.snake.encoder.IdEncoder;
import com.github.snake.models.Game;
import com.github.snake.models.GameStatusEnum;
import com.github.snake.services.interfaces.GamePlayService;
import com.github.snake.services.interfaces.GameService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MappingServiceImpl implements MappingService {

  private IdEncoder encoder;

  private GameService gameService;
  private GamePlayService gamePlayService;

  public MappingServiceImpl(IdEncoder encoder, GameService gameService,
      GamePlayService gamePlayService) {

    this.encoder = encoder;

    this.gameService = gameService;
    this.gamePlayService = gamePlayService;
  }

  @Override
  public GameDTO findById(String encodedGameId) {

    Long decodedGameId = encoder.decode(encodedGameId);
    return convertGameToDto(gameService.findById(decodedGameId));
  }

  @Override
  public GameDTO startNewGame() {
    return convertGameToDto(gameService.startNewGame());
  }

  @Override
  public GameDTO makeAction(String encodedGameId, char action) {

    Long decodedGameId = encoder.decode(encodedGameId);
    return convertGameToDto(gameService.makeAction(decodedGameId, action));
  }

  private GameDTO convertGameToDto(Game game) {

    GameDTO dto = new GameDTO();
    String encodedId = encoder.encode(game.getId());

    dto.setId(encodedId);
    dto.setStatus(game.getStatus());
    dto.setTurn(game.getTurn());

    if (isGameOngoing(game)) {

      Long gameId = game.getId();

      dto.setMap(gamePlayService.getGameboard(game));

      dto.setBorderActive(gamePlayService.isBorderFoodActive(gameId));
      dto.setGrowthActive(gamePlayService.isGrowthFoodActive(gameId));
      dto.setImmunityActive(gamePlayService.isImmunityFoodActive(gameId));
    }

    return dto;
  }

  private static boolean isGameOngoing(Game game) {
    return game.getStatus() != GameStatusEnum.WON && game.getStatus() != GameStatusEnum.LOST;
  }
}
