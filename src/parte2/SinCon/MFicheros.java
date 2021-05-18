package parte2.SinCon;

import java.util.HashMap;
import java.util.*;

public class MFicheros {

    // Clave-Nombre fichero , Valor-Cjto nombres de usuarios
    private HashMap<String, Set<String>> tabla = new HashMap<>();

    public MFicheros(){}

    // Dado un fichero, me devuelve un cliente que lo tenga y este conectado
    public synchronized String buscarUsuario (String nombre_fichero){
        Set<String> usuarios = tabla.get(nombre_fichero); // Devuelve null, o la lista (no vacia) de usuarios que lo tienen
        String nombre_usuario=null;
        if (usuarios != null){
            nombre_usuario = usuarios.iterator().next();// Seleccionamos el primero que esté
        }
        return nombre_usuario;
    } 

    public synchronized void setFicheros(List<String> ficheros, String nombre){
        // Recorremos la lista de los ficheros del usuario
        for(String nombre_fichero: ficheros){
            Set<String> lista = tabla.get(nombre_fichero);
            if(lista != null){ // Si no es null es que hay un set ya
                lista.add(nombre);
            }
            else { // si es null es que no habia set, asi que lo creamos y lo añadimos
                tabla.put(nombre_fichero, new HashSet<String>());
                tabla.get(nombre_fichero).add(nombre);
            }

        }
    }

    public synchronized void desconectar(String nombre_usuario) { // TENER UN SET PARA ELIMINAR
        for (Map.Entry<String, Set<String>> entry : tabla.entrySet()) {
            // System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            entry.getValue().remove(nombre_usuario); // no estoy seguro de si compara bien pero creo que si
        }
    }

	public synchronized void actualizar(String nombre_receptor, String nombre_fichero) {
        tabla.get(nombre_fichero).add(nombre_receptor);
	}
    
}
