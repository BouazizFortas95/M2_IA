package app;

import java.util.*;

public class SolveurNaif extends Solveur {
    private Collection<Clause> clauses;
    private int[] interpretation;
    
    protected SolveurNaif() {
        this(0);
    }
    
    public SolveurNaif(int nprops) {
        super(nprops);
        this.interpretation = new int[nprops];
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

    private boolean satisfiable(int i) {
        // interprétation de toutes les propositions
        if (i == nprops)
            return clauses.stream().allMatch(c -> c.evalue(interpretation));
        else {
            // branchement :
            //  - tente de mettre la ième proposition à 1
            if (satisfiable(i+1))
                return true;
            // - restaure l'interprétation
            interpretation[i] = -i-1;
            for (int j = i+1; j < nprops; j++)
                interpretation[j] = j+1;
            //  - tente de mettre la ième proposition à 0
            return satisfiable(i+1);
        }
    }
    
    public boolean satisfiable() {
        // interprétation initiale constante à 1
        for (int i = 0; i < nprops; i++)
            interpretation[i] = i+1;
        return satisfiable(0);
    }
}
