package app;

import java.util.*;

public class SolveurSplit extends Solveur implements Cloneable {
    private Collection<Clause> clauses;
    int[] interpretation;
    
    protected SolveurSplit() {
        this(0);
    }
    
    public SolveurSplit(int nprops) {
        this(nprops, new int[nprops]);
    }

    private SolveurSplit(int nprops, int[] interpretation) {
        super(nprops);
        this.interpretation = interpretation;
        this.clauses = new HashSet<Clause>();
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
        return interpretation;
    }

    private static int index(int l) {
        return (l > 0)? l-1: -l-1;
    }
    
    @Override
    public SolveurSplit clone() {
        SolveurSplit clone = new SolveurSplit(nprops, interpretation.clone());
        for (Clause c : clauses)
            clone.add((Clause)c.clone());
        return clone;
    }
    
    private SolveurSplit simplifie(int l) {
        interpretation[index(l)] = l;
        clauses.removeIf(c -> c.simplifie(l));
        return this;
    }
    
    private boolean satisfiable(int i) {
        // l'ensemble vide de clauses est satisfiable
        if (clauses.size() == 0) {
            // interprétation arbitraire des propositions restantes
            for (int j = i; j < nprops; j++)
                interpretation[j] = j+1;
            return true;
        }
        // un clause vide est insatisfiable
        if (clauses.stream().anyMatch(c -> c.size() == 0))
            return false;
        // branchement :   
        SolveurSplit clone = this.clone();
        //  - tente de simplifier par le littéral Pᵢ
        if (this.simplifie(i+1).satisfiable(i+1))
            return true;
        // - restaure l'état du solveur
        this.interpretation = clone.interpretation;
        this.clauses = clone.clauses;
        //  - tente de simplifier par le littéral ¬Pᵢ
        return this.simplifie(-i-1).satisfiable(i+1);
    }    
    public boolean satisfiable() {
        return satisfiable(0);
    }
}
