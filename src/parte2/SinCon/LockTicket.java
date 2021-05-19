package parte2.SinCon;
import java.util.concurrent.atomic.*;

public class LockTicket{
    private volatile int next; // turno siguiente
    private volatile AtomicInteger number; // turno ticket
    private int [] turn; // turnos

    public LockTicket(int MAX){
        this.next = 0;
        this.number = new AtomicInteger(0);
        this.turn = new int[MAX];
        for(int i = 0; i < MAX ; ++i){
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
