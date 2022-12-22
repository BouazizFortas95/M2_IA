package app;
// nécessite Sat4j : http://download.forge.ow2.org/sat4j/
import java.util.*;
import org.sat4j.minisat.*;
import org.sat4j.specs.*;
import org.sat4j.core.*;

public class DIMACS extends Solveur {
    private Map<String,Integer> propnames; // nom de proposition → entier
    private Collection<Clause> clauses;    // ensemble de clauses
    private int[] interpretation;
    
    protected DIMACS () {
        this(0);
    }
    
    protected DIMACS (int nprops) {
        this(nprops, null);
    }
    
    public DIMACS (int nprops, Map<String,Integer> propnames) {
        super(nprops);
        this.propnames = propnames;
        this.clauses = new HashSet<Clause>();
    }

    public boolean add(Clause c) {
        return clauses.add(c);
    }
     
    public String toString() {
        String ret = "";
        if (propnames != null) {
            // commentaires : table des numéros de propositions
            ret += "c table des propositions\nc   ";
            for (Map.Entry<String,Integer> e : propnames.entrySet()) {
                String sid = e.getValue().toString();            
                ret += e.getKey();
                for (int i = 0; i < sid.length() - e.getKey().length(); i++)
                    ret += " ";
                ret += " ";
            }
            ret += "\nc   ";
            for (Map.Entry<String,Integer> e : propnames.entrySet()) {
                String sid = e.getValue().toString();
                ret += sid;
                for (int i = 0; i < e.getKey().length() - sid.length(); i++)
                    ret += " ";
                ret += " ";
            }
        }
        // en-tête avec les nombres de propositions et de clauses
        ret += "\np cnf "+ nprops +" "+ clauses.size() + "\n";
        // ajout des clauses au format DIMACS
        for (Clause c : clauses)
            ret += c + "\n";
        return ret;
    }

    public int[] modele() {
        return interpretation;
    }

    public String modeleAsString() {
        String ret = "[";
        for (Map.Entry<String,Integer> e : propnames.entrySet()) {
            ret += (interpretation[Integer.valueOf(e.getValue())-1] > 0)? "1": "0";
            ret += "/"+ e.getKey() +", ";
        }
        return ret.substring(0, ret.length() - 2) + "]";
    }
    
    /**
     * Teste si la formule (sous forme clausale) au format DIMACS est
     * satisfiable en appelant Sat4J.
     */
    public boolean satisfiable() {
        ISolver solver = SolverFactory.newDefault();
        // initialisation du solver
        solver.newVar(nprops);
        solver.setExpectedNumberOfClauses(clauses.size());
        try {
            for (Clause c : clauses) { // ajout des clauses
                solver.addClause(new VecInt(c.toIntArray()));
            }
            IProblem problem = solver;
            boolean ret = problem.isSatisfiable();
            if (ret)
                interpretation = problem.model();
            return ret;
        } catch (ContradictionException e) {
            return false;
        } catch (TimeoutException e) {
            System.err.println("timeout!");
            return false;
        }
    }
}
