package com.github.snake.dto;

import com.github.snake.models.GameStatusEnum;
import lombok.Data;

@Data
public class GameDTO {

  private long id;
  private GameStatusEnum status;
  private short turn;
  private String[][] map;
}
