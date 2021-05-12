package parte1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Servidor {
    private static List<Fichero> listF;
    
    // Hilo main espera comunicaciones
    public static void main(String[] args) throws IOException {
        // Ficheros
        listF = new ArrayList<Fichero>();
        listF.add(new Fichero("a.txt"));

        System.out.println("Servidor creado");
        System.out.print("Ficheros disponibles: ");
        System.out.println(listF);


        // Creo el socket del servidor
        ServerSocket ss = new ServerSocket(500);

        while(true){
            // Me quedo esperando a que otros nodos distribuidos se quieran comunicar conmigo
            Socket si = ss.accept();

            // Creamos thread oyente-cliente para escuchar al cliente i y comunicarse con el
            (new OC(si,listF)).run();
        }
    }
}