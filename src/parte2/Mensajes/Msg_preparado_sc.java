package parte2.Mensajes;

public class Msg_preparado_sc extends Mensaje{

    private String dirIP;
    private int puerto_propietario; // puerto del propietario del fichero



    public Msg_preparado_sc(int puerto, String dirIP){
        super(TipoMensaje.MSG_PREPARADO_SC);
        this.dirIP = dirIP;
        this.puerto_propietario = puerto;
    }

    public int getPuerto(){
        return puerto_propietario;
    }

    public String getIP(){
        return dirIP;
    }
}