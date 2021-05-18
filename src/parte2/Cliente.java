package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import parte2.Mensajes.*;
import parte2.SinCon.*;

public class Cliente{
    // Nombre usuario
    // Dir ip de la maquina
    // Socket y flujos con el servidor

    public static void main(String[] args) throws Exception { // main : interactuar con el usuario
        String IP = "localhost"; // IP del servidor  

        // Preguntar el nombre del usuario
        Scanner teclado = new Scanner(System.in);
        System.out.println("Introduce el nombre de usuario: ");
        String nombre_usuario = teclado.nextLine();

        // Creamos socket con el servidor
        Socket sc = new Socket(IP, 1); // Puerto -------------------------------------------
        ObjectOutputStream foutc = new ObjectOutputStream(sc.getOutputStream());
        ObjectInputStream finc = new ObjectInputStream(sc.getInputStream());

        // Creamos hilo oyente-servidor
        // Le enviamos el cerrojo necesario para la sincronizacion de la salida por pantalla
        Lock lock = new LockBakery(2); // 0-Cliente , 1-OS
        (new Thread(new OS(sc,finc,foutc,lock))).start();
        
        // Enviar mensaje de conexion al servidor

        foutc.writeObject(new Msg_conexion(nombre_usuario));
        foutc.flush();

        // Booleano para cerrar conexion
        boolean ok = true;

        while (ok) {

            //lock.takeLock(0); 
            System.out.println("Menu de acciones (elige un numero): ");
            System.out.println("1 - Consultar el nombre de los usuarios conectados y su informacion");
            System.out.println("2 - Descargar informacion");
            System.out.println("3 - Desconectarse del servidor");
            //lock.releaseLock(0);

            int opcion = teclado.nextInt();     
            String sucio = teclado.nextLine();

            switch (opcion) {
                case 1:
                    // Enviar mensaje MSG_LISTA_USUARIOS
                    foutc.writeObject(new Msg_lista_usuarios());
                    foutc.flush();
                    //lock.takeLock(0); 
                    System.out.println("PRUEBA : Cliente - lista");
                    //lock.releaseLock(0);
                    break;
                case 2:
                    // (se deben permitir otras acciones y descargas simultaneas)
                    // Enviar mensaje MSG_SOLICITUD_FICHERO
                    //lock.takeLock(0); 
                    System.out.println("Indica el nombre del fichero que quieres descargar: ");
                    String nombre_fichero = teclado.nextLine();
                    //lock.releaseLock(0);

                    foutc.writeObject(new Msg_solicitud_fichero(nombre_fichero,nombre_usuario));
                    foutc.flush();
                    break;
                case 3:
                    // Enviar mensaje MSG_CERRAR_CONEXION
                    foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario));
                    foutc.flush();
                    ok = false; //Cerramos este hilo y el hilo OS se queda esperando confirmación   
                    break;
                default:
                    //lock.takeLock(0); 
                    System.out.println("Opcion no valida");
                    //lock.releaseLock(0);
                    break;
            }
        }
        teclado.close();
    }
}
