package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import parte2.Mensajes.*;

public class OC implements Runnable {
    private Socket si;
    // Tabla temporal

    public OC(Socket si) {
        this.si = si;
    }

    public void run() { // Proporcionar concurrencia respecto a las sesiones de cada usuario con el
                        // servidor
        try {
            ObjectOutputStream fout = new ObjectOutputStream(si.getOutputStream());
            ObjectInputStream fin = new ObjectInputStream(si.getInputStream());

            while (true) {

                // Leo flujo de entrada y hago las acciones oportunas
                Mensaje m = (Mensaje) fin.readObject();

                switch (m.getTipo()) {
                    case MSG_CONEXION:
                        // guardar informacion del usuario (en las tablas)
                        // envio mensaje confirmacion conexion fout
                        fout.writeObject(new Msg_confirm_conexion());
                        fout.flush();
                        break;
                    case MSG_LISTA_USUARIOS:
                        // crear un mensaje con la informacion de usuarios en sistema
                        // envio mensaje confirmacion lista usuarios fout
                        fout.writeObject(new Msg_confirm_lista_usuarios());
                        fout.flush();
                        break;
                    case MSG_CERRAR_CONEXION:
                        // eliminar informacion del usuario (en las tablas)
                        // envio mensaje confirmacion cerrar conexion fout
                        fout.writeObject(new Msg_cerrar_conexion());
                        fout.flush();
                        break;
                    case MSG_SOLICITUD_FICHERO:
                        // buscar usuario que contiene el fichero y obtener fout2
                        // envio mensaje MENSAJE_ EMITIR_FICHERO por fout2
                        fout.writeObject(new Msg_emitir_fichero());
                        fout.flush();
                        break;
                    case MSG_PREPARADO_CS:
                        // buscar fout1 (flujo del cliente al que hay que enviar la informacion)
                        // envio fout1 mensaje MENSAJE_PREPARADO_SERVIDORCLIENTE
                        fout.writeObject(new Msg_preparado_sc());
                        fout.flush();
                        break;
                    default:
                        System.out.print("Mensaje no valido");
                        break;
                }

            }

            // Cerrar sockets
         } catch (Exception e) {
            System.out.print("Error OC");
        }

    }

}
