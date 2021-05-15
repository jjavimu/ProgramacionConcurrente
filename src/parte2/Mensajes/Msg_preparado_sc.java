package parte2.Mensajes;

public class Msg_preparado_sc extends Mensaje{

    private String dirIP;
    private String puerto_propietario; // puerto del propietario del fichero

    public Msg_preparado_sc(String dirIP, String puerto){
        super(TipoMensaje.MSG_PREPARADO_SC);
        this.dirIP = dirIP;
        this.puerto_propietario = puerto;
    }
}