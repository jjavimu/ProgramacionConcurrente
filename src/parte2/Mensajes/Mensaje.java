package parte2.Mensajes;

import java.io.Serializable;

public class Mensaje implements Serializable{
    // Tipo de mensaje
    private TipoMensaje tipo_mensaje;
    // Origen
    private String origen;
    // Destino
    private String destino; 


    public Mensaje(TipoMensaje tipo){
        this.tipo_mensaje = tipo;
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
