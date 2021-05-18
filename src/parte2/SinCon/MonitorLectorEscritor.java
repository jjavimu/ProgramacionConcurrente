package parte2.SinCon;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorLectorEscritor{

    private final Lock l;
    private final Condition colaLector;
    private final Condition colaEscritor;
    private int nlector;
    private int nescritor;

    public MonitorLectorEscritor(){
        this.l = new ReentrantLock(true);
        this.colaLector = l.newCondition();
        this.colaEscritor = l.newCondition();
        this.nlector = 0;
        this.nescritor = 0;
    }

    public void request_read() throws InterruptedException{
        l.lock();
        while(nescritor >0){
            colaLector.wait();
        }
        nlector = nlector +1;
        l.unlock();
    }

    public void release_read(){
        l.lock();
        nlector = nlector -1;
        if(nlector == 0){
            colaEscritor.signal();
        }
        l.unlock();
    }

    public void request_write() throws InterruptedException{
        l.lock();
        while(nlector > 0 || nescritor > 0){
            colaEscritor.wait();
        }
        nescritor = nescritor + 1;
        l.unlock();
    }

    public void release_write(){
        l.lock();
        nescritor = nescritor -1;
        colaEscritor.signal();
        colaLector.signalAll();
        l.unlock();
    }
}
