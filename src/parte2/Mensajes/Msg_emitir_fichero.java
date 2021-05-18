package parte2.Mensajes;


public class Msg_emitir_fichero extends Mensaje{

    private String nombre_fichero;
    private String nombre_receptor;
    private String nombre_emisor;
    private int num_puerto;

    public Msg_emitir_fichero(String fichero, String nombre_emisor, String nombre_receptor, int num_puerto){
        super(TipoMensaje.MSG_EMITIR_FICHERO);  
        this.nombre_fichero = fichero;
        this.nombre_receptor = nombre_receptor;
        this.nombre_emisor = nombre_emisor;
        this.num_puerto = num_puerto;
    }

    public String getFichero(){
        return nombre_fichero;
    }

    public String getNombreReceptor(){
        return nombre_receptor;
    }

    public String getNombreEmisor(){
        return nombre_emisor;
    }

    public int getPuerto(){
        return num_puerto;
    }

}