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
        e.acquire(); //cojo el mutex
        if(n_escritor > 0){ // hay escritor
            espera_lector++; // lector espera +1
            e.release(); // suelto el mutex porque se duerme
            lector.acquire(); // me duermo en cola lector
        }
        n_lector++; // me despiertan. Me han pasado mutex e
        if(espera_lector > 0){ // si hay mas dormidos, les despierto
            espera_lector--; // uno menos esperando
            lector.release(); // le despierto
        }
        else e.release();  // el ultimo en despertar libera mutex para que entre quien quiera
    } //todos leen ahora a la vez

    public void release_read() throws InterruptedException{
        e.acquire(); //cojo mutex
        n_lector--; // soy uno menos

        if(n_lector == 0 && espera_escritor > 0){ //si hay escritor esperando y no lectores
            espera_escritor--;
            escritor.release(); //despierto al escritor y le paso el mutex
        }
        else e.release(); //libero para que entre quien quiera
    }

    public void request_write() throws InterruptedException{
        e.acquire(); //cojo mutex

        if(n_lector>0 || n_escritor > 0){ //hay lectores o escritores
            espera_escritor++; //me espero, libero y duermo
            e.release();
            escritor.acquire();
        }
        n_escritor++; //me despiertan pasando el mutex
        e.release(); //libero
       
    }

    public void release_write() throws InterruptedException{
        e.acquire(); //cojo y uno menos
        n_escritor--; 
        //despierto a los que esperan pasando testigo
        if(espera_escritor>0){ //primero miro escritores
            espera_escritor--;
            escritor.release();
        }
        else if(espera_lector>0){
            espera_lector--;
            lector.release();
        }
        else e.release(); //si nadie espera lo libero
    
    }
    
}
