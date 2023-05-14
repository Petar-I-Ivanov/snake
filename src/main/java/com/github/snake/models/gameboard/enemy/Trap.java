package com.github.snake.models.gameboard.enemy;

import com.github.snake.utilities.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Trap extends Enemy {
	
	@Column(name = "turn_placed", nullable = false)
	private short turnPlaced;

  public Trap() {
    super(Constants.TRAP_SIGN);
    this.turnPlaced = 0;
  }
}
