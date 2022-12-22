package app;
// nécessite Sat4j : http://download.forge.ow2.org/sat4j/
import java.io.*;
import org.sat4j.reader.*;

public class Main {
    public static void main(String[] args) {
        Solveur solveur = new DPLL();
        if (args[0].equals("--naif"))
            solveur = new SolveurNaif();
        else if (args[0].equals("--split"))
            solveur = new SolveurSplit();
        else if (args[0].equals("--rec"))
            solveur = new DPLLRec();
        else if (args[0].equals("--sat4j"))
            solveur = new DIMACS();
        else
            solveur = new DPLL();
        String filename = args[0].startsWith("--")? args[1]: args[0];
        try {
            new DimacsParser(solveur).parse(filename);
            if (solveur.satisfiable()) {
                int[] modele = solveur.modele();
                System.out.println("SAT");
                for (int i = 0; i < modele.length; i++)
                    System.out.print(modele[i] + " ");
                System.out.println("0");
            }
            else
                System.out.println("UNSAT");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("UNSAT");
        } catch (ParseFormatException e) {
            System.err.println("Parse error: "+e);
        } catch (IOException e) {
            System.err.println("I/O error: "+e);
        }
    }
}
