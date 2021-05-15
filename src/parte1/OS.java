package parte1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class OS implements Runnable {
    // Enumerado con las operaciones
    private Socket sc;
    private String nombre_fichero;

    public OS(Socket sc, String nombre_fichero) {
        this.sc = sc;
        this.nombre_fichero = nombre_fichero;
    }

    private void descargarFichero(ObjectInputStream finc) throws IOException {
        // DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream("parte1/ClienteDescargas/" + nombre_fichero);
        
        byte[] buffer = finc.readAllBytes(); // Mucho más elegante

        //System.out.println("read " + buffer.length + " bytes." + fichero.toPath());

        // int filesize = 4096; // Send file size in separate msg
        // int read = 0;
        // int totalRead = 0;
        // int remaining = filesize;
        // while ((read = finc.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
        //     totalRead += read;
        //     remaining -= read;
        //     // System.out.println("read " + totalRead + " bytes.");
        //     fos.write(buffer, 0, read);
        // }

        fos.write(buffer);
        fos.flush();

        fos.close();
    }

    public void run() {
        try {
            // Especificamos por donde escribo y por donde leo en el socket (el cliente)
            ObjectOutputStream foutc = new ObjectOutputStream(sc.getOutputStream());
            ObjectInputStream finc = new ObjectInputStream(sc.getInputStream());

            // Conexion establecida
            System.out.println("[OS]: Conexion establecida con el servidor");

            Mensaje m = new Msg_solicitud_fichero(nombre_fichero);

            foutc.writeObject(m);
            foutc.flush();

            System.out.println("Has pedido el fichero " + m.getNombreFichero());

            // Recibe el fichero CON LA CLASE FICHERO
            // Fichero f = (Fichero) finc.readObject();
            // System.out.println("Fichero recibido: " + f.getNombre());

            Mensaje ok = (Mensaje) finc.readObject();
             if(ok.getTipo() != TipoMensaje.MSG_CONF_FICHERO)
                throw new Exception();

            descargarFichero(finc);
            System.out.println("Fichero recibido: " + m.getNombreFichero());

            foutc.close();
            finc.close();
            sc.close();
            System.out.println("[OS]: Conexion finalizada con el servidor");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[OS]: Error de entrada/salida");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[OS]: Error");
        }
    }
}
