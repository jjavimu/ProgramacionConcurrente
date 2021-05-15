package parte1;

import java.io.Serializable;

public class Msg_solicitud_fichero extends Mensaje {
    private String nombre_fichero;
    private TipoMensaje tipomsg;
    //faltan atributos de origen y destino
    

    public Msg_solicitud_fichero(String nombre_fichero){
        this.tipomsg = TipoMensaje.MSG_SOLICITUD_FICHERO;
        this.nombre_fichero = nombre_fichero;
    }

    public TipoMensaje getTipo(){
        return tipomsg;
    }
    
    public String getOrigen(){
        return null;
    }
    
    public String getDestino(){
        return null;
    }

    public String getNombreFichero(){
        return nombre_fichero;
    }
    
}
