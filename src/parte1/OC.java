package parte1;

import java.net.Socket;
import java.util.List;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public boolean enviarFichero(String file, ObjectOutputStream fout) throws IOException {
        // DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        FileInputStream fis;
        try {
            fis = new FileInputStream("parte1/ServidorRecursos/" + file);

            // Confirmación, puede esperar el fichero. Si no existe no hace el descargarFichero.
            System.out.println("Lo he encontrado");
            Mensaje ok = new Msg_conf_fichero();
            fout.writeObject(ok);
            fout.flush();
    
            byte[] buffer = fis.readAllBytes();

            fout.write(buffer);
            fout.flush();

            fis.close();
            return true;
            
        } catch (FileNotFoundException e) {
            System.out.println("[OC]: El fichero pedido no existe");
            return false;
        }
    }

    public void run() {
        try {
            // Especificamos por donde escribo y por donde leo en el socket (el servidor)
            ObjectOutputStream fout = new ObjectOutputStream(si.getOutputStream());
            ObjectInputStream fin = new ObjectInputStream(si.getInputStream());

            // Conexion establecida
            System.out.println("[OC]: Conexion establecida con el Cliente");

            // Se queda esperando a que le indiquen el nombre del fichero
            Mensaje m = (Mensaje) fin.readObject();

            System.out.println("El cliente me ha pedido el fichero: " + m.getNombreFichero());

            // FICHERO IMPLEMENTADO CON CLASE FICHERO -----------------------------

            // if (m.getTipo() == TipoMensaje.MSG_SOLICITUD_FICHERO) {
            // System.out.print("Buscando..."); // Buscar en la base de datos del servidor
            // el fichero
            // Fichero f_enviar = null;
            // for (Fichero f : listF) { // Enviamos el fichero
            // if (f.getNombre().compareTo(m.getNombreFichero()) == 0) {
            // f_enviar = f;
            // break;
            // }
            // }
            // if (f_enviar != null){
            // System.out.println("Lo he encontrado");
            // fout.writeObject(f_enviar); fout.flush();
            // System.out.println("Se lo he mandado");
            // } else { System.out.println("No he encontrado el fichero " +
            // m.getNombreFichero()); }
            // }

            // FICHERO IMPLEMENTADO CON FILE con función

            if( enviarFichero(m.getNombreFichero(), fout) != false)
                System.out.println("Se lo he mandado");

            // Conexion finalizada
            System.out.println("[OC]: Conexion finalizada con el Cliente");

            fout.close();
            fin.close();
            si.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[OC]: Error de entrada/salida");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[OC]: Error");
        }

    }

}
