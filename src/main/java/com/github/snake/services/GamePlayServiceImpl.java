package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.GameStatusEnum;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.services.interfaces.BarrierService;
import com.github.snake.services.interfaces.EnemyService;
import com.github.snake.services.interfaces.ExitService;
import com.github.snake.services.interfaces.FoodService;
import com.github.snake.services.interfaces.GamePlayService;
import com.github.snake.services.interfaces.GameboardService;
import com.github.snake.services.interfaces.SnakeService;
import com.github.snake.utilities.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;

@ApplicationScoped
public class GamePlayServiceImpl implements GamePlayService {

  private SnakeService snakeService;
  private FoodService foodService;
  private EnemyService enemyService;
  private BarrierService barrierService;
  private ExitService exitService;

  private GameboardService gameboardService;

  public GamePlayServiceImpl(SnakeService snakeService, FoodService foodService,
      EnemyService enemyService, BarrierService barrierService, ExitService exitService,
      GameboardService gameboardService) {

    this.snakeService = snakeService;
    this.foodService = foodService;
    this.enemyService = enemyService;
    this.barrierService = barrierService;
    this.exitService = exitService;

    this.gameboardService = gameboardService;
  }

  @Override
  public boolean isGrowthFoodActive(Long gameId) {
    return snakeService.isGrowthFoodActive(gameId);
  }

  @Override
  public boolean isBorderFoodActive(Long gameId) {
    return snakeService.isBorderFoodActive(gameId);
  }

  @Override
  public boolean isImmunityFoodActive(Long gameId) {
    return snakeService.isImmunityFoodActive(gameId);
  }

  @Override
  public void generateStartingObjects(Game game) {

    snakeService.generateSnake(game);
    foodService.foodCheck(game);
  }

  @Override
  public void gameLoop(Game game, char action) {

    turnActions(game, action);

    GameStatusEnum gameStatus = game.getStatus();
    GameStatusEnum expectedStatus = getExpectedStatus(game.getId());

    if (isExpectedStatusDifferent(gameStatus, expectedStatus)
        && isGameboardIncreasing(gameStatus, expectedStatus)) {

      game.setStatus(expectedStatus);
      expandingGameboard(game);

      if (isLastExpand(expectedStatus)) {
        exitService.generateExit(game);
      }
    }

    if (snakeService.isSnakeEscaped(game.getId()) || snakeService.isSnakeKilled(game.getId())) {

      GameStatusEnum status =
          snakeService.isSnakeEscaped(game.getId()) ? GameStatusEnum.WON : GameStatusEnum.LOST;
      game.setStatus(status);
      gameboardService.deleteGameboardObjects(game.getId());
    }
  }

  @Override
  public String[][] getGameboard(Game game) {

    byte border = Constants.getGameboardRowCol(game);

    String[][] gameboard = new String[border][border];

    setGameboardObjects(game.getId(), gameboard);
    return setEmptySpacesAndReturn(gameboard);
  }

  private void turnActions(Game game, char action) {

    snakeService.move(game, action);
    foodService.foodCheck(game);
    enemyService.turnActionAndChecks(game);
  }

  private void expandingGameboard(Game game) {

    gameboardService.moveAllWithOneRowAndCol(game.getId());
    barrierService.generateBarrier(game);
    enemyService.generatePoacher(game);
  }

  private GameStatusEnum getExpectedStatus(Long gameId) {

    int snakeSize = snakeService.getSnakeSize(gameId);

    return Arrays.asList(GameStatusEnum.values()).stream()
        .filter(status -> isSnakeSizeInStatusBorder(snakeSize, status)).findFirst().orElse(null);
  }

  private void setGameboardObjects(Long gameId, String[][] gameboard) {

    for (GameboardObject object : gameboardService.findAllForGameId(gameId)) {
      gameboard[object.getRowLocation()][object.getColLocation()] = object.getSign();
    }
  }

  private static String[][] setEmptySpacesAndReturn(String[][] gameboard) {

    for (int row = 0; row < gameboard.length; row++) {
      for (int col = 0; col < gameboard[0].length; col++) {

        if (gameboard[row][col] == null) {
          gameboard[row][col] = Constants.EMPTY_SPACE_SIGN;
        }
      }
    }

    return gameboard;
  }

  private static boolean isSnakeSizeInStatusBorder(int snakeSize, GameStatusEnum status) {
    return status.getLowerBound() <= snakeSize && status.getUpperBound() >= snakeSize;
  }

  private static boolean isExpectedStatusDifferent(GameStatusEnum gameStatus,
      GameStatusEnum expectedStatus) {
    return gameStatus != expectedStatus;
  }

  private static boolean isGameboardIncreasing(GameStatusEnum gameStatus,
      GameStatusEnum expectedStatus) {
    return gameStatus.getLowerBound() < expectedStatus.getLowerBound();
  }

  private static boolean isLastExpand(GameStatusEnum gameStatus) {
    return gameStatus == GameStatusEnum.LEVEL_SIX;
  }
}
