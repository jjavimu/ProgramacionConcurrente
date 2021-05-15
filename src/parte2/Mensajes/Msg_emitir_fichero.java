package parte2.Mensajes;


public class Msg_emitir_fichero extends Mensaje{

    private String nombre_cliente;
    private String nombre_fichero;

    public Msg_emitir_fichero(String cliente, String fichero){
        super(TipoMensaje.MSG_EMITIR_FICHERO);  
        this.nombre_cliente = cliente;
        this.nombre_fichero = fichero;
    }

    public String getCliente(){
        return nombre_cliente;
    }

    public String getFichero(){
        return nombre_fichero;
    }

}