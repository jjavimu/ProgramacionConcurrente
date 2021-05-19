package parte2.Mensajes;


public class Msg_emitir_fichero extends Mensaje{

    private String nombre_fichero;
    private int num_puerto;

    // nombre_emisor es el destino del mensaje
    // nombre receptor el origen
    public Msg_emitir_fichero(String fichero, String nombre_emisor, String nombre_receptor, int num_puerto){
        super(TipoMensaje.MSG_EMITIR_FICHERO,nombre_receptor, nombre_emisor);  
        this.nombre_fichero = fichero;
        this.num_puerto = num_puerto;
    }

    public String getFichero(){
        return nombre_fichero;
    }

    public int getPuerto(){
        return num_puerto;
    }

}