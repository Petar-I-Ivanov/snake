package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SnakeService {

	private Repository repository;
	private GameboardPositionService positionService;

	public SnakeService(Repository repository, GameboardPositionService positionService) {
		this.repository = repository;
		this.positionService = positionService;
	}

	public Position getSnakeHeadLocation(Long gameId) {
		return repository.findSingleByGameId(gameId, SnakeHead.class).getLocation();
	}

	public int getSnakeSize(Long gameId) {
		return repository.findListByGameId(gameId, SnakeBody.class).size() + 1;
	}

	public void generateSnake(Game game) {

		SnakeHead snakeHead = new SnakeHead();
		snakeHead.setGame(game);
		repository.save(snakeHead);

		game.setSnakeHead(snakeHead);
	}

	// TODO: simpify this method
	public void move(Game game, char action) {

		SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);
		List<SnakeBody> bodies = repository.findListByGameId(game.getId(), SnakeBody.class);
		Long gameId = game.getId();

		Position nextPosition = Position.getNextPositionFromChar(head.getLocation(), action);

		if (!Position.isPositionInBorders(game, nextPosition)) {
			throw new IllegalArgumentException("Next position is outside the borders.");
		}

		if (!positionService.isPositionOccupied(gameId, nextPosition)) {
			moveAndReturnLastPosition(head, bodies, nextPosition);
			repository.save(head);
			repository.save(bodies);
			return;
		}

		if (positionService.isPositionNormalFood(gameId, nextPosition)) {

			positionService.eatNormalFood(gameId, nextPosition);
			Position lastPosition = moveAndReturnLastPosition(head, bodies, nextPosition);

			SnakeBody body = new SnakeBody();
			body.setLocation(lastPosition);
			body.setGame(game);
			bodies.add(body);

			repository.save(head);
			repository.save(bodies);
			return;
		}

		if (positionService.isPositionPoisonousFood(gameId, nextPosition)) {
			head.setPoisonousFoodActive(true);
			positionService.eatSpecialFood(gameId, nextPosition);
			moveAndReturnLastPosition(head, bodies, nextPosition);
			repository.save(head);
			repository.save(bodies);
			return;
		}

		if (positionService.isPositionBorderFood(gameId, nextPosition)) {
			head.setBorderFoodActive(true);
			positionService.eatSpecialFood(gameId, nextPosition);
			moveAndReturnLastPosition(head, bodies, nextPosition);
			repository.save(head);
			repository.save(bodies);
			return;
		}

		if (positionService.isPositionGrowthFood(gameId, nextPosition)) {
			head.setGrowthFoodActive(true);
			positionService.eatSpecialFood(gameId, nextPosition);
			moveAndReturnLastPosition(head, bodies, nextPosition);
			repository.save(head);
			repository.save(bodies);
			return;
		}

		if (positionService.isPositionImmunityFood(gameId, nextPosition)) {
			head.setImmunityFoodActive(true);
			positionService.eatSpecialFood(gameId, nextPosition);
			moveAndReturnLastPosition(head, bodies, nextPosition);
			repository.save(head);
			repository.save(bodies);
			return;
		}
		
		if (positionService.isPositionEnemy(gameId, nextPosition)) {
			head.setKilled(true);
			repository.save(head);
			return;
		}
		
		if (positionService.isPositionEnemy(gameId, nextPosition)) {
			head.setEscaped(true);
			repository.save(head);
			return;
		}
	}

	private static Position moveAndReturnLastPosition(SnakeHead head, List<SnakeBody> bodies, Position position) {

		Position temp = head.getLocation();
		head.setLocation(position);
		position = temp;

		for (SnakeBody body : bodies) {

			temp = body.getLocation();
			body.setLocation(position);
			position = temp;
		}

		return position;
	}
}
