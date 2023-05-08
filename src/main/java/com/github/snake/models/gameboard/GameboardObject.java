package com.github.snake.models.gameboard;

import com.github.snake.utilities.Position;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@MappedSuperclass
public class GameboardObject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Column(length = 1, nullable = false)
  protected String sign;

  @Column(name = "row_location", nullable = false)
  protected byte rowLocation;

  @Column(name = "col_location", nullable = false)
  protected byte colLocation;

  protected GameboardObject(String sign) {
    this.sign = sign;
  }

  protected GameboardObject(String sign, Position location) {
    this.sign = sign;
    this.rowLocation = location.getRow();
    this.colLocation = location.getCol();
  }

  public Position getLocation() {
    return new Position(this.rowLocation, this.colLocation);
  }

  public void setLocation(Position location) {

    this.rowLocation = location.getRow();
    this.colLocation = location.getCol();
  }

  public byte getRowLocation() {
    return this.rowLocation;
  }

  public byte getColLocation() {
    return this.colLocation;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSign() {
    return this.sign;
  }
}
