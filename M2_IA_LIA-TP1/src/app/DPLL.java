package app;
import java.util.*;

public class DPLL extends Solveur implements Cloneable {
    private HashSet<Integer> interpretation;
    private Map<Integer,Collection<Clause>> occurrences;
    private HashSet<Integer> unitaires;
    private Collection<Integer> purs;
    private int nclauses;

    protected DPLL () {
        this(0);
    }
    
    public DPLL (int nprops) {
        super(nprops);
        this.nclauses = 0;
        this.interpretation = new HashSet<Integer>();
        this.unitaires = new HashSet<Integer>();
        this.setNProps(nprops);
    }

    @Override
    protected void setNProps (int nprops) {
        this.nprops = nprops;
        this.purs = new HashSet<Integer>(nprops*2);
        this.occurrences = new HashMap<Integer,Collection<Clause>>(nprops*2);
        for (int j = 1; j <= nprops; j++) {
            Integer i = Integer.valueOf(j);
            occurrences.put(i, new LinkedList<Clause>());
            occurrences.put(neg(i), new LinkedList<Clause>());
            if (!interpretation.contains(i) && !interpretation.contains(neg(i))) {
                purs.add(i);
                purs.add(neg(i));
            }
        }
    }

    @Override
    public DPLL clone() {
        DPLL clone = new DPLL(nprops);
        clone.interpretation = (HashSet<Integer>) interpretation.clone();
        clone.setNProps(nprops);
        Collection<Clause> clauses = getClauses();
        for (Clause c : getClauses())
            clone.add((Clause)c.clone());
        assert clone.getClauses().containsAll(clauses);
        assert clauses.containsAll(clone.getClauses());
        return clone;
    }

    protected boolean addUnsafe(Clause c) throws UnsatisfiedLinkError {
        if (c.size() == 0)
            throw new UnsatisfiedLinkError();
        return add(c);
    }

    public boolean add(Clause c) {
        nclauses++;
        if (c.size() == 1)
            unitaires.add(c.iterator().next());

        for (Integer i : c) {
            occurrences.get(i).add(c);
            if (purs.contains(neg(i)))
                purs.remove(neg(i));
        }
        return true;
    }        

    protected static Integer neg(Integer i) {
        int j = i.intValue();
        assert j != 0; 
        return Integer.valueOf(-j);
    }
    
    public int[] modele() {
        int[] modele = new int[nprops];
        for (int j = 1; j <= nprops; j++) {
            if (interpretation.contains(Integer.valueOf(j)))
                modele[j-1] = j;
            else
                // valeur par défaut : ⊥
                modele[j-1] = -j;
        }
        return modele;
        // version où interpretation est une LinkedList<Integer>
        // interpretation.sort(new Comparator<Integer> () {
        //     public int compare(Integer i1, Integer i2) {
        //         int j1 = i1.intValue();
        //         int j2 = i2.intValue();
        //         return (j1 > 0? j1: -j1) - (j2 > 0? j2: -j2);
        //     }});
        // return interpretation.stream().mapToInt(i->i).toArray();
    }

    public boolean simplifie(Integer i) {
        // assert !interpretation.contains(i);
        interpretation.add(i);
        for (Clause c : occurrences.get(i)) {
            nclauses--;
            for (Integer j : c) {
                Collection<Clause> joccs = occurrences.get(j);
                if (!i.equals(j)) // évite ConcurrentModificationException
                    joccs.remove(c);
                if (joccs.isEmpty())
                    purs.add(neg(j));
            }
        }
        occurrences.put(i, new LinkedList<Clause>());
        purs.remove(i);
        for (Clause c : occurrences.get(neg(i))) {
            c.remove(neg(i));
            if (c.size() == 0)
                return false;
            if (c.size() == 1)
                unitaires.add(c.iterator().next());
        }
        occurrences.put(neg(i), new LinkedList<Clause>());
        purs.remove(neg(i));
        return true;
    }

    public boolean satisfiable() {
        // l'ensemble vide de clauses est satisfiable
        if (nclauses == 0)
            return true;
        while ((!unitaires.isEmpty() || !purs.isEmpty())
               && nclauses > 0) {
            // clauses unitaires
            while (!unitaires.isEmpty() && nclauses > 0) {
                Integer i = unitaires.iterator().next();
                unitaires.remove(i);
                if (!simplifie(i))
                    return false;
            }
            // litteraux purs
            if (!purs.isEmpty() && nclauses > 0) {   
                Integer i = purs.iterator().next();
                assert occurrences.get(neg(i)).isEmpty();
                purs.remove(i);
                if (!simplifie(i))
                    return false;
            }
        }
        if (nclauses == 0)
            return true;
        // branchement
        DPLL clone = this.clone();
        assert clone.unitaires.isEmpty() && clone.purs.isEmpty();
        for (Integer i : occurrences.keySet()) {
            if (occurrences.get(i).size() > 0) {
                assert occurrences.get(neg(i)).size() > 0;
                //  - tente de simplifier par le littéral Pᵢ
                if (simplifie(i) && satisfiable())
                    return true;
                //  - tente de simplifier par le littéral ¬Pᵢ
                if (clone.simplifie(neg(i)) && clone.satisfiable()) {
                    this.interpretation = clone.interpretation;
                    return true;
                }
                return false;
            }
        }
        assert false;
        return false;
    }

    public Collection<Clause> getClauses() {
        Collection<Clause> ret = new HashSet<Clause>(nclauses);
        for (Integer i : occurrences.keySet()) {
            ret.addAll(occurrences.get(i));
        }
        return ret;
    }

    public String toString() {
        String ret = "    nclauses = "+ nclauses;
        ret += "\n    I         = "+ interpretation;
        ret += "\n    purs      = "+ purs;
        ret += "\n    unitaires = "+ unitaires;
        ret += "\n    clauses   = {";
        for (Integer i : occurrences.keySet())
            if (occurrences.get(i).size() != 0)
                ret += "\n                  "+ i +" = "+ occurrences.get(i);
        return ret;
    }
}
