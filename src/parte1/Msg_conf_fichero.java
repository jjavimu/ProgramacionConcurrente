package parte1;

import java.io.Serializable;

public class Msg_conf_fichero implements Mensaje, Serializable {

    @Override
    public TipoMensaje getTipo() {
        return TipoMensaje.MSG_CONF_FICHERO;
    }

    @Override
    public String getOrigen() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDestino() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNombreFichero() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
