package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Semaphore;

import parte2.Mensajes.*;
import parte2.SinCon.*;

public class OS implements Runnable {
    private ObjectOutputStream foutc;
    private ObjectInputStream finc;
    private Socket sc;
    private Lock lock;
    private Semaphore control_fout;

    public OS(Socket sc, ObjectInputStream finc, ObjectOutputStream foutc, Lock lock, Semaphore control_fout) {
        this.sc = sc;
        this.finc = finc;
        this.foutc = foutc;
        this.lock = lock;
        this.control_fout = control_fout;
    }

    public void run() { // Escucha continua del canal con el servidor
        try {
            // Booleano para cerrar conexion
            boolean ok = true;
            while (ok) {
                
                Mensaje m = (Mensaje) finc.readObject();
                

                switch (m.getTipo()) {
                    case MSG_CONFIRM_CONEXION:
                        // imprimir conexion establecida por standard output
                        lock.takeLock(1);
                        System.out.println("OS: Conexion establecida");
                        lock.releaseLock(1);
                        break;
                    case MSG_CONFIRM_LISTA_USUARIOS:
                        // imprimir lista usuarios por standard output
                        Msg_confirm_lista_usuarios msg1 = (Msg_confirm_lista_usuarios) m;
                        List<Usuario> lista = msg1.getLista();
                        lock.takeLock(1);
                        System.out.println("PRUEBA : OS - sacar lista");
                        System.out.println(lista);
                        lock.releaseLock(1);
                        break;
                    case MSG_EMITIR_FICHERO:
                        // El servidor me avisa de que otro cliente quiere un fichero que yo tengo
                        Msg_emitir_fichero msg2 = (Msg_emitir_fichero) m;
                        String nombre_fichero = msg2.getFichero();
                        String nombre_receptor = msg2.getNombreReceptor();
                        String nombre_emisor = msg2.getNombreEmisor();
                        int puerto = msg2.getPuerto();

                        // enviar MENSAJE_PREPARADO_CLIENTESERVIDOR a mi oyente
                        control_fout.acquire();
                        foutc.writeObject(new Msg_preparado_cs(nombre_emisor,nombre_receptor,puerto));
                        foutc.flush();
                        control_fout.release();
                        lock.takeLock(1);
                        System.out.println("Emitir fichero :");
                        System.out.println("OS puerto :" + puerto);
                        System.out.println("OS nombre receptor :" + nombre_receptor);
                        System.out.println("OS nombre fichero :" + nombre_fichero);
                        lock.releaseLock(1);
                        // Crear proceso EMISOR y espero en accept la conexion
                        new Thread((new Emisor(nombre_fichero,puerto))).start();
                        break;
                    case MSG_PREPARADO_SC:
                        // El servidor me avisa de que el cliente que tiene el fichero que quiero esta
                        // listo
                        // Llega direccion Ip y puerto del propietario de fichero
                        Msg_preparado_sc msg3 = (Msg_preparado_sc) m;
                        int puerto_emisor = msg3.getPuerto();
                        String IPemisor = msg3.getIP();

                        // Crear proceso RECEPTOR
                        (new Thread((new Receptor(puerto_emisor,IPemisor)))).start();
                        break;
                    case MSG_CONFIRM_CERRAR_CONEXION:
                        // imprimir adios por standard output
                        lock.takeLock(1);
                        System.out.println("OS: Conexion cerrada. Adios :)");
                        lock.releaseLock(1);
                        ok = false;
                        break;
                    default:
                        lock.takeLock(1);
                        System.out.println("Mensaje no valido");
                        lock.releaseLock(1);
                        break;
                }
            }

            // Cerramos el socket
            finc.close();
            foutc.close();
            sc.close();

        } catch (Exception e) {
            System.out.println("Error OS");
        }
    }


}
