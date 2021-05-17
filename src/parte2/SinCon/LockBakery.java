package parte2.SinCon;


public class LockBakery implements Lock {
    
    private volatile Cell [] turn;
    private int N;
    
    public LockBakery(int N) {
        this.N = N;
        this.turn = new Cell[N];
        for(int i = 0; i < N ; ++i){
            this.turn[i] = new Cell(-1);
        }
    }

    private int max(Cell[] array){
        int max = array[0].get();
        for (int i = 0; i < array.length; i++) {
            if (array[i].get() > max) {
                max = array[i].get();
            }
        }
        return max;

    }
    
    private boolean compararMayorPar(int a, int b, int c, int d) {
        return ((a>c) || (a == c && b > d));
    }

    public void takeLock(int i) {
        turn[i].set(0);
        turn[i].set(max(this.turn) + 1);
        for(int j = 0; j < this.N; ++j){
            if(j != i){
                while(turn[j].get() != -1 && compararMayorPar(this.turn[i].get(), i, this.turn[j].get(), j));
            }
        }

    }

    public void releaseLock(int i) {
        turn[i].set(-1);
    }
    
    public String toString() {
        return "LockBakery";
    }
}
