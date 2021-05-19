package parte2.Mensajes;

public class Msg_confirm_conexion extends Mensaje{

    public Msg_confirm_conexion(String nombre_origen, String nombre_destino){
        super(TipoMensaje.MSG_CONFIRM_CONEXION, nombre_origen, nombre_destino);       
    }

}