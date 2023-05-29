package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.repositories.GameRepository;
import com.github.snake.services.interfaces.GamePlayService;
import com.github.snake.services.interfaces.GameService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameServiceImpl implements GameService {

  private GameRepository gameRepository;
  private GamePlayService gamePlayService;

  public GameServiceImpl(GameRepository gameRepository, GamePlayService gamePlayService) {

    this.gameRepository = gameRepository;
    this.gamePlayService = gamePlayService;
  }

  @Override
  public Game findById(Long gameId) {
    return gameRepository.findById(gameId);
  }

  @Override
  @Transactional
  public Game startNewGame() {

    Game game = new Game();
    gameRepository.persistAndFlush(game);

    gamePlayService.generateStartingObjects(game);
    return game;
  }

  @Override
  @Transactional
  public Game makeAction(Long gameId, char action) {

    Game game = gameRepository.findById(gameId);

    gamePlayService.gameLoop(game, action);

    game.setTurn((short) (game.getTurn() + 1));
    gameRepository.persistAndFlush(game);
    return game;
  }
}
