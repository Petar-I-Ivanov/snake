package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.GameStatusEnum;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.services.gameboard.SnakeService;
import com.github.snake.services.gameboard.enemy.EnemyService;
import com.github.snake.services.gameboard.food.FoodService;
import com.github.snake.services.gameboard.helpers.CommonGameboardService;
import com.github.snake.services.gameboard.terrain.BarrierService;
import com.github.snake.services.gameboard.terrain.ExitService;
import com.github.snake.utilities.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;

@ApplicationScoped
public class GamePlayService {

  private SnakeService snakeService;
  private FoodService foodService;
  private EnemyService enemyService;
  private BarrierService barrierService;
  private ExitService exitService;

  private CommonGameboardService commonGameboardService;

  public GamePlayService(SnakeService snakeService, FoodService foodService,
      EnemyService enemyService, BarrierService barrierService, ExitService exitService,
      CommonGameboardService commonGameboardService) {

    this.snakeService = snakeService;
    this.foodService = foodService;
    this.enemyService = enemyService;
    this.barrierService = barrierService;
    this.exitService = exitService;

    this.commonGameboardService = commonGameboardService;
  }

  public void generateStartingObjects(Game game) {

    snakeService.generateSnake(game);
    foodService.foodCheck(game);
  }

  public void gameLoop(Game game, char action) {

    snakeService.move(game, action);
    foodService.foodCheck(game);
    enemyService.turnActionAndChecks(game);
  }

  public void expandingGameboard(Game game) {

    commonGameboardService.moveAllWithOneRowAndCol(game.getId());
    barrierService.generateBarrier(game);
    enemyService.generatePoacher(game);
  }

  public String[][] getGameboard(Game game) {

    byte border = Constants.getGameboardRowCol(game);

    String[][] gameboard = new String[border][border];

    setGameboardObjects(game.getId(), gameboard);
    return setEmptySpacesAndReturn(gameboard);
  }

  public boolean isGameExpanding(Game game) {

    GameStatusEnum expectedStatus = getExpectedStatus(game.getId());

    if (game.getStatus() != expectedStatus) {

      game.setStatus(expectedStatus);

      if (isLastExpand(game.getStatus())) {
        exitService.generateExit(game);
      }

      return true;
    }

    return false;
  }

  private GameStatusEnum getExpectedStatus(Long gameId) {

    int snakeSize = snakeService.getSnakeSize(gameId);

    return Arrays.asList(GameStatusEnum.values()).stream()
        .filter(status -> isSnakeSizeInStatusBorder(snakeSize, status)).findFirst().orElse(null);
  }

  private void setGameboardObjects(Long gameId, String[][] gameboard) {

    for (GameboardObject object : commonGameboardService.findAllForGameId(gameId)) {
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

  private static boolean isLastExpand(GameStatusEnum gameStatus) {
    return gameStatus == GameStatusEnum.LEVEL_SIX;
  }
}
