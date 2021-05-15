package parte2.Mensajes;

import java.util.ArrayList;
import java.util.List;
import parte2.Usuario;

public class Msg_confirm_lista_usuarios extends Mensaje{
    private List<Usuario> usuarios;

    public Msg_confirm_lista_usuarios(){
        super(TipoMensaje.MSG_LISTA_USUARIOS);  
        usuarios = new ArrayList<Usuario>();
    }

    public List<Usuario> getLista(){
        return this.usuarios;
    }
}