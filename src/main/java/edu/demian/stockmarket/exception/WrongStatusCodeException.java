package edu.demian.stockmarket.exception;

public class WrongStatusCodeException extends RuntimeException {

  public WrongStatusCodeException(String message) {
    super(message);
  }
}
