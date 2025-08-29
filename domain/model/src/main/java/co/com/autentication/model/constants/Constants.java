package co.com.autentication.model.constants;

public class Constants {
    private Constants() {}
    public static final String LOG_USER_REQUEST_RECEIVED = "Solicitud recibida para crear usuario, traceId: {}";
    public static final String LOG_USER_CREATED_SUCCESS = "Usuario creado con éxito id={}, traceId={}";
    public static final String LOG_USER_FIND_BY_DOCUMENT = "Buscando usuario con documento={}, traceId={}";
    public static final String LOG_USER_FOUND_BY_DOCUMENT = "Usuario encontrado con id={}, traceId={}";
    public static final String LOG_USER_RESPONSE_SENT = "Respuesta enviada con status={}, traceId={}";
    public static final String LOG_USER_ERROR_PROCESSING = "Error procesando usuario: {}, traceId={}, detalle: {}";
    public static final String LOG_USER_REPO_START_SAVE = "Iniciando guardado de usuario con email={}, traceId={}";
    public static final String LOG_IDENTITY_NOT_FOUND = "Usuario no encontrado con documento: {}, traceId: {}";
    public static final String LOG_USER_REPO_SAVED_SUCCESS = "Usuario guardado con éxito con id: {}, traceId: {}";
    public static final String LOG_USER_REPO_ERROR_SAVING = "Error guardando usuario en repositorio, mensaje: {}, traceId: {}";

}
