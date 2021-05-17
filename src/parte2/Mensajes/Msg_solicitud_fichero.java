package parte2.Mensajes;

public class Msg_solicitud_fichero extends Mensaje{

    private String nombre_fichero;
    private String nombre_usuario;
 
    public Msg_solicitud_fichero(String nombre_fichero, String nombre_usuario){
        super(TipoMensaje.MSG_SOLICITUD_FICHERO);
        this.nombre_fichero = nombre_fichero;     
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombreFichero() {
        return nombre_fichero;
    }
    
    public String getNombreUsuario(){
        return nombre_usuario;
    }
}