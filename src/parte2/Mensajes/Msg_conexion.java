package parte2.Mensajes;

import java.net.InetAddress;

public class Msg_conexion extends Mensaje{
    private InetAddress ip;
    public Msg_conexion(String nombre_origen, String nombre_destino, InetAddress my_ip){
        super(TipoMensaje.MSG_CONEXION, nombre_origen, nombre_destino);     
        this.ip = my_ip;
    }

    public InetAddress getIP() {
        return ip;
    }
}