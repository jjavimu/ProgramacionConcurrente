package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import parte2.Mensajes.*;
import parte2.SinCon.*;

import java.util.concurrent.Semaphore;

public class OC implements Runnable {
    // Socket para comunicarse con un cliente en concreto
    private Socket si;

    // Tabla de informacion
    private MUsuarios tablaUsuarios;
    private MFicheros tablaFicheros;
    private Canales tablaCanales;
    private MonitorLectorEscritor monitor_canales;
    private Puerto puerto;
    private Semaphore control_puerto;
    private SemLectorEscritor sem_ficheros;
    private SemLectorEscritor sem_usuarios;

    public OC(Socket si, MUsuarios tablaUsuarios, MFicheros tablaFicheros, Canales tablaCanales, Puerto puerto,
            Semaphore sem, MonitorLectorEscritor monitor_canales, SemLectorEscritor sem_ficheros, SemLectorEscritor sem_usuarios) {
        this.si = si;
        this.tablaFicheros = tablaFicheros;
        this.tablaCanales = tablaCanales;
        this.tablaUsuarios = tablaUsuarios;
        this.control_puerto = sem;
        this.monitor_canales = monitor_canales;
        this.puerto = puerto;
        this.sem_ficheros = sem_ficheros;
        this.sem_usuarios = sem_usuarios;
    }

