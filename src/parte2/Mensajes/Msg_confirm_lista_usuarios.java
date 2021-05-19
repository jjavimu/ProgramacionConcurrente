package parte2.Mensajes;

import java.util.HashMap;
import java.util.List;
import parte2.Usuario;

public class Msg_confirm_lista_usuarios extends Mensaje{
    private HashMap<String, List<String>> usuarios;

    public Msg_confirm_lista_usuarios(HashMap<String, List<String>> usuarios, String nombre_emisor, String nombre_receptor){
        super(TipoMensaje.MSG_CONFIRM_LISTA_USUARIOS, nombre_emisor, nombre_receptor);  
        this.usuarios = usuarios;
    }

    public HashMap<String, List<String>> getLista(){
        return this.usuarios;
    }
}