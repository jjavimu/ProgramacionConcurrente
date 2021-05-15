package parte1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Servidor {
    private static List<Fichero> listF;
    private static File[] lista_ficheros;
    
    // Hilo main espera comunicaciones
    public static void main(String[] args) throws IOException {
        // Ficheros con la clase fichero --------------------
        // listF = new ArrayList<Fichero>();
        // listF.add(new Fichero("a.txt"));
        // listF.add(new Fichero("b.txt"));
        // listF.add(new Fichero("c.txt"));

        // System.out.println("Iniciando servidor....");
        // System.out.print("Ficheros disponibles: ");
        // System.out.println(listF);
        // System.out.println("-----------------------");

        // Ficheros con ficheros de verdad
        File carpeta_bd = new File("parte1/ServidorRecursos/");
        lista_ficheros = carpeta_bd.listFiles();
        System.out.println("Ficheros disponibles: ");
        for(File f: lista_ficheros){
            System.out.println("- " +f.getName());
        }
        System.out.println("-----------------------");



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