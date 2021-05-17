package parte2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import parte2.SinCon.*;

public class Servidor {
    // Contadores globales con acceso seguro (crear canales-puertos (lo de que tardan en quedarse libres))
    // direccion IP


    public static void main(String[] args) throws Exception {
        // Tabla de usuarios
        MUsuarios tablaUsuarios = new MUsuarios();
        // Tabla fichero - usuarios
        MFicheros tablaFicheros = new MFicheros(); // de los conectados
        // Tabla usuario - flujos E/S
        MCanales tablaCanales = new MCanales(); // de los conectados

        // Creacion y control de puertos 
        Semaphore sem_puerto = new Semaphore(1); // Para la EM de puerto
        int puerto = 1; // Puerto del server. A partir de ese se genera el resto.
        

        try {
            // Leer fichero con los usuaros registrados y todos sus datos
            // Guardamos la info SOLO en el monitor tablaUsuarios
            File usuarios = new File("parte2/ServidorRecursos/users.txt");
            FileReader fr = new FileReader(usuarios);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            
            while ((linea = br.readLine()) != null) { 
                String[] datos = linea.split(" ");
                // Aqui se hacen los new Usuarios
                if(datos.length < 2){
                    System.out.println("Servidor: Error de lectura en la base de datos");
                    System.exit(1);
                }
                else {
                    List<String> ficheros = new ArrayList<>();
                    for(int i = 2; i < datos.length; ++i){
                        ficheros.add(datos[i]);
                    }
                    Usuario nuevo_usuario = new Usuario(datos[0],datos[1],false,ficheros);
                    tablaUsuarios.add(nuevo_usuario);
                }
               
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichero users.txt no encontrado");
            System.exit(1);
        }

        // Crear ServerSocket
        ServerSocket ss = new ServerSocket(1);
        // serversocket.getLocalPort

        while (true) {
            // Me quedo esperando a la peticion de inicio de sesion
            Socket si = ss.accept();
            // Asocio un hilo de ejecucion a cada usuario
            (new Thread((new OC(si, tablaUsuarios, tablaFicheros, tablaCanales, puerto, sem_puerto)))).start();
        }
    }
}
