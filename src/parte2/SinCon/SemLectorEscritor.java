package parte2.SinCon;

import java.util.concurrent.Semaphore;

public class SemLectorEscritor {
    private int n_lector;
    private int n_escritor;
    private int espera_lector;
    private int espera_escritor;

    private Semaphore e;
    private Semaphore lector;
    private Semaphore escritor;

    public SemLectorEscritor(){
        n_lector = 0;
        n_escritor = 0;
        espera_escritor = 0;
        espera_lector = 0;

        e = new Semaphore(1);
        lector = new Semaphore(0);
        escritor = new Semaphore(0);

    }

    public void request_read() throws InterruptedException{
        e.acquire();
        if(n_escritor > 0){
            espera_lector++;
            e.release();
            lector.acquire();
        }
        n_lector++;
        if(espera_lector > 0){
            espera_lector--;
            lector.release();
        }
        else e.release();
    }

    public void release_read() throws InterruptedException{
        e.acquire();

        n_lector--;

        if(n_lector == 0 && espera_escritor > 0){
            espera_escritor--;
            escritor.release();
        }
        else e.release();
    }

    public void request_write() throws InterruptedException{
        e.acquire();

        if(n_lector>0 || n_escritor > 0){
            espera_escritor++;
            e.release();
            escritor.acquire();
        }
        n_escritor++;
        e.release();
       
    }

    public void release_write() throws InterruptedException{
        e.acquire();
        n_escritor--;
        if(espera_escritor>0){
            espera_escritor--;
            escritor.release();
        }
        else if(espera_lector>0){
            espera_lector--;
            lector.release();
        }
        else e.release();
    
    }
    
}
