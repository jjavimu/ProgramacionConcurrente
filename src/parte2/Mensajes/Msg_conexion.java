package parte2.Mensajes;


public class Msg_conexion extends Mensaje{
    private String nombre_usuario;

    public Msg_conexion(String nombre){
        super(TipoMensaje.MSG_CONEXION);   
        this.nombre_usuario = nombre;    
    }
    
    public String getNombreUsuario() {
        return this.nombre_usuario;
    }
}