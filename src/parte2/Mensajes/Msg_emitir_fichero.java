package parte2.Mensajes;


public class Msg_emitir_fichero extends Mensaje{

    private String nombre_fichero;
    private String nombre;
    private int num_puerto;

    public Msg_emitir_fichero(String fichero, String nombre, int num_puerto){
        super(TipoMensaje.MSG_EMITIR_FICHERO);  
        this.nombre_fichero = fichero;
        this.nombre = nombre;
        this.num_puerto = num_puerto;
    }

    public String getFichero(){
        return nombre_fichero;
    }

    public String getNombreUsuario(){
        return nombre;
    }

    public int getPuerto(){
        return num_puerto;
    }

}