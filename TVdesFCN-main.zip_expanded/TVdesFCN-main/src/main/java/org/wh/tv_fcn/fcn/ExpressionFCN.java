package org.wh.tv_fcn.fcn;

import java.util.*;

public class ExpressionFCN {
  private int cursor;
  private Action action;
  private int negativeOpCount;
  private char currentValue;
  private final String expression;
  private final Map<String, boolean[]> table;
  private final List<String> clauses;
  private final List<String> subExpressions;
  private final List<String> propositionClauses;
  private final Set<Character> propositions;
  private static final Map<Character, Action> symbolToActionMap = Map.ofEntries(
      Map.entry('!', Action.NEGATIVE_OP),
      Map.entry('(', Action.LEFT_PARENTHESIS),
      Map.entry(')', Action.RIGHT_PARENTHESIS),
      Map.entry('|', Action.DISJUNCTION_OP),
      Map.entry('&', Action.CONJUNCTION_OP)
  );

  public ExpressionFCN(String expression) throws FCNException {
    this.table = new HashMap<>(expression.length());
    this.clauses = new ArrayList<>();
    this.subExpressions = new ArrayList<>();
    this.propositionClauses = new ArrayList<>();
    this.action = Action.START;
    this.propositions = new HashSet<>();

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < expression.length(); i++) {
      if (isCorrectChar(expression.charAt(i)))
        builder.append(Character.toUpperCase(expression.charAt(i)));
      else
        throw new FCNException(ExpressionError.ILLEGAL_CHARACTER, i);
    }
    this.expression = builder.toString();
  }

  private static boolean isCorrectChar(char c) {
    return Character.isAlphabetic(c) || Character.isSpaceChar(c) || symbolToActionMap.containsKey(c);
  }

  private void readNextSymbol() {
    if (cursor >= expression.length()) action = Action.END;
    else {
      while (Character.isSpaceChar(expression.charAt(cursor))) cursor += 1;
      char c = expression.charAt(cursor);
      if (Character.isAlphabetic(c)) {
        action = Action.PROPOSITION;
        currentValue = c;
      } else {
        action = symbolToActionMap.get(c);
      }
      cursor += 1;
    }
  }

  private void readNextProp() throws FCNException {
    if (action == Action.PROPOSITION) {
      readNextSymbol();
      propositionClauses.add((negativeOpCount % 2 == 0 ? "" : "!") + currentValue);
      negativeOpCount = 0;
    } else if (action == Action.NEGATIVE_OP) {
      readNextSymbol();
      negativeOpCount += 1;
      readNextProp();
    } else {
      throw new FCNException(ExpressionError.EXPRESSION_MALFORMED, cursor);
    }
  }

  private void readNextClause() throws FCNException {
    readNextProp();
    if (action == Action.DISJUNCTION_OP) {
      readNextSymbol();
      readNextClause();
    }
  }

  private void addClause(String clause) {
    for (String c : clauses) {
      if (c.equals(clause))
        return;
    }
    clauses.add(clause);
  }

  private boolean addSubExpression(String expression) {
    for (String e : subExpressions) {
      if (e.equals(expression))
        return false;
    }
    subExpressions.add(expression);
    return true;
  }

  private void buildCurrentClause() {
    if (propositionClauses.isEmpty()) return;
    StringBuilder builder = new StringBuilder();
    for (String s : propositionClauses) {
      if (!s.equals(propositionClauses.get(0))) {
        builder.append("|");
      }
      builder.append(s);
    }
    String clause = builder.toString();
    addClause(clause);
    if (addSubExpression(clause)) {
      int n = 1 << propositions.size();
      boolean[] column = new boolean[n];
      for (int i = 0; i < n; i++) column[i] = false;
      for (int i = 0; i < n; i++) {
        for (String propClause : propositionClauses) {
          if (propClause.length() == 1) column[i] |= table.get(propClause)[i];
          else column[i] |= !table.get(propClause.substring(1))[i];
        }
      }
      table.put(clause, column);
    }
    propositionClauses.clear();
  }

  private void readNextExpression() throws FCNException {
    switch (action) {
      case LEFT_PARENTHESIS -> {
        readNextSymbol();
        readNextClause();
        buildCurrentClause();

        if (action != Action.RIGHT_PARENTHESIS) {
          throw new FCNException(ExpressionError.NO_CLOSING_PARENTHESIS, cursor);
        } else {
          readNextSymbol();
          if (action == Action.CONJUNCTION_OP) {
            readNextSymbol();
            readNextExpression();
          } else if (action != Action.END) {
            throw new FCNException(ExpressionError.NO_CONJUNCTION, cursor);
          }
        }
      }

      case PROPOSITION, NEGATIVE_OP -> {
        readNextClause();
        buildCurrentClause();
        if (action == Action.CONJUNCTION_OP) {
          readNextSymbol();
          readNextExpression();
        } else if (action != Action.END) {
          throw new FCNException(ExpressionError.NO_CONJUNCTION, cursor);
        }
      }

      case END -> {
      }

      default -> throw new FCNException(ExpressionError.EXPRESSION_MALFORMED, cursor);
    }
  }

  private void buildAll() {
    if (subExpressions.isEmpty()) return;
    StringBuilder builder = new StringBuilder();
    for (String clause : clauses) {
      if (!clause.equals(clauses.get(0))) builder.append('&');

      if (clause.length() > 2) builder.append('(').append(clause).append(')');
      else builder.append(clause);
    }
    String expr = builder.toString();
    if (addSubExpression(expr)) {
      int n = 1 << propositions.size();
      boolean[] column = new boolean[n];
      for (int i = 0; i < n; i++) column[i] = true;
      for (int i = 0; i < n; i++) {
        for (String clause : clauses) {
          column[i] &= table.get(clause)[i];
        }
      }
      table.put(expr, column);
    }
  }

  private char booleanStr(boolean b, DisplayMode displayMode) {
    if (displayMode == DisplayMode.NUMBERS) return b ? '1' : '0';
    return b ? 'V' : 'F';
  }

  public String toHTML(DisplayMode displayMode) {
    StringBuilder builder = new StringBuilder();
    builder.append("<table>\n<tr>");
    for (String se : subExpressions) {
      builder.append("<th>").append(se).append("</th>");
    }
    builder.append("</tr>");

    for (int n = 1 << propositions.size(), i = 0; i < n; i++) {
      builder.append("\n<tr>");
      for (String se : subExpressions) {
        builder
            .append("<td>")
            .append(booleanStr(table.get(se)[i], displayMode))
            .append("</td>");
      }
      builder.append("</tr>");
    }
    builder.append("</table>");
    return builder.toString();
  }

  private void makePropList() throws FCNException {
    int n = expression.length();
    for (int i = 0; i < n; i++) {
      if (Character.isAlphabetic(expression.charAt(i)))
        propositions.add(expression.charAt(i));
    }
    if (propositions.isEmpty()) {
      throw new FCNException(ExpressionError.EXPRESSION_MALFORMED);
    }
    n = 1 << propositions.size();
    int i = n;
    for (char c : propositions) {
      i /= 2;
      String s = String.valueOf(c);
      addSubExpression(s);
      boolean[] column = new boolean[n];
      boolean flag = true;
      for (int j = 0; j < n; ) {
        for (int k = 0; k < i; k++, j++) column[j] = flag;
        flag = !flag;
      }
      table.put(s, column);
    }
  }

  public void resolve() throws FCNException {
    makePropList();
    readNextSymbol();
    readNextExpression();
    buildAll();
  }

  public static void main(String[] args) {
    try {
      ExpressionFCN expressionFCN = new ExpressionFCN("a&b");
      expressionFCN.resolve();
      System.out.println(expressionFCN.toHTML(DisplayMode.TRUE_FALSE));
    } catch (FCNException e) {
      e.printStackTrace();
    }
  }

  public String getExpression() {
    return expression;
  }
}
