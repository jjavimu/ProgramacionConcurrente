package parte2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    // Escritura - exclusion mutua, lectura no (readers and writers)
    // Contadores globales con acceso seguro (crear canales-puertos (lo de que
    // tardan en quedarse libres))
    // CON TODO LO ANTERIOR: LOCKS, SEM, MONITOR

    // tabla usuarios --- (id usuario, fin, fout)
    // tabla informacion --- (id usuario, [f1,f2,....])
    // tabla --- fichero - Listausuario
    // direccion IP
    // puerto
    // serverSocket

    public static void main(String[] args) throws Exception {

        try {
            // Leer fichero con los usuaros registrados y todos sus datos
            File usuarios = new File("src/parte2/ServidorRecursos/users.txt");
            FileReader fr = new FileReader(usuarios);
            BufferedReader br = new BufferedReader(fr);
            String linea; String usuario_leido;
            while ((linea = br.readLine()) != null){
                String[] datos = linea.split(" ");
                switch (datos.length){
                    case 1: // es el nombre de un fichero
                        // meter en donde sea de usuario_leido
                        break;
                    case 2: // es usuario o ip
                        if(datos[0] == "Usuario"){
                            usuario_leido = datos[0];
                            // meter donde sea
                        }
                        else { // es "IP"
                            // meter en donde sea de usuario_leido
                        }
                        break;
                    default: 
                        System.out.println("Servidor: Error de lectura en la base de datos");
                        System.exit(1);
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("Fichero users.txt no encontrado");
            System.exit(1);
        }

        // Crear ServerSocket
        ServerSocket ss = new ServerSocket(500); // Cambiar puertos *******

        while (true) {
            // Me quedo esperando a la peticion de inicio de sesion
            Socket si = ss.accept();
            // Asocio un hilo de ejecucion a cada usuario
            (new OC(si)).run();
        }
    }
}

// De forma concurrente tiene que hacer las siguientes acciones:
            // accion 1:Solicitud de busqueda de usuarios conectados: El servidor realiza
            // una busqueda
            // en su base de datos y devuelve los resultados obtenidos.

            // accion 2: Solicitud de descarga de informacion: El servidor se comunica con
            // los dos clientes
            // en cuestion, gestionando el inicio de la comunicacion p2p entre ellos. Una
            // vez los clientes
            // establecen conexion el servidor se desentiende de la comunicacion p2p.

            // accion 3: Fin de sesion y actualiza la base de datos

// CONTROLAR QUE NO SE CONECTEN A LA VEZ DOS CON EL MISMO NOMBRE