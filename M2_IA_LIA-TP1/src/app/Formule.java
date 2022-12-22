package app;
import java.util.*;

public abstract class Formule implements Cloneable {
    // identifiant de la formule pour la mise sous forme clausale Ã©qui-satisfiable
    protected int id = 0;
    
    // methodes abstraites
    public abstract String toString();
    public abstract boolean evalue(Map<String,Boolean> interpretation);
    public abstract Formule substitue(Map<String,Formule> s);
    public abstract Formule clone();
    public abstract Formule getNNF();
    public abstract Formule getDualNNF();
    public abstract Formule getCNF();
    protected abstract Formule getCNF1(Formule psi);  
    protected abstract int setid(int n, Map<String,Integer> p);
    protected abstract void addSousClauses(DIMACS clauses);
    protected abstract boolean addToSequent(Sequent seq);

    // mÃ©thodes concrÃ¨tes
    protected int getid() {
        if (id == 0)
            setid(0, new HashMap<String,Integer>());
        return id;
    }    
    
    public DIMACS getDIMACS () {
        // passage sous forme normale nÃ©gative
        Formule nnf = this.getNNF();
        // table qui associera un entier > 0 Ã  chaque proposition
        Map<String,Integer> propnames = new HashMap<String,Integer>();
        // chaque nÅ“ud de `nnf' reÃ§oit un entier non nul
        int nprops = nnf.setid(0, propnames);
        // ensemble initialement vide de clauses
        DIMACS dimacs = new DIMACS(nprops, propnames);
        // ajout des clauses (Q_Ï†' â‡’ Ïˆ_Ï†') pour chaque sous-formule Ï†'
        nnf.addSousClauses(dimacs);
        // ajout de la clause Q_Ï†
        Clause c = new Clause();
        c.add(nnf.getid());
        dimacs.add(c);
        return dimacs;
    }
    
    public boolean valide() {
        Sequent seq = new Sequent();
        seq.add(this.getNNF());
        return seq.prouvable();
    }
    
