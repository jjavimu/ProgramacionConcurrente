package parte2;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.*;

public class Usuario implements Serializable { // Representa un usuario registrado en el sistema

    // Identificador del usuario (es unico)
    private String nombre;
    // Direccion ip
    private InetAddress dirIP;
    // Lista de informacion compartida
    private List<String> ficheros;
    // Indica si el usuario esta o no conectado
    private boolean conectado;

    // El servidor almacena la info sobre todos los usuarios registrados en el sistema (instancias de esta clase)

    public Usuario(String nombre, InetAddress dirIP, boolean conectado, List<String> ficheros){
        this.nombre = nombre;
        this.conectado = conectado;
        this.dirIP = dirIP;
        this.ficheros = ficheros;
    }

    public String getNombre() {
        return this.nombre;
    }

    public InetAddress getDirIP() {
        return this.dirIP;
    }
    public void setDirIP(InetAddress ip) {
        this.dirIP = ip;
    }

    public List<String> getFicheros() {
        return  Collections.unmodifiableList(ficheros);
    }

    public void addFichero(String nombre_fichero){
        this.ficheros.add(nombre_fichero);
    }

    public boolean getConectado() {
        return this.conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public String toString(){  
        String imprime_nombre = "Usuario: " + this.nombre + "\n" ;
        String imprime_conexion = "Conectado: " + this.conectado + "\n";
        String imprime_ip = "Direccion IP: " + this.dirIP + "\n" ;
        String imprime_fich = "Ficheros : " + this.ficheros + "\n" ;
        
        return imprime_nombre + imprime_conexion + imprime_ip + imprime_fich;
    }
    
}
