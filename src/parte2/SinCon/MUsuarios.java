package parte2.SinCon;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parte2.*;

public class MUsuarios {

    // Clave - nombre usuario , Valor - Informacion del usuario
    private HashMap<String, Usuario> tabla = new HashMap<>();

    public MUsuarios(){}

    // Dado un usuario, si no existe lo mete en la tabla
    // si existe y no esta conectado devuelve cierto y lo conecta
    // si existe y esta conectado devuelve falso
    public synchronized boolean conectarUsuario(String nombre_usuario, InetAddress ip){
        boolean ok = true;
        Usuario usuario_info = tabla.get(nombre_usuario);
         if(usuario_info == null){
             // Introducimos el usuario en la base de datos
             tabla.put(nombre_usuario, new Usuario(nombre_usuario,ip,true,new ArrayList<>()));
         }

         else if(usuario_info.getConectado()) {
             ok = false;
         } else{
             usuario_info.setConectado(true);
             usuario_info.setDirIP(ip);
         }
        return ok;
    }

    public synchronized boolean comprobarConectado(String nombre_usuario){
        return tabla.get(nombre_usuario).getConectado();
    }

    /*public synchronized List<Usuario> getListaUsuariosConectados(){
        List<Usuario> conectados = new ArrayList<>();
        // Recorremos el hashmap buscando los conectados
       for (Map.Entry<String, Usuario> entry : tabla.entrySet()) {
            // System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if(entry.getValue().getConectado()){
                conectados.add(entry.getValue());
            }
       }
       return Collections.unmodifiableList(conectados);
    }*/

    public synchronized void desconectar(String nombre){
        tabla.get(nombre).setConectado(false);
    }

    public synchronized List<String> getFicheros(String nombre){
        return tabla.get(nombre).getFicheros();
    }

    public synchronized InetAddress getIP(String nombre){
        return tabla.get(nombre).getDirIP();
    }

	public synchronized void actualizar(String nombre_receptor, String nombre_fichero) {
        tabla.get(nombre_receptor).addFichero(nombre_fichero);
	}

    public synchronized void add(Usuario nuevo_usuario) {
        tabla.put(nuevo_usuario.getNombre(),nuevo_usuario);
    }

    public synchronized List<Usuario>getTabla(){
        List<Usuario> usuarios= new ArrayList<>();
        // Recorremos el hashmap buscando los conectados
       for (Map.Entry<String, Usuario> entry : tabla.entrySet()) {
            // System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            usuarios.add(entry.getValue());
       }
       return Collections.unmodifiableList(usuarios);
    }

    public synchronized HashMap<String, List<String>> getLista(boolean todos){
        HashMap<String, List<String>>  usuarios = new HashMap<>();
        // Recorremos el hashmap buscando los conectados
       for (Map.Entry<String, Usuario> entry : tabla.entrySet()) {
            List<String> ficheros = new ArrayList<>();
            if(entry.getValue().getConectado() || todos){ // Si esta conectado o me han dihcho que todos
                for(String s : entry.getValue().getFicheros()){
                    ficheros.add(s);
                }
                usuarios.put(entry.getKey(), ficheros);
            }
       }

       return usuarios;
    }

    
}
