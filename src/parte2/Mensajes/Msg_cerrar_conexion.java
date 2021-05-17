package parte2.Mensajes;

public class Msg_cerrar_conexion extends Mensaje {
    private String nombre_usuario;
    
    public Msg_cerrar_conexion(String nombre_usuario) {
        super(TipoMensaje.MSG_CERRAR_CONEXION);
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombreUsuario(){
        return nombre_usuario;
    }
}
