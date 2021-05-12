package parte1;

import java.net.Socket;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OC implements Runnable {
    // Enumerado con las operaciones
    private Socket si;
    private List<Fichero> listF;

    public OC(Socket si, List<Fichero> listF) {
        this.si = si;
        this.listF = listF;
    }

    public void run() {
        try {
            // Especificamos por donde escribo y por donde leo en el socket (el servidor)
            ObjectOutputStream fout = new ObjectOutputStream(si.getOutputStream());
            ObjectInputStream fin = new ObjectInputStream(si.getInputStream());

            // Conexion establecida
            System.out.println("Conexion establecida en Servidor (oc)");

            // Se queda esperando a que le indiquen el nombre del fichero
            Mensaje m = (Mensaje) fin.readObject();

            System.out.println("El cliente me ha pedido el fichero: " + m.getNombreFichero());            

            if (m.getTipo() == TipoMensaje.MSG_SOLICITUD_FICHERO) {
                System.out.println("Lo voy a buscar"); 
                // Buscar en la base de datos del servidor el fichero
                for (Fichero f : listF) {
                    // Enviamos el fichero
                    System.out.println("Buscando..."); 
                    if (f.getNombre().compareTo(m.getNombreFichero()) == 0) {
                        System.out.println("Lo he encontrado"); 
                        fout.writeObject(f);
                        fout.flush();
                        System.out.println("Se lo he mandado"); 
                    }
                }

            }

            fout.close();
            fin.close();
            si.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error en OC");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
