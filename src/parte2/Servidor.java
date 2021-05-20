package parte2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import parte2.SinCon.*;

public class Servidor {
    // direccion IP


    public static void main(String[] args) throws Exception {
        // Tabla de usuarios
        MUsuarios tablaUsuarios = new MUsuarios();
        SemLectorEscritor sem_usuarios = new SemLectorEscritor();

        // Tabla fichero - usuarios
        MFicheros tablaFicheros = new MFicheros(); // de los conectados
        SemLectorEscritor sem_ficheros = new SemLectorEscritor();
        // Tabla usuario - flujos E/S
        Canales tablaCanales = new Canales(); // de los conectados
        MonitorLectorEscritor monitor_canales = new MonitorLectorEscritor(); // Para la EM de tablaCanales

        // Creacion y control de puertos 
        Puerto puerto = new Puerto(500); // Puerto del server. A partir de ese se genera el resto.
        Semaphore sem_puerto = new Semaphore(1); // Para la EM de puerto
        
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            System.out.println("Your current socket port : " + puerto.getPuerto());
 
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        

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
                    Usuario nuevo_usuario = new Usuario(datos[0],InetAddress.getByName(datos[1]),false,ficheros);
                    tablaUsuarios.add(nuevo_usuario);
                }
               
            }
            System.out.println("-----------------------------------");
            System.out.println("USUARIOS EN LA BASE DE DATOS:\n");
            for( Usuario u: tablaUsuarios.getTabla()){
                System.out.println(u);
            }
            System.out.println("-----------------------------------");
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fichero users.txt no encontrado");
            System.exit(1);
        }

        // Crear ServerSocket
        ServerSocket ss = new ServerSocket(puerto.getPuerto());
        
        // serversocket.getLocalPort

        while (true) {
            // Me quedo esperando a la peticion de inicio de sesion
            Socket si = ss.accept();
            // Asocio un hilo de ejecucion a cada usuario
            (new Thread((new OC(si, tablaUsuarios, tablaFicheros, tablaCanales, puerto, sem_puerto,monitor_canales, sem_ficheros, sem_usuarios)))).start();
        }
    }
}
