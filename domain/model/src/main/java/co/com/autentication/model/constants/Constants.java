package co.com.autentication.model.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String LOG_USER_REQUEST_RECEIVED = "Solicitud recibida para crear usuario, traceId: {}";
    public final String LOG_USER_CREATED_SUCCESS = "Usuario creado con éxito id={}, traceId={}";
    public final String LOG_USER_FIND_BY_DOCUMENT = "Buscando usuario con documento={}, traceId={}";
    public final String LOG_USER_FOUND_BY_DOCUMENT = "Usuario encontrado con id={}, traceId={}";
    public final String LOG_USER_RESPONSE_SENT = "Respuesta enviada con status={}, traceId={}";
    public final String LOG_USER_ERROR_PROCESSING = "Error procesando usuario: {}, traceId={}, detalle: {}";
    public final String LOG_USER_REPO_START_SAVE = "Iniciando guardado de usuario con email={}, traceId={}";
    public final String LOG_IDENTITY_NOT_FOUND = "Usuario no encontrado con documento: {}, traceId: {}";
    public final String LOG_USER_REPO_SAVED_SUCCESS = "Usuario guardado con éxito con id: {}, traceId: {}";
    public final String LOG_USER_REPO_ERROR_SAVING = "Error guardando usuario en repositorio, mensaje: {}, traceId: {}";
    public final String LOG_USER_FOUND_BY_EMAIL = "Usuario encontrado con email={}, traceId={}";
    public final String LOG_USER_ERROR_FIND_BY_EMAIL = "Error al buscar usuario con email={}, traceId={}, detalle={}";
    public final String LOG_ROLE_NOT_FOUND = "No se encontro el rol con id:{}, traceId:{}";

    public final String LOG_LOGIN_REQUEST_RECEIVED = "Solicitud de login recibida para usuario={}, traceId={}";
    public final String LOG_USER_FIND_BY_EMAIL = "Iniciando búsqueda de usuario con email={}, traceId={}";
    public final String LOG_LOGIN_SUCCESS = "Login exitoso para usuario={}, traceId={}";
    public final String LOG_LOGIN_ERROR = "Error en login: {}, traceId={}";
    public final String LOG_LOGIN_INVALID_CREDENTIALS = "Credenciales inválidas para email={}, traceId={}";

}
