package parte1;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    // Hilo Main se encarga de interactuar con el usuario
    public static void main(String[] args) throws IOException {
        
        // Establecemos el teclado
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce el nombre del fichero: ");
        String nombre_fichero = teclado.nextLine();
        teclado.close();
        // Creamos socket con el servidor
        Socket sc = new Socket("localhost",500);
        // Hilo oyente-servidor para comunciarse con el servidor
        (new OS(sc,nombre_fichero)).run();
    }   
}
