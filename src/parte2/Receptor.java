package parte2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Receptor implements Runnable { // Funciona como el cliente de la entrega 1
    private int puerto;
    private InetAddress IP;
    private String nombre_fichero;
    public Receptor(int puerto,InetAddress IP, String fichero) {
        this.puerto = puerto;
        this.IP = IP;
        this.nombre_fichero = fichero;
    }
    
    private void descargarFichero(ObjectInputStream finc) throws IOException {
        // DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream("parte2/ClienteDescargas/" + nombre_fichero);
        
        byte[] buffer = finc.readAllBytes(); // Mucho más elegante
        fos.write(buffer);
        fos.flush();
        fos.close();

    }
    public void run() {
        try {

            // Crear Socket
            Socket sr = new Socket(IP, puerto); // Cambiar puertos *****
            //System.out.println("[Receptor]: new socket " + IP + " "+ puerto); *** PREGUNTAR PRIMERA PEDIDA NO VA
            // Flujo de entrada del receptor
            ObjectInputStream finr = new ObjectInputStream(sr.getInputStream());

            // Conexion establecida
            // System.out.println("[Receptor]: Conexion establecida con el emisor");
            // Recibe el fichero -------------------------------------
            try{
                descargarFichero(finr);
                // System.out.println("[Receptor]: Fichero " + nombre_fichero + " recibido");
            } catch (IOException e){
                System.out.println("[Receptor]: Error en la descarga del fichero");
            }
            // --------------------------------------------------------

            finr.close();
            sr.close();
            // System.out.println("[Receptor]: Conexion finalizada con el emisor");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Receptor]: Ha ocurrido un error");
        }
    }
}