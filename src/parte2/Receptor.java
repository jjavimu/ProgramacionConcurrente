package parte2;

import java.io.ObjectInputStream;
import java.net.Socket;

public class Receptor implements Runnable { // Funciona como el cliente de la entrega 1
    private int puerto;
    private String IP;
    public Receptor(int puerto,String IP) {
        this.puerto = puerto;
        this.IP = IP;
    }

    public void run() {
        try {

            // Crear Socket
            Socket sr = new Socket(IP, puerto); // Cambiar puertos *****

            // Flujo de entrada del receptor
            ObjectInputStream finr = new ObjectInputStream(sr.getInputStream());

            // Conexion establecida
            System.out.println("Receptor: Conexion establecida con el emisor");

            // Recibe el fichero -------------------------------------
            // byte[] b = finr.readAllBytes(); // finr para ellos es InputStream
	        //Path path = Paths.get(User.BASE_FOLDER + requester.getId() + "/input/" + file_name);
	        //Files.write(path, b);
            //System.out.println("Fichero recibido: " + f.getNombre());
            // guardar el fichero en ClienteDescargas
            // --------------------------------------------------------

            finr.close();
            sr.close();
            System.out.println("Receptor: Conexion finalizada con el servidor");

        } catch (Exception e) {
            System.out.println("Error Receptor");
        }
    }
}