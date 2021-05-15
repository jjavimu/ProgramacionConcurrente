package parte2.Mensajes;

public enum TipoMensaje {
    MSG_CONEXION, 
    MSG_CONFIRM_CONEXION, 
    MSG_LISTA_USUARIOS, 
    MSG_CONFIRM_LISTA_USUARIOS,  // lista de usuarios
    MSG_SOLICITUD_FICHERO, // nombre fichero
    MSG_EMITIR_FICHERO, // nombre fichero, nombre cliente que lo quiere
    MSG_PREPARADO_CS, // direccion Ip y puerto del propietario de fichero
    MSG_PREPARADO_SC,
    MSG_CERRAR_CONEXION, 
    MSG_CONFIRM_CERRAR_CONEXION
}
