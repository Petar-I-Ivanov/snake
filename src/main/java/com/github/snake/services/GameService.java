package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.repositories.GameRepository;
import com.github.snake.services.food.NormalFoodService;
import com.github.snake.utilities.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameService {

  private GameRepository gameRepository;

  private SnakeService snakeService;
  private NormalFoodService foodService;

  private GameboardObjectsService gameboardObjectsService;

  public GameService(GameRepository gameRepository, SnakeService snakeService,
      NormalFoodService foodService, GameboardObjectsService gameboardObjectsService) {

    this.gameRepository = gameRepository;
    this.snakeService = snakeService;
    this.foodService = foodService;

    this.gameboardObjectsService = gameboardObjectsService;
  }

  public Game findById(Long gameId) {
    return gameRepository.findById(gameId);
  }

  @Transactional
  public Game startNewGame() {
    Game game = new Game();
    gameRepository.persistAndFlush(game);

    snakeService.addSnake(game);
    gameRepository.persistAndFlush(game);
    return game;
  }

  @Transactional
  public Game gameLoop(Long gameId, char action) {

    Game game = gameRepository.findById(gameId);

    snakeService.move(game, action);
    foodService.addCheck(game);
    gameRepository.persistAndFlush(game);
    return game;
  }

  public String[][] getGameboard(Long gameId) {

    Game game = gameRepository.findById(gameId);
    byte border = Constants.getGameboardRowCol(game);

    String[][] gameboard = new String[border][border];
    setGameboardObjects(gameId, gameboard);

    for (int row = 0; row < gameboard.length; row++) {
      for (int col = 0; col < gameboard[0].length; col++) {

        if (gameboard[row][col] == null) {
          gameboard[row][col] = Constants.EMPTY_SPACE_SIGN;
        }
      }
    }

    return gameboard;
  }

  private void setGameboardObjects(Long gameId, String[][] gameboard) {

    for (GameboardObject object : gameboardObjectsService.findAllForGameId(gameId)) {

      gameboard[object.getRowLocation()][object.getColLocation()] = object.getSign();
    }
  }
}
