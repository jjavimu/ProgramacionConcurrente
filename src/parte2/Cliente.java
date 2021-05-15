package parte2;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import parte2.Mensajes.*;

public class Cliente {
    // Nombre usuario
    // Dir ip de la maquina
    // Socket y flujos con el servidor

    public static void main(String[] args) throws Exception { // main : interactuar con el usuario

        // Preguntar el nombre del usuario
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce el nombre de usuario: ");
        String nombre_usuario = teclado.nextLine();

        // Creamos socket con el servidor
        Socket sc = new Socket("localhost", 500);
        ObjectOutputStream foutc = new ObjectOutputStream(sc.getOutputStream());
        ObjectInputStream finc = new ObjectInputStream(sc.getInputStream());

        (new OS(sc,finc,foutc)).run();
        

        // Enviar mensaje de conexion al servidor
        foutc.writeObject(new Msg_conexion(nombre_usuario,finc,foutc));
        foutc.flush();

        while (true) {
            System.out.print("Menu de acciones (elige un numero): ");
            System.out.print("1 - Consultar el nombre de los usuarios conectados y su informacion");
            System.out.print("2 - Descargar informacion");
            System.out.print("3 - Desconectarse del servidor");
            int opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                    // Enviar mensaje MSG_LISTA_USUARIOS
                    foutc.writeObject(new Msg_lista_usuarios());
                    foutc.flush();
                    break;
                case 2:
                    // (se deben permitir otras acciones y descargas simultaneas)
                    // Enviar mensaje MSG_SOLICITUD_FICHERO
                    System.out.print("Indica el nombre del fichero que quieres descargar");
                    String nombre_fichero = teclado.nextLine();
                    foutc.writeObject(new Msg_solicitud_fichero(nombre_fichero));
                    foutc.flush();
                    break;
                case 3:
                    // Enviar mensaje MSG_CERRAR_CONEXION
                    foutc.writeObject(new Msg_cerrar_conexion());
                    foutc.flush();
                    break;
                default:
                    System.out.print("Opcion no valida");
                    break;
            }
            // Indicar fin de sesion al terminar
        }

        // Cerramos los objetos de E/S
        teclado.close();
        foutc.close();
        sc.close();
    }
}
