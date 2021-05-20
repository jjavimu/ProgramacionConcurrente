package parte2.Mensajes;

import java.io.Serializable;


public class Mensaje implements Serializable{
    // Tipo de mensaje
    private TipoMensaje tipo_mensaje;
    // Origen
    private String origen;
    // Destino
    private String destino; 


    public Mensaje(TipoMensaje tipo, String nombre_origen, String nombre_destino){
        this.tipo_mensaje = tipo;
        this.origen = nombre_origen;
        this.destino = nombre_destino;
    }

    public TipoMensaje getTipo(){
        return tipo_mensaje;
    }
    public String getOrigen(){
        return origen;
    }
    public String getDestino(){
        return destino;
    }

}
