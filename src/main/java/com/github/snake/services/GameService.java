package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.GameStatusEnum;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.repositories.GameRepository;
import com.github.snake.services.enemy.EnemyService;
import com.github.snake.services.food.FoodService;
import com.github.snake.utilities.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameService {

  private GameRepository gameRepository;

  private SnakeService snakeService;
  private FoodService foodService;
  private EnemyService enemyService;
  private BarrierService barrierService;
  private ExitService exitService;

  private GameboardPositionService gameboardObjectsService;

  public GameService(GameRepository gameRepository, SnakeService snakeService,
      FoodService foodService, EnemyService enemyService,
      BarrierService barrierService, ExitService exitService,
      GameboardPositionService gameboardObjectsService) {

    this.gameRepository = gameRepository;
    this.snakeService = snakeService;
    this.foodService = foodService;
    this.enemyService = enemyService;
    this.barrierService = barrierService;
    this.exitService = exitService;

    this.gameboardObjectsService = gameboardObjectsService;
  }

  public Game findById(Long gameId) {
    return gameRepository.findById(gameId);
  }

  @Transactional
  public Game startNewGame() {
    Game game = new Game();
    gameRepository.persistAndFlush(game);

    snakeService.generateSnake(game);
    gameRepository.persistAndFlush(game);
    return game;
  }

  @Transactional
  public Game gameLoop(Long gameId, char action) {

    Game game = gameRepository.findById(gameId);

    snakeService.move(game, action);
    foodService.foodCheck(game);
    enemyService.turnActionAndChecks(game);

    if (isGameIncreasing(game)) {
    	
      gameboardObjectsService.moveAllWithOneRowAndCol(gameId);
      enemyService.generatePoacher(game);
      barrierService.generateBarrier(game);
    }
    
    if (snakeService.isSnakeEscaped(gameId) || snakeService.isSnakeKilled(gameId)) {
    	
    	GameStatusEnum status = snakeService.isSnakeEscaped(gameId) ? GameStatusEnum.WON : GameStatusEnum.LOST;
    	game.setStatus(status);
    }

    game.setTurn((short) (game.getTurn() + 1));
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
  
  private boolean isGameIncreasing(Game game) {
	  
	  int snakeSize = snakeService.getSnakeSize(game.getId());
	  
	  if (game.getStatus() == GameStatusEnum.LEVEL_ONE && snakeSize >= 5) {
		  game.setStatus(GameStatusEnum.LEVEL_TWO);
		  return true;
	  }
	  
	  if (game.getStatus() == GameStatusEnum.LEVEL_TWO && snakeSize >= 10) {
		  game.setStatus(GameStatusEnum.LEVEL_THREE);
		  return true;
	  }
	  
	  if (game.getStatus() == GameStatusEnum.LEVEL_THREE && snakeSize >= 15) {
		  game.setStatus(GameStatusEnum.LEVEL_FOUR);
		  return true;
	  }
	  
	  if (game.getStatus() == GameStatusEnum.LEVEL_FOUR && snakeSize >= 20) {
		  game.setStatus(GameStatusEnum.LEVEL_FIVE);
		  return true;
	  }
	  
	  if (game.getStatus() == GameStatusEnum.LEVEL_FIVE && snakeSize >= 25) {
		  game.setStatus(GameStatusEnum.LEVEL_SIX);
		  exitService.generateExit(game);
		  return true;
	  }
	  
	  return false;
  }
}
