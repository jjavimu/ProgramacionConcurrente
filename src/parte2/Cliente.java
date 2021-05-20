package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import parte2.Mensajes.*;
import parte2.SinCon.*;

public class Cliente{
    
    public static void main(String[] args) throws Exception { // main : interactuar con el usuario
        Scanner teclado = new Scanner(System.in);
        System.out.println("Introduce la ip del host: ");
         // IP del servidor
        String host = teclado.nextLine();//"MacBook-Pro-de-Javi.local"; 

        // IP del cliente
        InetAddress my_ip = InetAddress.getLocalHost();

        // Preguntar el nombre del usuario
        System.out.println("Introduce el nombre de usuario: ");
        String nombre_usuario = teclado.nextLine();

        // Aquí podríamos ver qué ficheros tiene en su carpeta para rellenar los datos del servidor
        /* 
        File directoryPath = new File("parte2/ClienteRecursos/");
        //List of all files and directories
        List<String> contents = new ArrayList<String>(directoryPath.list());
        // Pasarle en msg_conexion contentss
        */

        // Creamos socket con el servidor
        Socket sc = new Socket(host, 500); 
        ObjectOutputStream foutc = new ObjectOutputStream(sc.getOutputStream());
        ObjectInputStream finc = new ObjectInputStream(sc.getInputStream());

        // Creamos hilo oyente-servidor
        LockTicket lock = new LockTicket(2); //0-Cliente, 1-OS
        (new Thread(new OS(sc,finc,foutc,lock))).start();
        
        // Enviar mensaje de conexion al servidor
        foutc.writeObject(new Msg_conexion(nombre_usuario,"Servidor",my_ip));
        foutc.flush();

        /*
        // Ejecutamos el menu de opciones en un JFrame
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Menu(finc, foutc, nombre_usuario);

			}
		});*/
        
        
        // Codigo para cuando no usamos JFrame
        // Booleano para cerrar conexion
        boolean ok = true;
        while (ok) {

            lock.takeLock(0); 
            System.out.println("Menu de acciones (elige un numero): ");
            System.out.println("1 - Consultar el nombre de los usuarios conectados y su informacion");
            System.out.println("2 - Descargar informacion");
            System.out.println("3 - Desconectarse del servidor");
            lock.releaseLock(0);

            int opcion = teclado.nextInt();     
            teclado.nextLine();
            

            switch (opcion) {
                case 1:
                    // Enviar mensaje MSG_LISTA_USUARIOS
                    foutc.writeObject(new Msg_lista_usuarios(nombre_usuario, "Servidor"));
                    foutc.flush();
                    break;
                case 2:
                    // (se deben permitir otras acciones y descargas simultaneas)
                    // Enviar mensaje MSG_SOLICITUD_FICHERO
                    
                    lock.takeLock(0);
                    System.out.println("Indica el nombre del fichero que quieres descargar: ");
                    String nombre_fichero = teclado.nextLine();
                    lock.releaseLock(0);

                    foutc.writeObject(new Msg_solicitud_fichero(nombre_fichero,nombre_usuario,"Servidor"));
                    foutc.flush();
                    break;
                case 3:
                    // Enviar mensaje MSG_CERRAR_CONEXION
                    foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario,"Servidor"));
                    foutc.flush();
                    ok = false; //Cerramos este hilo y el hilo OS se queda esperando confirmación   
                    break;
                default:
                    lock.takeLock(0);
                    System.out.println("Opcion no valida");
                    lock.releaseLock(0);
                    break;
            }
        }
        
        teclado.close();
    }
}
