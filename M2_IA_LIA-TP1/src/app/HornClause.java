package app;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Une clause de Horn est de la forme P â‡� Qâ‚� âˆ§ Â·Â·Â· âˆ§ Qâ‚–. Elle est
 * reprÃ©sentÃ©e en format DIMACS (les littÃ©raux sont des entiers) en sÃ©parant
 * la tÃªte P (qui peut Ãªtre âŠ¥) de la queue {Qâ‚�,â€¦,Qâ‚–}.
 */
public class HornClause {
	/**
	 * La proposition P ou 0 pour âŠ¥ dans une clause nÃ©gative.
	 */
	private int head;
	/**
	 * L'ensemble de propositions {Qâ‚�, â€¦, Qâ‚–}.
	 */
	private Set<Integer> tail;
	/**
	 * Le nombre de proposition Ã  traiter dans la queue ; initialement k.
	 */
	private int count;

	public HornClause(Set<Integer> c) throws IllegalArgumentException {
		// vÃ©rifie que `c' est bien une clause de Horn
		if (c.stream().filter(l -> l.intValue() > 0).count() > 1)
			throw new IllegalArgumentException(c + " n'est pas une clause de Horn.");
		else {
			// `c' est bien une clause de Horn
			Optional<Integer> h = c.stream().filter(l -> l.intValue() > 0).findAny();
			if (h.isPresent())
				head = h.get().intValue();
			else
				head = 0;
		}
		// copie les propositions nÃ©gatives
		tail = c.stream().filter(l -> l.intValue() < 0).map(l -> -l.intValue()).collect(Collectors.toSet());
		count = tail.size();
	}

	public String toString() {
		String ret = "";
		if (head > 0)
			ret += head + " ";
		for (Integer i : tail)
			ret += (-i.intValue()) + " ";
		return ret + "0";
	}

	public boolean evalue(int[] interpretation) {
		return head > 0 || tail.stream().anyMatch(l -> interpretation[l.intValue() - 1] < 0);
	}

	public int head() {
		return head;
	}

	public Set<Integer> tail() {
		return tail;
	}

	/**
	 * DÃ©crÃ©mente le compte associÃ© Ã  la clause et retourne true si ce compte
	 * tombe Ã  0.
	 */
	protected boolean decrement() {
		count--;
		return count == 0;
	}
}
