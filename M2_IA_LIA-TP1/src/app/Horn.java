package app;

import java.util.*;
import java.lang.reflect.Array;

public class Horn extends Solveur {
	private Collection<HornClause> clauses;
	private int[] interpretation;

	protected Horn() {
		this(0);
	}

	public Horn(int nprops) {
		super(nprops);
		this.interpretation = new int[nprops];
		this.clauses = new HashSet<HornClause>();
	}

	@Override
	protected void setNProps(int nprops) {
		super.setNProps(nprops);
		this.interpretation = new int[nprops];
	}

	public boolean add(Clause c) throws IllegalArgumentException {
		return clauses.add(new HornClause(c));
	}

	public int[] modele() {
		return interpretation;
	}

	public boolean satisfiable() {
		@SuppressWarnings("unchecked")
		final Collection<HornClause>[] contient = (Collection<HornClause>[]) Array
				.newInstance((Class<LinkedList<HornClause>>) new LinkedList<HornClause>().getClass(), nprops);
		Collection<Integer> nouveaux = new LinkedList<Integer>(); // E' := Ø

		// initialisation
		for (int i = 0; i < nprops; i++) {
			// pour tout Q ∈ fp(F)
			interpretation[i] = -i - 1; // Q ∉ E
			contient[i] = new LinkedList<HornClause>(); // contient[Q] := null
		}
		for (HornClause c : clauses) {
			// pour tout C ∈ F
			if (c.tail().size() == 0) {
				if (c.head() == 0)
					return false;
				interpretation[c.head() - 1] = c.head(); // E := E ∪ {P}
				nouveaux.add(Integer.valueOf(c.head())); // E' := E' ∪ {P}
			} else
				for (Integer q : c.tail())
					// pour tout 1 ≤ i ≤ k
					contient[q.intValue() - 1].add(c); // contient[Qᵢ].add(C)
		}

		// boucle principale
		while (!nouveaux.isEmpty()) {
			// tant que E' ≠ Ø
			Collection<Integer> tmp = new LinkedList<Integer>(); // E" := Ø
			for (Integer q : nouveaux)
				// pour tout Q ∈ E'
				for (HornClause c : contient[q.intValue() - 1]) {
					// pour tout C ∈ contient[Q]
					int p = c.head();
					if ((p == 0 || interpretation[p - 1] < 0) && c.decrement()) {
						if (p == 0)
							return false;
						interpretation[p - 1] = p; // E := E ∪ {P}
						tmp.add(Integer.valueOf(p)); // E" := E" ∪ {P}
					}
				}
			nouveaux = tmp; // E' := E"
		}
		return true;
	}
}
