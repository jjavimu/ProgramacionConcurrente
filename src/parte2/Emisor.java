package parte2;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Emisor implements Runnable { // Funciona como el servidor de la entrega 1
    private String nombre_fichero;
    private int puerto;

    public Emisor(String nombre_fichero, int puerto) {
        
        this.nombre_fichero = nombre_fichero;
        this.puerto = puerto;
    }
    
    public boolean enviarFichero(ObjectOutputStream fout) throws IOException {
        // DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        FileInputStream fis;
        try {
            fis = new FileInputStream("parte2/ClienteRecursos/" + nombre_fichero);
            byte[] buffer = fis.readAllBytes();
            fout.write(buffer);
            fout.flush();
            fis.close();
            
            return true;
            
        } catch (FileNotFoundException e) {
            System.out.println("[Emisor]: El fichero pedido no existe");
            return false;
        }
    }
    
    
    public void run() {
        try {
            
            // Crear ServerSocket
            ServerSocket es = new ServerSocket(puerto);
            
            // Me quedo esperando a la peticion del receptor
            System.out.println("[Emisor]: ServerSocket creado y antes de accept");
            Socket si = es.accept();

            // Accedo flujo de salida del emisor
            ObjectOutputStream fout = new ObjectOutputStream(si.getOutputStream());

            // Conexion establecida
            System.out.println("[Emisor]: Conexion establecida con el Receptor");

            
            
            // -----------------------------------------------------------
            System.out.println("[Emisor]: Buscando " + nombre_fichero + " ...");
            try {
                if( enviarFichero(fout) != false)
                    System.out.println("[Emisor]: Fichero "  + nombre_fichero + " enviado");
            } catch (IOException e) {
                System.out.println("[Emisor]: Error en el envío del fichero " + nombre_fichero);
            }

            /*File fichero = new File("src/parte2/ClienteRecursos/" + this.nombre_fichero); // ruta
            byte[] contenido = Files.readAllBytes(fichero.toPath());
            
            // Escribir informacion por salida
            fout.write(contenido); // out es outputStream
            fout.flush();*/
            // ------------------------------------------------------------

            // Conexion finalizada
            System.out.println("[Emisor]: Conexion finalizada con el Receptor");

            fout.close();
            si.close();
            es.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Emisor]: Error. Puerto: "+ puerto);
        }

    }

}