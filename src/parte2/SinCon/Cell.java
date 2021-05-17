package parte2.SinCon;


public class Cell {
    
    private volatile int elem;
    
    public Cell(int init) {
        this.elem = init;
    }

    public int get(){
        return this.elem;
    }

    public void set(int x){
        this.elem = x;
    }

    
}
