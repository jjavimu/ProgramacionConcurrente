package parte2.Mensajes;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Msg_conexion extends Mensaje{
    private String nombre_usuario;
    private ObjectInputStream finc;
    private ObjectOutputStream foutc;

    public Msg_conexion(String nombre, ObjectInputStream finc, ObjectOutputStream foutc){
        super(TipoMensaje.MSG_CONEXION);   
        this.nombre_usuario = nombre; 
        this.finc = finc;
        this.foutc = foutc;   
    }
    
    public String getNombreUsuario() {
        return this.nombre_usuario;
    }

    public ObjectInputStream getFinc() {
        return this.finc;
    }

    public ObjectOutputStream getFoutc() {
        return this.foutc;
    }
}