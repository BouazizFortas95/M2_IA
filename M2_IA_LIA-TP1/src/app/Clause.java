package app;
import java.util.*;

public class Clause extends HashSet<Integer> {
    public Clause () {
        super();
    }

    public int[] toIntArray() {
        return this.stream().mapToInt(i->i).toArray();
    }
    
    public String toString() {
        String ret = "";
        for (Integer i : this)
            ret += i + " ";
        return ret + "0";
    }
    
    public boolean add(int n) {
        return this.add(Integer.valueOf(n));
    }
    
    public boolean evalue(int[] interpretation) {
        return this.stream().anyMatch(
            l -> interpretation[(l.intValue()>0)
                                ?l.intValue()-1
                                :(-l.intValue())-1]
                 == l.intValue());
    }

    public boolean simplifie(int l) {      
        if (this.contains(Integer.valueOf(l)))
            return true;
        this.remove(Integer.valueOf(-l));
        return false;
    }
}
