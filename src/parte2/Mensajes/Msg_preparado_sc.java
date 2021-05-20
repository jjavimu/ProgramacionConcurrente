package parte2.Mensajes;

import java.net.InetAddress;

public class Msg_preparado_sc extends Mensaje{

    private InetAddress dirIP;
    private int puerto_propietario; // puerto del propietario del emisor
    private String nombre_fichero;



    public Msg_preparado_sc(int puerto, InetAddress dirIP, String fichero, String nombre_emisor, String nombre_receptor){
        super(TipoMensaje.MSG_PREPARADO_SC, nombre_emisor, nombre_receptor);
        this.dirIP = dirIP;
        this.puerto_propietario = puerto;
        this.nombre_fichero = fichero;
    }

    public int getPuerto(){
        return puerto_propietario;
    }

    public InetAddress getIP(){
        return dirIP;
    }

    public String getNombreFichero(){
        return nombre_fichero;
    }
}