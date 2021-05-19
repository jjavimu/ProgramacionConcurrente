package parte2.Mensajes;


public class Msg_conexion extends Mensaje{

    public Msg_conexion(String nombre_origen, String nombre_destino){
        super(TipoMensaje.MSG_CONEXION, nombre_origen, nombre_destino);     
    }
}