    // sous-classes
    public static class Et extends Formule {
        protected Formule phi1;
        protected Formule phi2;
        public Et (Formule phi1, Formule phi2) {
            this.phi1 = phi1;
            this.phi2 = phi2;
        }
        public String toString() {
            return "(" + phi1 +  " âˆ§ " + phi2 + ")";
        }
        public int setid(int n, Map<String,Integer> p) {
            this.id = n+1;
            int m = phi1.setid(n+1, p);
            return phi2.setid(m, p);
        }
        public boolean evalue(Map<String,Boolean> interpretation) {
            return phi1.evalue(interpretation)
                && phi2.evalue(interpretation);
        }
        public Formule substitue(Map<String,Formule> s) {
            return new Et(phi1.substitue(s), phi2.substitue(s));
        }
        public Et clone() {
            return new Et(phi1.clone(), phi2.clone());
        }
        public Formule getNNF() {
            return new Et(phi1.getNNF(), phi2.getNNF());
        }
        public Formule getDualNNF() {
            return new Ou(phi1.getDualNNF(), phi2.getDualNNF());
        }
        public Formule getCNF() {
            // cnf(Ï†â‚� âˆ§ Ï†â‚‚) = cnf(Ï†â‚�) âˆ§ cnf(Ï†â‚‚)
            return new Et(phi1.getCNF(), phi2.getCNF());
        }
        // cas de ((Ï†â‚� âˆ§ Ï†â‚‚) âˆ¨ Ïˆ) oÃ¹ this = (Ï†â‚� âˆ§ Ï†â‚‚) et Ïˆ est dÃ©jÃ  en CNF
        protected Formule getCNF1(Formule psi) {
            // application de la distributivitÃ© :
            // (Ï†â‚� âˆ§ Ï†â‚‚) âˆ¨ Ïˆ  â‡�  (Ï†â‚� âˆ¨ Ïˆ) âˆ§ (Ï†â‚‚ âˆ¨ Ïˆ)
            return new Et(new Ou(phi1, psi).getCNF(),
                          new Ou(phi2, psi).getCNF());
        }
        protected void addSousClauses(DIMACS clauses) {
            // Ï†'= Ï†â‚� âˆ§ Ï†â‚‚
            phi1.addSousClauses(clauses);
            phi2.addSousClauses(clauses);
            // clause Â¬Q_Ï†' âˆ¨ Q_Ï†â‚�
            Clause c1 = new Clause();
            c1.add(-this.getid());
            c1.add(phi1.getid());
            // clause Â¬Q_Ï†' âˆ¨ Q_Ï†â‚‚
            Clause c2 = new Clause();
            c2.add(-this.getid());
            c2.add(phi2.getid());
            // ajout des nouvelles clauses
            clauses.add(c1);
            clauses.add(c2);
        }
        protected boolean addToSequent(Sequent seq) {
            seq.ets.add(this);
            return true;
        }
    }
    public static class Ou extends Formule {
        protected Formule phi1;
        protected Formule phi2;
        public Ou (Formule phi1, Formule phi2) {
            this.phi1 = phi1;
            this.phi2 = phi2;
        }
        public String toString() {
            return "(" + phi1 +  " âˆ¨ " + phi2 + ")";
        }
        public int setid(int n, Map<String,Integer> p) {
            this.id = n+1;
            int m = phi1.setid(n+1, p);
            return phi2.setid(m, p);
        }
        public boolean evalue(Map<String,Boolean> interpretation) {
            return phi1.evalue(interpretation)
                || phi2.evalue(interpretation);
        }
        public Formule substitue(Map<String,Formule> s) {
            return new Ou(phi1.substitue(s), phi2.substitue(s));
        }
        public Ou clone() {
            return new Ou(phi1.clone(), phi2.clone());
        }
        public Formule getNNF() {
            return new Ou(phi1.getNNF(), phi2.getNNF());
        }
        public Formule getDualNNF() {
            return new Et(phi1.getDualNNF(), phi2.getDualNNF());
        }
        public Formule getCNF() {
            // on lance une analyse de cas :
            // selon que Ï†â‚� soit une conjonction, une disjonction ou un littÃ©ral,
            // cnf(Ï†â‚� âˆ¨ Ï†â‚‚) va Ãªtre traitÃ© diffÃ©remment
            return phi1.getCNF1(phi2.getCNF());
        }
        // cas de ((Ï†â‚� âˆ¨ Ï†â‚‚) âˆ¨ Ïˆ) oÃ¹ this = (Ï†â‚� âˆ¨ Ï†â‚‚) et Ïˆ est dÃ©jÃ  en CNF
        public Formule getCNF1(Formule psi) {
            // on calcule Ï† = cnf(Ï†â‚� âˆ¨ Ï†â‚‚)
            Formule phi = this.getCNF();
            // on se retrouve avec la formule Ï† âˆ¨ Ïˆ oÃ¹ Ï† et Ïˆ sont dÃ©jÃ  en CNF
            // si Ï† est une conjonction : il faut appliquer la distributivitÃ©
            if (phi.getClass() == Formule.Et.class)
                return phi.getCNF1(psi);
            // si Ïˆ est une conjonction : il faut appliquer la distributivitÃ©
            else if (psi.getClass() == Formule.Et.class)
                return psi.getCNF1(phi);
            // Ï† et Ïˆ sont des disjonctions en CNF ou des littÃ©raux
            // et alors cnf(Ï† âˆ¨ Ïˆ) = Ï† âˆ¨ Ïˆ
            else
                return new Ou(phi, psi);
        }
        protected void addSousClauses(DIMACS clauses) {
            // Ï†'= Ï†â‚� âˆ¨ Ï†â‚‚
            phi1.addSousClauses(clauses);
            phi2.addSousClauses(clauses);
            // clause Â¬Q_Ï†' âˆ¨ Q_Ï†â‚� âˆ¨ Q_Ï†â‚‚
            Clause c = new Clause();
            c.add(-this.getid());
            c.add(phi1.getid());
            c.add(phi2.getid());
            // ajout de la nouvelle clause
            clauses.add(c);
        }
        protected boolean addToSequent(Sequent seq) {
            seq.ous.add(this);
            return true;
        }
    }
    public static class Non extends Formule {
        private Formule phi1;
        public Non (Formule phi1) {
            this.phi1 = phi1;
        }
        public String toString() {
            return "Â¬" + phi1;
        }
        public int setid(int n, Map<String,Integer> p) {
            // vÃ©rifie que nous sommes en forme normale nÃ©gative
            assert (phi1.getClass() == Formule.Proposition.class);
            String nom = ((Formule.Proposition)phi1).nom;
            // pour un littÃ©ral nÃ©gatif, son identifiant est la nÃ©gation
            // de l'entier associÃ© Ã  sa proposition
            if (p.containsKey(nom)) {
                int k = p.get(nom).intValue();
                this.id = -k;
                return n;
            }
            else {
                p.put(nom, n+1);
                this.id = -n-1;
                return n+1;
            }
        }
        public boolean evalue(Map<String,Boolean> interpretation) {
            return !phi1.evalue(interpretation);
        }
        public Formule substitue(Map<String,Formule> s) {
            return new Non(phi1.substitue(s));
        }
        public Non clone() {
            return new Non(phi1.clone());
        }
        public Formule getNNF() {
            return phi1.getDualNNF();
        }
        public Formule getDualNNF() {
            return phi1.getNNF();
        }
        public Formule getCNF() {
            // vÃ©rifie que nous sommes en forme normale nÃ©gative
            assert (phi1.getClass() == Formule.Proposition.class);
            // cnf(Â¬P) = Â¬P
            return this.clone();
        }
        // cas d'une formule (Â¬P âˆ¨ Ïˆ) oÃ¹ this = Â¬P et Ïˆ est dÃ©jÃ  en CNF
        protected Formule getCNF1(Formule psi) {
            // vÃ©rifie que nous sommes en forme normale nÃ©gative
            assert (phi1.getClass() == Formule.Proposition.class);
            // si Ïˆ est une conjonction : il faut appliquer la distributivitÃ©
            if (psi.getClass() == Formule.Et.class)
                return psi.getCNF1(this.clone());
            // sinon Ïˆ est une disjonction en CNF ou un littÃ©ral
            // et alors cnf(Â¬P âˆ¨ Ïˆ) = Â¬P âˆ¨ Ïˆ
            else
                return new Ou(this.clone(), psi);
        }
        protected void addSousClauses(DIMACS clauses) {
            // Ï†'= Â¬P
            // rien Ã  faire
        }
        protected boolean addToSequent(Sequent seq) {
            // vÃ©rifie que nous sommes bien en forme normale nÃ©gative
            assert (phi1.getClass() == Formule.Proposition.class);
            Formule.Proposition p = (Formule.Proposition) phi1;
            Boolean existe = seq.litteraux.put(p.nom,Boolean.FALSE);
            return existe == null || !existe.booleanValue();
        }
    }
    public static class Proposition extends Formule {
        protected String nom;
        public Proposition (String nom) {
            this.nom = nom;
        }
        public String toString() {
            return nom;
        }
        public int setid(int n, Map<String,Integer> p) {
            // pour un littÃ©ral positif, son identifiant est
            // l'entier associÃ© Ã  sa proposition
            if (p.containsKey(nom)) {
                int k = p.get(nom).intValue();
                this.id = k;
                return n;
            }
            else {
                p.put(nom, n+1);
                this.id = n+1;
                return n+1;
            }
        }
        public boolean evalue(Map<String,Boolean> interpretation) {
            return interpretation.get(nom).booleanValue();
        }
        public Formule substitue(Map<String,Formule> s) {
            if (s.containsKey(nom))
                return s.get(nom).clone();
            else
                return new Proposition(nom);
        }
        public Proposition clone() {
            return new Proposition(nom);
        }
        public Formule getNNF() {
            return new Proposition(nom);
        }
        public Formule getDualNNF() {
            return new Non(new Proposition(nom));
        }
        public Formule getCNF() {
            // cnf(P) = P
            return this.clone();
        }
        // cas d'une formule (P âˆ¨ Ïˆ) oÃ¹ this = P et Ïˆ est dÃ©jÃ  en CNF
        protected Formule getCNF1(Formule psi) {
            // si Ïˆ est une conjonction : il faut appliquer la distributivitÃ©
            if (psi.getClass() == Formule.Et.class)
                return psi.getCNF1(this.clone());
            // sinon Ïˆ est une disjonction en CNF ou un littÃ©ral
            // et alors cnf(P âˆ¨ Ïˆ) = P âˆ¨ Ïˆ
            else
                return new Ou(this.clone(), psi);
        }
        protected void addSousClauses(DIMACS clauses) {
            // Ï†'= P
            // rien Ã  faire
        }
        protected boolean addToSequent(Sequent seq) {
            Boolean existe = seq.litteraux.put(nom,Boolean.TRUE);
            return existe == null || existe.booleanValue();
        }
    }

