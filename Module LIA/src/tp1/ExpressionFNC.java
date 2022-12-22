package tp1;

import java.util.*;

/**
 * @author Bouaziz Fortas
 *
 */
public class ExpressionFNC {
	private int cursor;
	private Priorité action;
	private int negativeOpCount;
	private char currentValue;
	private final String expression;
	private final Map<String, boolean[]> table;
	private final List<String> clauses;
	private final List<String> subExpressions;
	private final List<String> propositionClauses;
	private final Set<Character> propositions;
	private static final Map<Character, Priorité> symbolToActionMap = Map.ofEntries(Map.entry('!', Priorité.NOT),
			Map.entry('(', Priorité.LEFT_BRACKET), Map.entry(')', Priorité.RIGHT_BRACKET), Map.entry('|', Priorité.OR),
			Map.entry('&', Priorité.AND));

	public ExpressionFNC(String expression){
		this.table = new HashMap<>(expression.length());
		this.clauses = new ArrayList<>();
		this.subExpressions = new ArrayList<>();
		this.propositionClauses = new ArrayList<>();
		this.action = Priorité.START;
		this.propositions = new HashSet<>();

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < expression.length(); i++) {
			if (isCorrectChar(expression.charAt(i)))
				builder.append(Character.toUpperCase(expression.charAt(i)));
			}
		this.expression = builder.toString();
	}

	private static boolean isCorrectChar(char c) {
		return Character.isAlphabetic(c) || Character.isSpaceChar(c) || symbolToActionMap.containsKey(c);
	}

	private void readNextSymbol() {
		if (cursor >= expression.length())
			action = Priorité.END;
		else {
			while (Character.isSpaceChar(expression.charAt(cursor)))
				cursor += 1;
			char c = expression.charAt(cursor);
			if (Character.isAlphabetic(c)) {
				action = Priorité.OPEREND;
				currentValue = c;
			} else {
				action = symbolToActionMap.get(c);
			}
			cursor += 1;
		}
	}

	private void readNextProp()  {
		if (action == Priorité.OPEREND) {
			readNextSymbol();
			propositionClauses.add((negativeOpCount % 2 == 0 ? "" : "!") + currentValue);
			negativeOpCount = 0;
		} else if (action == Priorité.NOT) {
			readNextSymbol();
			negativeOpCount += 1;
			readNextProp();
		}
	}

	private void readNextClause() {
		readNextProp();
		if (action == Priorité.OR) {
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
		if (propositionClauses.isEmpty())
			return;
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
			for (int i = 0; i < n; i++)
				column[i] = false;
			for (int i = 0; i < n; i++) {
				for (String propClause : propositionClauses) {
					if (propClause.length() == 1)
						column[i] |= table.get(propClause)[i];
					else
						column[i] |= !table.get(propClause.substring(1))[i];
				}
			}
			table.put(clause, column);
		}
		propositionClauses.clear();
	}

	private void readNextExpression() {
		switch (action) {
		case LEFT_BRACKET:
			readNextSymbol();
			readNextClause();
			buildCurrentClause();

			if (action == Priorité.RIGHT_BRACKET) {
				readNextSymbol();
				if (action == Priorité.AND) {
					readNextSymbol();
					readNextExpression();
				}
			}

		case OPEREND:
			readNextClause();
			buildCurrentClause();
			if (action == Priorité.AND) {
				readNextSymbol();
				readNextExpression();
			}

		case NOT:
			readNextClause();
			buildCurrentClause();
			if (action == Priorité.AND) {
				readNextSymbol();
				readNextExpression();
			}

		case END:
			break;
		default:
			break;
		}
	}

	private void buildAll() {
		if (subExpressions.isEmpty())
			return;
		StringBuilder builder = new StringBuilder();
		for (String clause : clauses) {
			if (!clause.equals(clauses.get(0)))
				builder.append('&');

			if (clause.length() > 2)
				builder.append('(').append(clause).append(')');
			else
				builder.append(clause);
		}
		String expr = builder.toString();
		if (addSubExpression(expr)) {
			int n = 1 << propositions.size();
			boolean[] column = new boolean[n];
			for (int i = 0; i < n; i++)
				column[i] = true;
			for (int i = 0; i < n; i++) {
				for (String clause : clauses) {
					column[i] &= table.get(clause)[i];
				}
			}
			table.put(expr, column);
		}
	}

	private char booleanVal(boolean val) {
			return val ? '1' : '0';
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("| ");
		for (String se : subExpressions) {
			builder.append(se).append(" | ");
		}
		builder.append("\n");

		for (int n = 1 << propositions.size(), i = 0; i < n; i++) {
			builder.append("\n|");
			for (String se : subExpressions) {
				builder.append(" ").append(booleanVal(table.get(se)[i])).append(" |");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	private void makePropList() {
		int n = expression.length();
		for (int i = 0; i < n; i++) {
			if (Character.isAlphabetic(expression.charAt(i)))
				propositions.add(expression.charAt(i));
		}

		n = 1 << propositions.size();
		int i = n;
		for (char c : propositions) {
			i /= 2;
			String s = String.valueOf(c);
			addSubExpression(s);
			boolean[] column = new boolean[n];
			boolean flag = true;
			for (int j = 0; j < n;) {
				for (int k = 0; k < i; k++, j++)
					column[j] = flag;
				flag = !flag;
			}
			table.put(s, column);
		}
	}

	public void resolve() {
		makePropList();
		readNextSymbol();
		readNextExpression();
		buildAll();
	}

	public String getExpression() {
		return expression;
	}
}
