package parte2.Mensajes;

public class Msg_solicitud_fichero extends Mensaje{

    private String nombre_fichero;
 
    public Msg_solicitud_fichero(String nombre_fichero, String nombre_origen, String nombre_destino){
        super(TipoMensaje.MSG_SOLICITUD_FICHERO, nombre_origen, nombre_destino);
        this.nombre_fichero = nombre_fichero;     
    }

    public String getNombreFichero() {
        return nombre_fichero;
    }
}