    public void run() { // Proporcionar concurrencia en las sesiones de cada usuario con el servidor
        try {
            // Flujos de E/S con un cliente en concreto
            ObjectOutputStream fout = new ObjectOutputStream(si.getOutputStream());
            ObjectInputStream fin = new ObjectInputStream(si.getInputStream());

            // Booleano para cerrar conexion con el cliente cuando se desconecta
            boolean ok = true;
            
            while (ok) {

                // Leo flujo de entrada y hago las acciones oportunas
                Mensaje m = (Mensaje) fin.readObject();

                switch (m.getTipo()) {
                    case MSG_CONEXION: {
                        // guardar informacion del usuario (en las tablas)
                        Msg_conexion msg_con = (Msg_conexion) m;
                        String nombre = msg_con.getOrigen();
                        InetAddress ip = msg_con.getIP();

                        sem_usuarios.request_write();
                        boolean conectado = tablaUsuarios.conectarUsuario(nombre,ip);
                        sem_usuarios.release_write();


                        if (conectado) {
                            // Suponemos que no van a conectarse dos personas con el mismo nombre a la vez
                            // Habria que controlar este error

                            // System.out.println(tablaUsuarios.getTabla());

                            monitor_canales.request_write();
                            tablaCanales.setCanales(nombre, fin, fout);
                            monitor_canales.release_write();

                            sem_usuarios.request_read();
                            List<String> ficheros = tablaUsuarios.getFicheros(nombre);
                            sem_usuarios.release_read();

                            sem_ficheros.request_write();
                            tablaFicheros.setFicheros(ficheros, nombre);
                            sem_ficheros.release_write();

                            // envio mensaje confirmacion conexion fout
                            fout.writeObject(new Msg_confirm_conexion("Servidor",nombre));
                            fout.flush();
                        }
                        break;
                    }
                    case MSG_LISTA_USUARIOS: {
                        sem_usuarios.request_read();
                        HashMap<String, List<String>> n_u = tablaUsuarios.getLista(false); // false = solo conectados
                        //System.out.println(n_u);
                        sem_usuarios.release_read();

                        // envio mensaje confirmacion lista usuarios fout
                        fout.writeObject(new Msg_confirm_lista_usuarios(n_u, "Servidor",m.getOrigen()));
                        fout.flush();
                        break;
                    }
                    case MSG_CERRAR_CONEXION: {
                        String nombre_usuario = m.getOrigen();

                        // eliminar informacion del usuario (en las tablas)
                        sem_usuarios.request_write();
                        tablaUsuarios.desconectar(nombre_usuario);
                        sem_usuarios.release_write();


                        monitor_canales.request_write();
                        tablaCanales.desconectar(nombre_usuario);
                        monitor_canales.release_write();
                        
                        sem_ficheros.request_write();
                        tablaFicheros.desconectar(nombre_usuario);
                        sem_ficheros.release_write();

                        // envio mensaje confirmacion cerrar conexion fout               
                        fout.writeObject(new Msg_confirm_cerrar_conexion("Servidor",nombre_usuario));
                        fout.flush();

                        ok = false; // Para salir del while
                        break;
                    }
                    case MSG_SOLICITUD_FICHERO: { 
                        Msg_solicitud_fichero msg3 = (Msg_solicitud_fichero) m;
                        String nombre_fichero = msg3.getNombreFichero();
                        String nombre_receptor = msg3.getOrigen();

                        // Buscar usuario que contiene el fichero
                        sem_ficheros.request_read();
                        String nombre_emisor = tablaFicheros.buscarUsuario(nombre_fichero);
                        sem_ficheros.release_read();
                        
                        if (nombre_emisor != null) {
                            // Suponemos que el fichero va a estar siempre
                            // Dejamos este if para que no nos pete. Habria que hacer control de errores
                            monitor_canales.request_read();
                            ObjectOutputStream fout_emisor = tablaCanales.getCanales(nombre_emisor).getElement1();
                            monitor_canales.release_read();
                
                            control_puerto.acquire();
                            puerto.actualiza();
                            int numero_puerto = puerto.getPuerto();
                            control_puerto.release();
                            
                            // System.out.println("--------------------------------");
                            // System.out.println("[OC] puerto :" + numero_puerto);
                            // System.out.println("[OC] nombre receptor :" + nombre_receptor);
                            // System.out.println("[OC] nombre fichero :" + nombre_fichero);
                            // System.out.println("--------------------------------");

                            
                            fout_emisor.writeObject(new Msg_emitir_fichero(nombre_fichero, nombre_emisor, nombre_receptor,numero_puerto));
                            fout_emisor.flush();
                            
                             // actualizar tabla ficheros suponiendo que emisor-receptor funcion bien
                            sem_ficheros.request_write();
                            tablaFicheros.actualizar(nombre_receptor, nombre_fichero);
                            sem_ficheros.release_write();

                            sem_usuarios.request_write();
                            tablaUsuarios.actualizar(nombre_receptor, nombre_fichero);
                            sem_usuarios.release_write();
                        }
                        break;
                    }
                    case MSG_PREPARADO_CS: {
                        Msg_preparado_cs msg4 = (Msg_preparado_cs) m;
                        int puerto_emisor = msg4.getPuerto();
                        String nombre_rec = msg4.getDestino();
                        String nombre_e = msg4.getOrigen();
                        String file_name = msg4.getNombreFichero();

                        sem_usuarios.request_read();
                        InetAddress IPemisor = tablaUsuarios.getIP(nombre_e); 
                        sem_usuarios.release_read();
                        
                        //System.out.println("--------------------------------");
                        // System.out.println("[OC]: puerto del emisor: " + puerto_emisor);
                        // System.out.println("[OC]: nombre receptor: " + nombre_rec);
                        // System.out.println("[OC]: IP emisor: " + IPemisor);
                        // System.out.println("--------------------------------");

                        // buscar fout1 (flujo del cliente al que hay que enviar la informacion)
                        monitor_canales.request_read();
                        ObjectOutputStream fout_receptor = tablaCanales.getCanales(nombre_rec).getElement1();
                        monitor_canales.release_read();

                        fout_receptor.writeObject(new Msg_preparado_sc(puerto_emisor, IPemisor, file_name, "Servidor",nombre_rec)); 
                        fout_receptor.flush();
                        break;
                    }
                    default:
                        System.out.println("[OC]: Mensaje no valido");
                        break;
                }

            }

            // Cerrar sockets
            fout.close();
            fin.close();
            si.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[OC]: Error desconexión abrupta OC");
        }
    }
}
