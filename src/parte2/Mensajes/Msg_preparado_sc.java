package parte2.Mensajes;

public class Msg_preparado_sc extends Mensaje{

    private String dirIP;
    private int puerto_propietario; // puerto del propietario del emisor
    private String nombre_fichero;



    public Msg_preparado_sc(int puerto, String dirIP, String fichero, String nombre_emisor, String nombre_receptor){
        super(TipoMensaje.MSG_PREPARADO_SC, nombre_emisor, nombre_receptor);
        this.dirIP = dirIP;
        this.puerto_propietario = puerto;
        this.nombre_fichero = fichero;
    }

    public int getPuerto(){
        return puerto_propietario;
    }

    public String getIP(){
        return dirIP;
    }

    public String getNombreFichero(){
        return nombre_fichero;
    }
}