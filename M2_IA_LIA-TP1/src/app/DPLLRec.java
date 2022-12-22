package app;
import java.util.*;

public class DPLLRec extends Solveur implements Cloneable {
    private Collection<Clause> clauses;
    int[] interpretation;

    protected DPLLRec() {
        this(0);
    }
    
    public DPLLRec(int nprops) {
        this(nprops, new int[nprops]);
    }

    private DPLLRec(int nprops, int[] interpretation) {
        super(nprops);
        this.interpretation = interpretation;
        this.clauses = new LinkedList<Clause>();//âš ï¸� ne marche pas avec HashSet
    }
    
    @Override
    protected void setNProps(int nprops) {
        super.setNProps(nprops);
        this.interpretation = new int[nprops];
    }
        
    public boolean add(Clause c) {
        return clauses.add(c);
    }

    public int[] modele () {
        for (int i = 0; i < nprops; i++)
            if (interpretation[i] == 0)
                interpretation[i] = i+1;
        return interpretation;
    }

    private static int index(int l) {
        return (l > 0)? l-1: -l-1;
    }

    @Override
    public DPLLRec clone() {
        DPLLRec clone = new DPLLRec(nprops, interpretation.clone());
        for (Clause c : clauses)
            clone.add((Clause)c.clone());
        return clone;
    }
    
    private DPLLRec simplifie(int l) {
        interpretation[index(l)] = l;
        clauses.removeIf(c -> c.simplifie(l));
        return this;
    }
    
    public boolean satisfiable() {
        // l'ensemble vide de clauses est satisfiable
        if (clauses.size() == 0)
            return true;
        // un clause vide est insatisfiable
        if (clauses.stream().anyMatch(c -> c.size() == 0))
            return false;
        // clause unitaire
        Optional<Clause> unitaire
            = clauses.stream().filter(c -> c.size() == 1).findAny();
        if (unitaire.isPresent()) {
            int l = unitaire.get().stream().findAny().get().intValue();
            return simplifie(l).satisfiable();
        }
        // littÃ©ral pur
        for (int i = 0; i < nprops; i++)
            if (interpretation[i] == 0) {
                final Integer l = Integer.valueOf(i+1);
                final Integer notl = Integer.valueOf(-i-1);
                if (clauses.stream().noneMatch(c ->
                        c.stream().anyMatch(j -> j.equals(notl))))
                    return simplifie(l.intValue()).satisfiable();
                if (clauses.stream().noneMatch(c ->
                        c.stream().anyMatch(j -> j.equals(l))))
                return simplifie(notl.intValue()).satisfiable();
            }
        // branchement
        for (int i = 0; i <nprops; i++)
            if (interpretation[i] == 0) {
                DPLLRec clone = this.clone();
                //  - tente de simplifier par le littÃ©ral Páµ¢
                if (this.simplifie(i+1).satisfiable())
                    return true;
                // - restaure l'Ã©tat du solveur
                this.interpretation = clone.interpretation;
                this.clauses = clone.clauses;
                //  - tente de simplifier par le littÃ©ral Â¬Páµ¢
                return this.simplifie(-i-1).satisfiable();
            }
        assert false;
        return false;
    }
}
