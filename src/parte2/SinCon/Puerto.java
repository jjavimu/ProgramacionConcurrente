package parte2.SinCon;

public class Puerto {

    private int puerto;
  
    public Puerto(int puerto) {
        this.puerto = puerto;
    }

    public int getPuerto() {
        return this.puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void actualiza(){
        puerto = puerto + 1;
    }

}
