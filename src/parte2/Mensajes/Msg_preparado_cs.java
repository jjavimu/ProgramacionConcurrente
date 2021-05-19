package parte2.Mensajes;

public class Msg_preparado_cs extends Mensaje{
    private int puerto;
    private String nombre_fichero;

    // Origen - emisor
    // Destino - receptor
    public Msg_preparado_cs(String nombre_emisor, String nombre_receptor, int puerto, String fichero){
        super(TipoMensaje.MSG_PREPARADO_CS, nombre_emisor, nombre_receptor);
        this.puerto = puerto;
        this.nombre_fichero = fichero;
    }

    public int getPuerto(){
        return puerto;
    }

    public String getNombreFichero(){
        return nombre_fichero;
    }
}