    // affichage des interprÃ©tations
    public static String interpretationToString
        (Map<String,Boolean> interpretation) {
        String ret = "[";
        for (Map.Entry<String, Boolean> pair: interpretation.entrySet())
            ret += (pair.getValue()?1:0) + "/" + pair.getKey() + ", ";
        return ret.substring(0, ret.length() - 2) + "]";
    }
    // affichage des substitutions propositionnelles
    public static String substitutionToString
        (Map<String,Formule> tau) {
        String ret = "[";
        for (Map.Entry<String, Formule> pair: tau.entrySet())
            ret += pair.getValue() + "/" + pair.getKey() + ", ";
        return ret.substring(0, ret.length() - 2) + "]";
    }
    // composition d'une interprÃ©tation avec une substitution
    public static Map<String,Boolean> interpretationOfSubstitution
        (Map<String,Boolean> i, Map<String,Formule> tau) {
        
        Map<String,Boolean> itau = new HashMap<String,Boolean>();
        for (Map.Entry<String, Boolean> pair: i.entrySet()) {
            if (tau.containsKey(pair.getKey()))
                itau.put(pair.getKey(), tau.get(pair.getKey()).evalue(i));
            else
                itau.put(pair.getKey(), pair.getValue());
        }
        return itau;        
    }
    // constructeur de formules avec implication
    public static Formule Impl(Formule phi1, Formule phi2) {
        return new Ou(new Non(phi1), phi2);
    }
    // petit jeu de tests    
    public static void main(String[] args) {
        System.out.println("ð�Ÿ®.ð�Ÿ®. Syntaxe concrÃ¨te");
        Formule phi = new Et(new Ou(new Proposition ("P"),
                                    new Non(new Proposition("Q"))),
                             new Proposition ("P"));
        System.out.println("           Ï† = " + phi);
        // sÃ©mantique
        System.out.println("\nð�Ÿ¯.ð�Ÿ­.ð�Ÿ¯. SÃ©mantique");
        Map<String,Boolean> I = new HashMap<String,Boolean>();
        I.put("P",Boolean.TRUE);
        I.put("Q",Boolean.FALSE);
        System.out.println("           I = "+interpretationToString(I));
        System.out.println("       âŸ¦Ï†âŸ§^I = "+ (phi.evalue(I)?1:0));
        // substitutions
        System.out.println("\nð�Ÿ°.ð�Ÿ¯.ð�Ÿ­. Substitutions propositionnelles");
        Map<String,Formule> tau = new HashMap<String,Formule>();
        tau.put("P", new Proposition("Q"));
        tau.put("Q", new Ou(new Proposition("P"),
                            new Non(new Proposition("Q"))));
        System.out.println("           Ï„ = "+ substitutionToString(tau));
        System.out.println("          Ï†Ï„ = "+ phi.substitue(tau));
        // lemme de substitution
        System.out.println("\nExemple d'utilisation du lemme 4.7 de substitution propositionnelle");
        Map<String,Boolean> Itau = interpretationOfSubstitution(I, tau);
        System.out.println("          IÏ„ = "+ interpretationToString(Itau));
        System.out.println("      âŸ¦Ï†Ï„âŸ§^I = "+ (phi.substitue(tau).evalue(I)?1:0));
        System.out.println("      âŸ¦Ï†âŸ§^IÏ„ = "+ (phi.evalue(Itau)?1:0));
        // forme normale nÃ©gative
        System.out.println("\nð�Ÿ±.ð�Ÿ­.ð�Ÿ­. Mise sous forme normale nÃ©gative");
        Formule peirce = Impl(Impl(Impl(new Proposition("P"), new Proposition("Q")),
                                   new Proposition("P")),
                              new Proposition("P"));
        System.out.println("      peirce = "+ peirce);
        System.out.println(" nnf(peirce) = "+ peirce.getNNF());
        System.out.println("nnf(Â¬peirce) = "+ peirce.getDualNNF());
        // forme normale conjonctive
        System.out.println("\nð�Ÿ±.ð�Ÿ®.ð�Ÿ­. Mise sous forme normale conjonctive logiquement Ã©quivalente");
        Formule psi =  new Ou(new Et(new Ou(new Non(new Proposition("P")),
                                            new Ou(new Proposition("T"),
                                                   new Non(new Proposition("R")))),
                                     new Ou(new Non(new Proposition("T")),
                                            new Et(new Non(new Proposition("S")),
                                                   new Et(new Proposition("Q"),
                                                          new Proposition("U"))))),
                              new Et (new Proposition("S"),
                                      new Proposition("R")));
        System.out.println("           Ïˆ = " + psi);
        System.out.println("      cnf(Ïˆ) = " + psi.getCNF());
        // exemple 5.5 des notes de cours
        System.out.println("\nExemple 5.5");
        Formule phi3 = new Ou(new Et(new Proposition("Pâ‚ƒ"),
                                     new Proposition("Qâ‚ƒ")),
                              new Ou(new Et(new Proposition("Pâ‚‚"),
                                            new Proposition("Qâ‚‚")),
                                     new Et(new Proposition("Pâ‚�"),
                                            new Proposition("Qâ‚�"))));
        System.out.println("          Ï†â‚ƒ = "+ phi3);
        System.out.println("     cnf(Ï†â‚ƒ) = "+ phi3.getCNF());
        Formule phi4 = new Ou(new Et(new Proposition("Pâ‚„"),
                                     new Proposition("Qâ‚„")),
                              phi3);
        System.out.println("\n          Ï†â‚„ = "+ phi4);
        System.out.println("     cnf(Ï†â‚„) = "+ phi4.getCNF());
        // forme clausale Ã©qui-satisfiable au format DIMACS
        System.out.println("\nð�Ÿ±.ð�Ÿ®.ð�Ÿ°. Forme clausale Ã©qui-satisfiable, au format DIMACS");
        DIMACS phi3dimacs = phi3.getDIMACS();
        System.out.println("Format DIMACS(Ï†â‚ƒ) :\n\n" + phi3dimacs);

        // utilisation de Sat4J
        System.out.println("\nð�Ÿ².ð�Ÿ­.ð�Ÿ®. Utilisation de Sat4J");
        System.out.println("Sur DIMACS(Ï†â‚ƒ) :\n");
        if (phi3dimacs.satisfiable()) {
            int[] modele = phi3dimacs.modele();
            System.out.println("SAT");
            for (int i = 0; i < modele.length; i++)
                System.out.print(modele[i] + " ");
            System.out.println("0");
            System.out.println("\nCe qui correspond Ã  l'interprÃ©tation\n"+phi3dimacs.modeleAsString());
        } else
            System.out.println("UNSAT");
    }
}
    
