package app;

public abstract class Solveur {
    protected int nprops;
    
    protected Solveur() {
        this(0);
    }

    public Solveur(int nprops) {
        this.nprops = nprops;
    }
    
    protected void setNProps(int nprops) {
        this.nprops = nprops;
    }

    protected boolean addUnsafe(Clause c) throws UnsatisfiedLinkError {
        if (c.size() == 0)
            throw new UnsatisfiedLinkError();
        return add(c);
    }

    public abstract boolean add(Clause c);

    public abstract int[] modele();

    public abstract boolean satisfiable();
}
