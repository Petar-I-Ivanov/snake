package com.github.snake.encoder;

public interface IdEncoder {

  String encode(Long id);

  Long decode(String encodedId);
}
