package parte2.Mensajes;

public class Msg_preparado_cs extends Mensaje{

    private String nombre_receptor;
    private String nombre_emisor;
    private int puerto;

    public Msg_preparado_cs(String nombre_emisor, String nombre_receptor, int puerto){
        super(TipoMensaje.MSG_PREPARADO_CS);
        this.nombre_receptor = nombre_receptor;
        this.nombre_emisor = nombre_emisor;
        this.puerto = puerto;
    }

    public String getNombreReceptor(){
        return nombre_receptor;
    }

    public String getNombreEmisor(){
        return nombre_emisor;
    }

    public int getPuerto(){
        return puerto;
    }
}