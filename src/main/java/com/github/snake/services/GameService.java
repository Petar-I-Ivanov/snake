package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.repositories.GameRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameService {

  private GameRepository gameRepository;
  private GamePlayService gamePlayService;

  public GameService(GameRepository gameRepository, GamePlayService gamePlayService) {

    this.gameRepository = gameRepository;
    this.gamePlayService = gamePlayService;
  }

  public Game findById(Long gameId) {
    return gameRepository.findById(gameId);
  }

  @Transactional
  public Game startNewGame() {

    Game game = new Game();
    gameRepository.persistAndFlush(game);

    gamePlayService.generateStartingObjects(game);
    return game;
  }

  @Transactional
  public Game gameLoop(Long gameId, char action) {

    Game game = gameRepository.findById(gameId);

    gamePlayService.gameLoop(game, action);

    game.setTurn((short) (game.getTurn() + 1));
    gameRepository.persistAndFlush(game);
    return game;
  }

  public String[][] getGameboard(Long gameId) {

    Game game = gameRepository.findById(gameId);
    return gamePlayService.getGameboard(game);
  }

  public boolean isGrowthFoodActive(Long gameId) {
    return gamePlayService.isGrowthFoodActive(gameId);
  }

  public boolean isBorderFoodActive(Long gameId) {
    return gamePlayService.isBorderFoodActive(gameId);
  }

  public boolean isImmunityFoodActive(Long gameId) {
    return gamePlayService.isImmunityFoodActive(gameId);
  }
}
