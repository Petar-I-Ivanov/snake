package com.github.snake.api.encoder;

public interface IdEncoder {

  String encode(Long id);

  Long decode(String encodedId);
}
