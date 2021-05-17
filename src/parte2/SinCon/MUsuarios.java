package parte2.SinCon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parte2.*;

public class MUsuarios {

    // Clave - nombre usuario , Valor - Informacion del usuario
    private HashMap<String, Usuario> tabla = new HashMap<>();

    public MUsuarios(){}

    // Dado un usuario, si no existe lo mete en la tabla
    // si existe y no esta conectado devuelve cierto
    // si existe y esta conectado devuelve falso
    public synchronized boolean conectarUsuario(String nombre_usuario){
        boolean ok = true;
        Usuario usuario_info = tabla.get(nombre_usuario);
         if(usuario_info == null){
             // Introducimos el usuario en la base de datos
             tabla.put(nombre_usuario, new Usuario(nombre_usuario,"localhost",true,new ArrayList<>()));
         }

         else if(usuario_info.getConectado()) {
             ok = false;
         }
        return ok;
    }

    public synchronized boolean comprobarConectado(String nombre_usuario){
        return tabla.get(nombre_usuario).getConectado();
    }

    public synchronized List<Usuario> getListaUsuariosConectados(){
        List<Usuario> conectados = new ArrayList<>();
        // Recorremos el hashmap buscando los conectados
       for (Map.Entry<String, Usuario> entry : tabla.entrySet()) {
            // System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if(entry.getValue().getConectado()){
                conectados.add(entry.getValue());
            }
       }
       return conectados;
    }

    public synchronized void desconectar(String nombre){
        tabla.get(nombre).setConectado(false);
    }

    public synchronized List<String> getFicheros(String nombre){
        return tabla.get(nombre).getFicheros();
    }

    public synchronized String getIP(String nombre){
        return tabla.get(nombre).getDirIP();
    }

	public void actualizar(String nombre_receptor, String nombre_fichero) {
        tabla.get(nombre_receptor).addFichero(nombre_fichero);
	}

    public void add(Usuario nuevo_usuario) {
        tabla.put(nuevo_usuario.getNombre(),nuevo_usuario);
    }
    
}
