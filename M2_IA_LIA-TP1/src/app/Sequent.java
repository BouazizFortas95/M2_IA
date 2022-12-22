package app;

import java.util.*;

public class Sequent implements Cloneable {
    protected HashMap<String,Boolean> litteraux;
    protected LinkedList<Formule.Et> ets;
    protected LinkedList<Formule.Ou> ous;

    public Sequent() {
        this.litteraux  = new HashMap<String,Boolean>();
        this.ets        = new LinkedList<Formule.Et>();
        this.ous        = new LinkedList<Formule.Ou>();
    }

    @Override
    public Sequent clone() {
        Sequent clone   = new Sequent();
        clone.litteraux = (HashMap<String,Boolean>)this.litteraux.clone();
        clone.ets       = (LinkedList<Formule.Et>) this.ets.clone();
        clone.ous       = (LinkedList<Formule.Ou>) this.ous.clone();
        return clone;
    }

    // retourne faux si le séquent devient une instance de la règle d'axiome
    public boolean add(Formule phi) {
        return phi.addToSequent(this);
    }
    
    public boolean prouvable() {
        Queue<Sequent> file = new LinkedList<Sequent>(); // file d'attente
        file.add(this);
        // recherche de preuve
        while (!file.isEmpty()) {
            // le séquent à prouver
            Sequent seq = file.poll();
            // cas d'une branche d'échec
            if (seq.ous.isEmpty() && seq.ets.isEmpty())
                return false;
            // choix d'une formule principale
            Formule.Ou phi = seq.ous.poll();
            if (phi != null) {
                // si la règle d'axiome ne s'applique pas, ajout à la file
                if (seq.add(phi.phi1) && seq.add(phi.phi2))
                    file.add(seq);
            }
            else {
                Formule.Et psi = seq.ets.poll();
                Sequent clone  = seq.clone();
                // si la règle d'axiome ne s'applique pas, ajout à la file
                if (seq.add(psi.phi1))
                    file.add(seq);
                // si la règle d'axiome ne s'applique pas, ajout à la file
                if (clone.add(psi.phi2))
                    file.add(clone);
            }
        }
        return true;
    }

    // affichage
    public String toString() {
        String ret = " {";
        for (String nom : litteraux.keySet()) {
            if (!litteraux.get(nom).booleanValue())
                ret += "¬";
            ret += nom +", ";
        }
        for (Formule phi : ous)
            ret += phi + ", ";
        for (Formule phi : ets)
            ret += phi + ", ";
        ret = ret.substring(0, ret.length()-1);
        return ret +"} ";
    }    
}
