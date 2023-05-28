package com.github.snake.services.gameboard;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.services.gameboard.food.FoodService;
import com.github.snake.services.gameboard.helpers.GameboardPositionService;
import com.github.snake.services.gameboard.helpers.RandomPositionGeneratorService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SnakeMovementService {

  private Repository repository;
  private FoodService foodService;

  private GameboardPositionService positionService;
  private RandomPositionGeneratorService randomPositionService;

  public SnakeMovementService(Repository repository, FoodService foodService,
      GameboardPositionService positionService,
      RandomPositionGeneratorService randomPositionService) {

    this.repository = repository;
    this.foodService = foodService;

    this.positionService = positionService;
    this.randomPositionService = randomPositionService;
  }

  public void move(Game game, char action) {

    SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);
    List<SnakeBody> bodies = repository.findListByGameId(game.getId(), SnakeBody.class);

    Position nextPosition = Position.getNextPositionFromChar(head.getLocation(), action);
    nextPosition = validateNextPosition(game, head, nextPosition);

    Long gameId = game.getId();

    if (!positionService.isPositionOccupied(gameId, nextPosition)) {
      moveAndReturnLastPosition(head, bodies, nextPosition);
    }

    if (positionService.isPositionNormalFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);

      if (!head.isPoisonousFoodActive()) {

        Position lastPosition = moveAndReturnLastPosition(head, bodies, nextPosition);

        SnakeBody body = new SnakeBody();
        body.setLocation(lastPosition);
        body.setGame(game);

        bodies.add(body);

        if (head.isGrowthFoodActive()) {

          Position newLastPosition =
              randomPositionService.getRandomFreeInlinePositionAround(game, lastPosition);

          // inside this if the bodies are lost
          // if (newLastPosition != null) {

          SnakeBody growthBody = new SnakeBody();
          growthBody.setLocation(newLastPosition);
          growthBody.setGame(game);

          bodies.add(growthBody);
          // }

          head.setGrowthFoodActive(false);
        }
      } else {
        head.setPoisonousFoodActive(false);
      }
    }

    if (positionService.isPositionPoisonousFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);

      if (bodies.isEmpty()) {
        head.setPoisonousFoodActive(true);
      } else {

        int lastBodyIndex = bodies.size() - 1;
        SnakeBody lastBody = bodies.get(lastBodyIndex);

        bodies.remove(lastBodyIndex);
        repository.delete(lastBody);
      }

      moveAndReturnLastPosition(head, bodies, nextPosition);
    }

    if (positionService.isPositionBorderFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      head.setBorderFoodActive(true);
      moveAndReturnLastPosition(head, bodies, nextPosition);
    }

    if (positionService.isPositionGrowthFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      head.setGrowthFoodActive(true);
      moveAndReturnLastPosition(head, bodies, nextPosition);
    }

    if (positionService.isPositionImmunityFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      head.setImmunityFoodActive(true);
      moveAndReturnLastPosition(head, bodies, nextPosition);
    }

    if (positionService.isPositionEnemy(gameId, nextPosition)) {
      head.setKilled(true);
    }

    if (positionService.isPositionExit(gameId, nextPosition)) {
      head.setEscaped(true);
    }

    repository.save(head);
    repository.save(bodies);
  }

  private static Position validateNextPosition(Game game, SnakeHead head, Position nextPosition) {

    if (!Position.isPositionInBorders(game, nextPosition)) {

      if (head.isBorderFoodActive()) {

        head.setBorderFoodActive(false);
        return Position.modifyOutOfBorderPosition(game, nextPosition);
      }

      throw new IllegalArgumentException("Next position is outside the borders.");
    }

    return nextPosition;
  }

  private static Position moveAndReturnLastPosition(SnakeHead head, List<SnakeBody> bodies,
      Position nextPosition) {

    Position temp = head.getLocation();
    head.setLocation(nextPosition);
    nextPosition = temp;

    for (SnakeBody body : bodies) {

      temp = body.getLocation();
      body.setLocation(nextPosition);
      nextPosition = temp;
    }

    return nextPosition;
  }
}
