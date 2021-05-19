package parte2.Mensajes;

public class Msg_cerrar_conexion extends Mensaje {
    
    public Msg_cerrar_conexion(String nombre_origen,String nombre_destino) {
        super(TipoMensaje.MSG_CERRAR_CONEXION, nombre_origen, nombre_destino);
    }
}
