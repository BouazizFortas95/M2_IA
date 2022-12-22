package app;
/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004, 2012 Artois University and CNRS
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL, and not to allow others to use your version of
 * this file under the terms of the EPL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 *
 * Based on the original MiniSat specification from:
 *
 * An extensible SAT solver. Niklas Een and Niklas Sorensson. Proceedings of the
 * Sixth International Conference on Theory and Applications of Satisfiability
 * Testing, LNCS 2919, pp 502-518, 2003.
 *
 * See www.minisat.se for the original solver in C++.
 *
 * Contributors:
 *   CRIL - initial API and implementation
 *******************************************************************************/
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sat4j.reader.EfficientScanner;
import org.sat4j.reader.ParseFormatException;

public class DimacsParser {
    protected int expectedNbOfConstr; // as announced on the p cnf line
    protected EfficientScanner scanner;
    protected final String formatString = "cnf";
    protected Solveur solveur;
    
    public DimacsParser(Solveur solveur) {
        this.solveur = solveur;
    }

    /**
     * Skip comments at the beginning of the input stream.
     * 
     * @throws IOException
     *             if an IO problem occurs.
     * @since 2.1
     */
    protected void skipComments() throws IOException {
        this.scanner.skipComments();
    }

    /**
     * @throws IOException
     *             iff an IO occurs
     * @throws ParseFormatException
     *             if the input stream does not comply with the DIMACS format.
     * @since 2.1
     */
    protected void readProblemLine() throws IOException, ParseFormatException {

        String line = this.scanner.nextLine().trim();

        if (line == null) {
            throw new ParseFormatException(
                    "premature end of file: <p cnf ...> expected");
        }
        String[] tokens = line.split("\\s+");
        if (tokens.length < 4 || !"p".equals(tokens[0])
                || !this.formatString.equals(tokens[1])) {
            throw new ParseFormatException("problem line expected (p cnf ...)");
        }

        int vars;

        // reads the max var id
        vars = Integer.parseInt(tokens[2]);
        assert vars > 0;
        this.solveur.setNProps(vars);
        // reads the number of clauses
        this.expectedNbOfConstr = Integer.parseInt(tokens[3]);
        assert this.expectedNbOfConstr > 0;
    }

    protected Clause literals = new Clause();

    protected void readConstrs()
        throws IOException, ParseFormatException {
        int realNbOfConstr = 0;

        this.literals.clear();
        boolean needToContinue = true;

        while (needToContinue) {
            boolean added = false;
            if (this.scanner.eof()) {
                // end of file
                if (this.literals.size() > 0) {
                    // no 0 end the last clause
                    flushConstraint();
                    added = true;
                }
                needToContinue = false;
            } else {
                if (this.scanner.currentChar() == 'c') {
                    // ignore comment line
                    this.scanner.skipRestOfLine();
                    continue;
                }
                added = handleLine();
            }
            if (added) {
                realNbOfConstr++;
            }
        }
    }
    
    protected void flushConstraint()
            throws UnsatisfiedLinkError {
        this.solveur.addUnsafe(this.literals);
        this.literals = new Clause();
    }

    protected boolean handleLine()
        throws IOException, ParseFormatException, UnsatisfiedLinkError {
        int lit;
        boolean added = false;
        while (!this.scanner.eof()) {
            lit = this.scanner.nextInt();
            if (lit == 0) {
                if (this.literals.size() > 0) {
                    flushConstraint();
                    this.literals.clear();
                    added = true;
                }
                break;
            }
            this.literals.add(Integer.valueOf(lit));
        }
        return added;
    }

    public Solveur parse(final String filename)
        throws ParseFormatException, IOException, UnsatisfiedLinkError {
        InputStream in = null;
        try {
            in = new FileInputStream(filename);
            this.scanner = new EfficientScanner(in);
            skipComments();
            readProblemLine();
            readConstrs();
            this.scanner.close();
            return this.solveur;
        } catch (IOException e) {
            throw new ParseFormatException(e);
        } catch (NumberFormatException e) {
            throw new ParseFormatException("integer value expected ");
        } catch (IllegalStateException e) {
            throw new ParseFormatException(e);
        } finally {
            if (in != null)
                in.close();
        }
    }
}
