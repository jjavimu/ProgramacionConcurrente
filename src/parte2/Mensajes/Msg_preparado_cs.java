package parte2.Mensajes;

public class Msg_preparado_cs extends Mensaje{

    private String nombre_usuario;
    private int puerto;

    public Msg_preparado_cs(String user, int puerto){
        super(TipoMensaje.MSG_PREPARADO_CS);
        this.nombre_usuario = user;
        this.puerto = puerto;
    }

    public String getNombreUsuario(){
        return nombre_usuario;
    }

    public int getPuerto(){
        return puerto;
    }
}