package parte1;

import java.io.Serializable;

public class Fichero implements Serializable {
    private String nombre;

    public Fichero(String nombre){
        this.nombre = nombre;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String toString(){
        return nombre;
    }
}
