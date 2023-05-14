package com.github.snake.services;

import java.util.ArrayList;
import java.util.List;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.Barrier;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;

import jakarta.enterprise.context.ApplicationScoped;

// TODO: simplify

@ApplicationScoped
public class BarrierService {

	private Repository repository;
	private GameboardPositionService positionService;
	
	public BarrierService(Repository repository, GameboardPositionService positionService) {
		this.repository = repository;
		this.positionService = positionService;
	}
	
	public void generateBarrier(Game game) {
		
		Barrier barrier = new Barrier();
		barrier.setLocation(getRandomAvailablePosition(game));
		barrier.setGame(game);
		
		repository.save(barrier);
		
		// game.getBarriers().add(barrier);
	}
	
	private Position getRandomAvailablePosition(Game game) {
		
		List<Position> availablePosition = getAvailablePosition(game);
		return availablePosition.get(RandomGenerator.randomInt(availablePosition.size()));
	}
	
	private List<Position> getAvailablePosition(Game game) {
		
		List<Position> positions = new ArrayList<>();
		
		positions.addAll(getUpperLeftAvailableSquare(game));
		positions.addAll(getUpperRightAvailableSquare(game));
		positions.addAll(getBottomLeftAvailableSquare(game));
		positions.addAll(getBottomRightAvailableSquare(game));
		
		return positions;
	}
	
	private List<Position> getUpperLeftAvailableSquare(Game game) {
		
		List<Position> positions = new ArrayList<>();
		
		int startRow = 1;
		int startCol = 1;
		
		int endRow = startRow + 1;
		int endCol = startCol + 1;
		
		for (int row = endRow; row <= endRow; row++) {
			for (int col = startCol; col <= endCol; col++) {
				
				Position position = new Position(row, col);
				
				if (!positionService.isPositionOccupied(game.getId(), position)) {
					positions.add(position);
				}
			}
		}
		
		return positions;
	}
	
	private List<Position> getUpperRightAvailableSquare(Game game) {
		
		List<Position> positions = new ArrayList<>();
		
		int startRow = 1;
		int startCol = Constants.getGameboardRowCol(game) - 2;
		
		int endRow = startRow + 1;
		int endCol = startCol - 1;
		
		for (int row = startRow; row <= endRow; row++) {
			for (int col = startCol; col >= endCol; col--) {
				
				Position position = new Position(row, col);
				
				if (!positionService.isPositionOccupied(game.getId(), position)) {
					positions.add(position);
				}
			}
		}
		
		return positions;
	}
	
	private List<Position> getBottomLeftAvailableSquare(Game game) {
		
		List<Position> positions = new ArrayList<>();
		
		int startRow = Constants.getGameboardRowCol(game) - 2;
		int startCol = 1;
		
		int endRow = startRow - 1;
		int endCol = startCol + 1;
		
		for (int row = startRow; row >= endRow; row--) {
			for (int col = startCol; col <= endCol; col++) {
				
				Position position = new Position(row, col);
				
				if (!positionService.isPositionOccupied(game.getId(), position)) {
					positions.add(position);
				}
			}
		}
		
		return positions;
	}
	
	private List<Position> getBottomRightAvailableSquare(Game game) {
		
		List<Position> positions = new ArrayList<>();
		
		int startRow = Constants.getGameboardRowCol(game) - 2;
		int startCol = Constants.getGameboardRowCol(game) - 2;
		
		int endRow = startRow - 1;
		int endCol = startCol - 1;
		
		for (int row = startRow; row >= endRow; row--) {
			for (int col = startCol; col >= endCol; col--) {
				
				Position position = new Position(row, col);
				
				if (!positionService.isPositionOccupied(game.getId(), position)) {
					positions.add(position);
				}
			}
		}
		
		return positions;
	}
}
