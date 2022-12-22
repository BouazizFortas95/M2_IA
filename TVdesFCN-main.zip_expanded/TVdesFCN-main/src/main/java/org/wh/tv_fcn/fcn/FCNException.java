package org.wh.tv_fcn.fcn;

public class FCNException extends Exception {
  private final int position;
  private final ExpressionError type;

  public FCNException(ExpressionError type, int position) {
    this.type = type;
    this.position = position;
  }

  public FCNException(ExpressionError type) {
    this.type = type;
    this.position = -1;
  }

  public ExpressionError getType() {
    return type;
  }

  public int getPosition() {
    return position;
  }
}
