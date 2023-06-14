package com.github.snake.encoder;

import jakarta.inject.Singleton;
import java.math.BigInteger;
import java.util.Base64;

@Singleton
public class Base64IdEncoder implements IdEncoder {

  private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
  private final Base64.Decoder decoder = Base64.getUrlDecoder();

  @Override
  public String encode(Long id) {

    byte[] idInByteArray = BigInteger.valueOf(id).toByteArray();
    return encoder.encodeToString(idInByteArray);
  }

  @Override
  public Long decode(String encodedId) {

    byte[] idInByteArray = decoder.decode(encodedId);
    return new BigInteger(idInByteArray).longValueExact();
  }
}
