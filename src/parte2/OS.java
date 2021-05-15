package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import parte2.Mensajes.*;

public class OS implements Runnable {
    private Socket sc;
    private ObjectOutputStream foutc;
    private ObjectInputStream finc;

    public OS(Socket sc, ObjectInputStream finc, ObjectOutputStream foutc) {
        this.sc = sc;
        this.finc = finc;
        this.foutc = foutc;
    }

    public void run() { // Escucha continua del canal con el servidor

        try {

            while (true) {
                Mensaje m = (Mensaje) finc.readObject();

                switch (m.getTipo()) {
                    case MSG_CONFIRM_CONEXION:
                        // imprimir conexion establecida por standard output
                        System.out.println("OS: Conexion establecida");
                        break;
                    case MSG_CONFIRM_LISTA_USUARIOS:
                        // imprimir lista usuarios por standard output
                        Msg_confirm_lista_usuarios msg1 = (Msg_confirm_lista_usuarios) m;
                        System.out.println(msg1.getLista()); // --------- hacer bonito
                        break;
                    case MSG_EMITIR_FICHERO:
                        // El servidor me avisa de que otro cliente quiere un fichero que yo tengo
                        // Llega nombre de cliente C1 e informacion pedida
                        Msg_emitir_fichero msg2 = (Msg_emitir_fichero) m;
                        String nombre_cliente = msg2.getCliente();
                        String nombre_fichero = msg2.getFichero();

                        // enviar MENSAJE_PREPARADO_CLIENTESERVIDOR a mi oyente
                        foutc.writeObject(new Msg_preparado_cs());
                        foutc.flush();

                        // Crear proceso EMISOR y espero en accept la conexion
                        (new Emisor(nombre_fichero)).run();
                        break;
                    case MSG_PREPARADO_SC:
                        // El servidor me avisa de que el cliente que tiene el fichero que quiero esta listo
                        // Llega direccion Ip y puerto del propietario de fichero
                        Msg_preparado_sc msg3 = (Msg_preparado_sc) m;

                        // Crear proceso RECEPTOR
                        (new Receptor()).run();
                        break;
                    case MSG_CONFIRM_CERRAR_CONEXION:
                        // imprimir adios por standard output
                        System.out.println("OS: Conexion cerrada. Adios :)");
                        break;
                    default:
                        System.out.print("Mensaje no valido");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.print("Error OS");
        }

        // Hay que cerrar el fout
    }

}
