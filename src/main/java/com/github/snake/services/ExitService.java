package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExitService {

	private Repository repository;
	private GameboardPositionService positionService;
	
	public ExitService(Repository repository, GameboardPositionService positionService) {
		this.repository = repository;
		this.positionService = positionService;
	}
	
	public void generateExit(Game game) {
		
		Position position = getRandomAvailablePositionAtBorder(game);
		
		Exit exit = new Exit();
		exit.setLocation(position);
		exit.setGame(game);
		
		repository.save(exit);
		
		game.setExit(exit);
	}
	
	private Position getRandomAvailablePositionAtBorder(Game game) {
		
		Position position;
		
		do {
			position = getRandomPositionAtBorder(game);
		} while (positionService.isPositionOccupied(game.getId(), position));
		
		return position;
	}
	
	private Position getRandomPositionAtBorder(Game game) {
		
		boolean isRandomRow = isRandomRow();
		
		if (isRandomRow) {
			
			int row = (RandomGenerator.randomInt(2) == 1) ? 0 : Constants.getGameboardRowCol(game) - 1;
			int col = RandomGenerator.randomInt(Constants.getGameboardRowCol(game));
			
			return new Position(row, col);
		}
		
		int row = RandomGenerator.randomInt(Constants.getGameboardRowCol(game));
		int col = (RandomGenerator.randomInt(2) == 1) ? 0 : Constants.getGameboardRowCol(game) - 1;
		return new Position(row, col);
	}
	
	private boolean isRandomRow() {
		return RandomGenerator.randomInt(2) == 0;
	}
}
