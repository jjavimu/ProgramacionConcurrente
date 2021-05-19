package parte2.Mensajes;


public class Msg_lista_usuarios extends Mensaje{

    public Msg_lista_usuarios(String nombre_emisor, String nombre_receptor){
        super(TipoMensaje.MSG_LISTA_USUARIOS, nombre_emisor, nombre_receptor); 
    }
}