package parte2.SinCon;


public class LockRompeEmpate implements Lock{
    // Tiene mayor complejidad pero es más justo.
    // Conviene usarlo cuando sabemos el numero de procesos que hay
    // porque si hay muchos le cuesta bastante
    private volatile Cell [] in;
    private volatile Cell [] last;
    private int N;

    public LockRompeEmpate(int N) {
        this.N = N;
        this.in = new Cell[N];
        this.last = new Cell[N];
        for(int i = 0; i < N ; ++i){
            this.in[i] = new Cell(-1);
            this.last[i] = new Cell(-1);
        }
    }

    public void takeLock(int i){
        for(int j = 0; j < N; ++j){
            in[i].set(j);
            last[j].set(i);
            for (int k = 0; k < N; ++k){
                if (k != i) {
                    while(in[k].get() >= in[i].get() && last[j].get() == i);
                }
            }
        }
    }

    public void releaseLock(int i){
        in[i].set(-1);
    }

    @Override
    public String toString() {
        return "LockRompeEmpate";
    }

}
