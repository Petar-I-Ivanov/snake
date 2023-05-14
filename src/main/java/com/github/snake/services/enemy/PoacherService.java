package com.github.snake.services.enemy;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.enemy.Poacher;
import com.github.snake.repositories.Repository;
import com.github.snake.services.GameboardPositionService;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PoacherService {

	private Repository repository;
	private TrapService trapService;
	private GameboardPositionService positionService;
	
	public PoacherService(Repository repository, TrapService trapService,
			GameboardPositionService positionService) {
		
		this.repository = repository;
		this.trapService = trapService;
		this.positionService = positionService;
	}
	
	public void generatePoacher(Game game) {
		
		Poacher poacher = new Poacher();
		poacher.setLocation(getRandomFreeCorner(game));
		poacher.setGame(game);
		
		repository.save(poacher);
		
		// game.getEnemies().add(poacher);
	}
	
	public void movePoachers(Game game) {
		
		for (Poacher poacher : repository.findListByGameId(game.getId(), Poacher.class)) {
			
			Position poacherPosition = null;
			
			if (RandomGenerator.isTwentyFivePercentChanceSuccessful()) {
				poacherPosition = poacher.getLocation();
			}
			
			movePoacher(game, poacher);
			
			if (poacherPosition != null) {
				trapService.generateTrap(game, poacherPosition);
			}
			
			repository.save(poacher);
		}
	}
	
	private Position getRandomFreeCorner(Game game) {
		
		Position randomCorner;
		
		do {
			randomCorner = RandomGenerator.randomCornerPosition(game);
		} while (positionService.isPositionOccupied(game.getId(), randomCorner));
		
		return randomCorner;
	}
	
	private void movePoacher(Game game, Poacher poacher) {
		
		Position nextPosition = RandomGenerator.randomNearPosition(game, poacher.getLocation());
		
		if (positionService.isPositionBarrier(game.getId(), nextPosition)) {
			
			do {
				nextPosition = RandomGenerator.randomNearPosition(game, nextPosition);
			} while (nextPosition != poacher.getLocation());
		}
		
		if (positionService.isPositionOccupied(game.getId(), nextPosition)) {
			movePoacher(game, poacher);
			return;
		}
		
		poacher.setLocation(nextPosition);
	}
}
