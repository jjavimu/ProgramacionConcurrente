package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import parte2.Mensajes.*;
import parte2.SinCon.MCanales;
import parte2.SinCon.MFicheros;
import parte2.SinCon.MUsuarios;
import java.util.concurrent.Semaphore;

public class OC implements Runnable {
    // Socket para comunicarse con un cliente en concreto
    private Socket si;

    // Tabla de informacion
    private MUsuarios tablaUsuarios;
    private MFicheros tablaFicheros;
    private MCanales tablaCanales;
    private int puerto;
    private Semaphore control_puerto;

    public OC(Socket si, MUsuarios tablaUsuarios, MFicheros tablaFicheros, MCanales tablaCanales, int puerto,
            Semaphore sem) {
        this.si = si;
        this.tablaFicheros = tablaFicheros;
        this.tablaCanales = tablaCanales;
        this.tablaUsuarios = tablaUsuarios;
        this.control_puerto = sem;
        this.puerto = puerto;

    }

    public void run() { // Proporcionar concurrencia en las sesiones de cada usuario con el servidor
        try {
            ObjectOutputStream fout = new ObjectOutputStream(si.getOutputStream());
            ObjectInputStream fin = new ObjectInputStream(si.getInputStream());

            // Booleano para cerrar conexion con el cliente cuando se desconecta
            boolean ok = true;

            while (ok) {

                // Leo flujo de entrada y hago las acciones oportunas
                Mensaje m = (Mensaje) fin.readObject();

                switch (m.getTipo()) {
                    case MSG_CONEXION:
                        // guardar informacion del usuario (en las tablas)
                        Msg_conexion msg1 = (Msg_conexion) m;
                        String nombre = msg1.getNombreUsuario();
                        if (!tablaUsuarios.conectarUsuario(nombre)) {
                            // Ya hay un usuario que ha iniciado sesion con este nombre
                            System.out.println("OC : El cliente no puede conectarse porque ya hay uno conectado");
                        } else {
                            tablaCanales.setCanales(nombre, fin, fout);
                            List<String> ficheros = tablaUsuarios.getFicheros(nombre);
                            tablaFicheros.setFicheros(ficheros, nombre);
                            // envio mensaje confirmacion conexion fout
                            fout.writeObject(new Msg_confirm_conexion());
                            fout.flush();
                        }
                        break;
                    case MSG_LISTA_USUARIOS:
                        // crear un mensaje con la informacion de usuarios en sistema
                        List<Usuario> usuarios = tablaUsuarios.getListaUsuariosConectados();
                        // envio mensaje confirmacion lista usuarios fout
                        fout.writeObject(new Msg_confirm_lista_usuarios(usuarios));
                        fout.flush();
                        break;
                    case MSG_CERRAR_CONEXION:
                        // eliminar informacion del usuario (en las tablas)
                        Msg_cerrar_conexion msg2 = (Msg_cerrar_conexion) m;
                        tablaUsuarios.desconectar(msg2.getNombreUsuario());
                        tablaCanales.desconectar(msg2.getNombreUsuario());
                        tablaFicheros.desconectar(msg2.getNombreUsuario());
                        // envio mensaje confirmacion cerrar conexion fout
                        fout.writeObject(new Msg_confirm_cerrar_conexion());
                        fout.flush();
                        ok = false; // Para salir del while
                        break;
                    case MSG_SOLICITUD_FICHERO:
                        // Buscar usuario que contiene el fichero
                        Msg_solicitud_fichero msg3 = (Msg_solicitud_fichero) m;
                        String nombre_fichero = msg3.getNombreFichero();
                        String nombre_emisor = tablaFicheros.buscarUsuario(nombre_fichero);
                        String nombre_receptor = msg3.getNombreUsuario();
                        if (nombre_emisor == null) {
                            System.out.println("El fichero no lo tiene nadie conectado");
                        } else {
                            ObjectOutputStream fout_emisor = tablaCanales.getCanales(nombre_emisor).getElement1();
                
                            control_puerto.acquire();
                            int numero_puerto = puerto;
                            puerto = puerto + 1;
                            control_puerto.release();
                            
                            fout_emisor.writeObject(new Msg_emitir_fichero(msg3.getNombreFichero(), nombre_receptor,numero_puerto)); // puerto
                            fout_emisor.flush();
                             // actualizar tabla ficheros suponiendo que emisor-receptor funcion bien ------------------
                            tablaFicheros.actualizar(nombre_receptor, nombre_fichero);
                            tablaUsuarios.actualizar(nombre_receptor, nombre_fichero);
                        }
                        break;
                    case MSG_PREPARADO_CS:
                        // buscar fout1 (flujo del cliente al que hay que enviar la informacion)
                        // envio fout1 mensaje MENSAJE_PREPARADO_SERVIDORCLIENTE
                        Msg_preparado_cs msg4 = (Msg_preparado_cs) m;
                        int puerto_emisor = msg4.getPuerto();
                        String nombre_usuario = msg4.getNombreUsuario();
                        String IPemisor = tablaUsuarios.getIP(nombre_usuario);

                        ObjectOutputStream fout_receptor = tablaCanales.getCanales(nombre_usuario).getElement1();

                        fout_receptor.writeObject(new Msg_preparado_sc(puerto_emisor,IPemisor)); 
                        fout_receptor.flush();
                        break;
                    default:
                        System.out.print("Mensaje no valido");
                        break;
                }

            }

            // Cerrar sockets
            fout.close();
            fin.close();
            si.close();
        } catch (Exception e) {
            System.out.print("Error OC");
        }
    }
}
