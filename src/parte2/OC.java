package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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

    public OC(Socket si, MUsuarios tablaUsuarios, MFicheros tablaFicheros, Canales tablaCanales, Puerto puerto,
            Semaphore sem, MonitorLectorEscritor monitor_canales) {
        this.si = si;
        this.tablaFicheros = tablaFicheros;
        this.tablaCanales = tablaCanales;
        this.tablaUsuarios = tablaUsuarios;
        this.control_puerto = sem;
        this.monitor_canales = monitor_canales;
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
                    case MSG_CONEXION: {
                        // guardar informacion del usuario (en las tablas)
                        Msg_conexion msg1 = (Msg_conexion) m;
                        String nombre = msg1.getNombreUsuario();

                        if (!tablaUsuarios.conectarUsuario(nombre)) {
                            // Ya hay un usuario que ha iniciado sesion con este nombre
                            System.out.println("[OC] : El cliente no puede conectarse porque ya hay uno conectado");
                        } else {
                            // System.out.println(tablaUsuarios.getTabla());

                            monitor_canales.request_write();
                            tablaCanales.setCanales(nombre, fin, fout);
                            monitor_canales.release_write();

                            List<String> ficheros = tablaUsuarios.getFicheros(nombre);
                            
                            tablaFicheros.setFicheros(ficheros, nombre);

                            // envio mensaje confirmacion conexion fout
                            fout.writeObject(new Msg_confirm_conexion());
                            fout.flush();
                        }
                        break;
                    }
                    case MSG_LISTA_USUARIOS: {
                        // crear un mensaje con la informacion de usuarios en sistema
                        //List<Usuario> usuarios = tablaUsuarios.getListaUsuariosConectados();

                        /*List<Usuario> enviar = new ArrayList<>();
                        for(Usuario u : usuarios){
                            enviar.add(new Usuario(u.getNombre(), u.getDirIP(), u.getConectado(), u.getFicheros()));
                        }*/
                       
                        /*System.out.println("PRUEBA : OC - Lista");
                        List<String> lista_ficheros = tablaFicheros.getLista();
                        System.out.println(lista_ficheros);

                        System.out.println("PRUEBA : OC - Lista de string en vez de usuarios");*/
                        HashMap<String, List<String>> n_u = tablaUsuarios.getLista(false); // false = solo conectados
                        //System.out.println(n_u);
                    
                        // envio mensaje confirmacion lista usuarios fout
                        fout.writeObject(new Msg_confirm_lista_usuarios(n_u));
                        fout.flush();
                        break;
                    }
                    case MSG_CERRAR_CONEXION: {
                        // eliminar informacion del usuario (en las tablas)
                        Msg_cerrar_conexion msg2 = (Msg_cerrar_conexion) m;
                        String nombre_usuario = msg2.getNombreUsuario();

                        tablaUsuarios.desconectar(nombre_usuario);

                        monitor_canales.request_write();
                        tablaCanales.desconectar(nombre_usuario);
                        monitor_canales.release_write();
                        
                        tablaFicheros.desconectar(nombre_usuario);

                        // envio mensaje confirmacion cerrar conexion fout               
                        fout.writeObject(new Msg_confirm_cerrar_conexion());
                        fout.flush();

                        ok = false; // Para salir del while
                        break;
                    }
                    case MSG_SOLICITUD_FICHERO: { 
                        // Buscar usuario que contiene el fichero
                        Msg_solicitud_fichero msg3 = (Msg_solicitud_fichero) m;
                        String nombre_fichero = msg3.getNombreFichero();
                        String nombre_emisor = tablaFicheros.buscarUsuario(nombre_fichero);
                        String nombre_receptor = msg3.getNombreUsuario();
                        
                        if (nombre_emisor == null) {
                            System.out.println("[OC]: El fichero " + nombre_fichero + " no lo tiene nadie conectado");
                        } else {
                            monitor_canales.request_read();
                            ObjectOutputStream fout_emisor = tablaCanales.getCanales(nombre_emisor).getElement1();
                            monitor_canales.release_read();
                
                            control_puerto.acquire();
                            puerto.actualiza();
                            int numero_puerto = puerto.getPuerto();
                            control_puerto.release();
                            
                            System.out.println("--------------------------------");
                            System.out.println("[OC] puerto :" + numero_puerto);
                            System.out.println("[OC] nombre receptor :" + nombre_receptor);
                            System.out.println("[OC] nombre fichero :" + nombre_fichero);
                            System.out.println("--------------------------------");

                            
                            fout_emisor.writeObject(new Msg_emitir_fichero(nombre_fichero, nombre_emisor, nombre_receptor,numero_puerto)); // puerto
                            fout_emisor.flush();
                            
                             // actualizar tabla ficheros suponiendo que emisor-receptor funcion bien ------------------
                            tablaFicheros.actualizar(nombre_receptor, nombre_fichero);
                            tablaUsuarios.actualizar(nombre_receptor, nombre_fichero);
                        }
                        break;
                    }
                    case MSG_PREPARADO_CS: {
                        // buscar fout1 (flujo del cliente al que hay que enviar la informacion)
                        // envio fout1 mensaje MENSAJE_PREPARADO_SERVIDORCLIENTE
                        Msg_preparado_cs msg4 = (Msg_preparado_cs) m;
                        int puerto_emisor = msg4.getPuerto();
                        String nombre_rec = msg4.getNombreReceptor();
                        String nombre_e = msg4.getNombreEmisor();
                        String file_name = msg4.getNombreFichero();
                        
                        String IPemisor = tablaUsuarios.getIP(nombre_e); //-----------------------------
                        
                        System.out.println("--------------------------------");
                        System.out.println("[OC]: puerto del emisor: " + puerto_emisor);
                        System.out.println("[OC]: nombre receptor: " + nombre_rec);
                        System.out.println("[OC]: IP emisor: " + IPemisor);
                        System.out.println("--------------------------------");
                        
                        monitor_canales.request_read();
                        ObjectOutputStream fout_receptor = tablaCanales.getCanales(nombre_rec).getElement1();
                        monitor_canales.release_read();

                        fout_receptor.writeObject(new Msg_preparado_sc(puerto_emisor, IPemisor, file_name)); 
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
            System.out.println("[OC]: Error OC");
        }
    }
}
