package parte2.SinCon;
import java.util.concurrent.atomic.*;

public class LockTicket implements Lock {
    private volatile int next; // display
    private volatile AtomicInteger number; // turno ticket
    private int [] turn; // turnos
    private int N; // numero de procesos

    public LockTicket(int N){
        this.N = N;
        this.next = 0;
        this.number = new AtomicInteger(0);
        this.turn = new int[N];
        for(int i = 0; i < N ; ++i){
            this.turn[i] = -1;
        }

    }

    public void takeLock(int i) {
        this.turn[i] = number.getAndAdd(1);
        while(this.turn[i]!= this.next);

    }

    public void releaseLock(int i) {
        this.next = this.next + 1;
    }

    public String toString() {
        return "LockTicket";
    }

    
}
