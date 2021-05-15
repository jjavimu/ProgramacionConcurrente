package parte2.Mensajes;

public class Msg_solicitud_fichero extends Mensaje{

    private String nombre;

    public Msg_solicitud_fichero(String nombre){
        super(TipoMensaje.MSG_SOLICITUD_FICHERO);
        this.nombre = nombre;     
    }

    public String getNombreFichero() {
        return nombre;
    }
}