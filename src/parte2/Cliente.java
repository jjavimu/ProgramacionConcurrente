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
        System.out.print("Introduce el nombre de usuario: ");
        String nombre_usuario = teclado.nextLine();

        // Creamos socket con el servidor
        Socket sc = new Socket(IP, 1); // Puerto -------------------------------------------
        ObjectOutputStream foutc = new ObjectOutputStream(sc.getOutputStream());
        ObjectInputStream finc = new ObjectInputStream(sc.getInputStream());

        // Creamos hilo oyente-servidor
        // Le enviamos el cerrojo necesario para la sincronizacion de la salida por pantalla
        Lock lock = new LockBakery(2); // 0-Cliente , 1-OS
        Semaphore control_fout = new Semaphore(1);
        (new Thread(new OS(sc,finc,foutc,lock,control_fout))).start();
        
        System.out.println("hola");
        
        // Enviar mensaje de conexion al servidor
        control_fout.acquire();
        foutc.writeObject(new Msg_conexion(nombre_usuario));
        foutc.flush();
        control_fout.release();

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

            switch (opcion) {
                case 1:
                    // Enviar mensaje MSG_LISTA_USUARIOS
                    control_fout.acquire();
                    foutc.writeObject(new Msg_lista_usuarios());
                    foutc.flush();
                    control_fout.release();
                    break;
                case 2:
                    // (se deben permitir otras acciones y descargas simultaneas)
                    // Enviar mensaje MSG_SOLICITUD_FICHERO
                    lock.takeLock(0); 
                    System.out.print("Indica el nombre del fichero que quieres descargar: ");
                    lock.releaseLock(0);

                    String nombre_fichero = teclado.nextLine();
                    control_fout.acquire();
                    foutc.writeObject(new Msg_solicitud_fichero(nombre_fichero,nombre_usuario));
                    foutc.flush();
                    control_fout.release();
                    break;
                case 3:
                    // Enviar mensaje MSG_CERRAR_CONEXION
                    control_fout.acquire();
                    foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario));
                    foutc.flush();
                    control_fout.release();
                    ok = false; //Cerramos este hilo y el hilo OS se queda esperando confirmación   
                    break;
                default:
                    lock.takeLock(0); 
                    System.out.print("Opcion no valida");
                    lock.releaseLock(0);
                    break;
            }
        }
        teclado.close();
    }
}
