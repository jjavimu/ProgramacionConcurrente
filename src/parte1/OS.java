package parte1;

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

    public void run() {
        try {
            // Especificamos por donde escribo y por donde leo en el socket (el cliente)
            ObjectOutputStream foutc = new ObjectOutputStream(sc.getOutputStream());
            ObjectInputStream finc = new ObjectInputStream(sc.getInputStream());

            // Conexion establecida
            System.out.println("Conexion establecida en Cliente (os)");

            Mensaje m = new Msg_solicitud_fichero(nombre_fichero);

           

            foutc.writeObject(m);
            foutc.flush();

            System.out.println("Has pedido el fichero " + m.getNombreFichero());

            // Recibe el fichero
            Fichero f = (Fichero) finc.readObject();
            System.out.println("Fichero recibido: " + f.getNombre());

            foutc.close();
            finc.close();
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error en OS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
