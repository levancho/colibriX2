  package com.despani.core.exception.handlers;

  import org.springframework.http.HttpHeaders;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.ControllerAdvice;
  import org.springframework.web.bind.annotation.ExceptionHandler;
  import org.springframework.web.context.request.WebRequest;
  import org.springframework.web.servlet.config.annotation.EnableWebMvc;
  import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionController extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = { OXStripesException.class,OXTransactionException.class,OXTreepexException.class,OXUserException.class})
//    protected ResponseEntity<Object> handleConflict(OXABaseException ex, WebRequest request) {
//        String bodyOfResponse = ex.getValue();
//        HttpHeaders h =  new HttpHeaders();
//        h.add("code",ex.getCode()+"");
//        h.add("key",ex.getKey());
//        return handleExceptionInternal(ex, ex.getExtType().toJSON(),h
//                , HttpStatus.BAD_REQUEST, request);
//    }
//
//    @ExceptionHandler(value = {OXAuthenticationException.class})
//    protected ResponseEntity<Object> handleConflict2(OXABaseRuntimeException ex, WebRequest request) {
//        String bodyOfResponse = ex.getValue();
//        HttpHeaders h =  new HttpHeaders();
//        h.add("code",ex.getCode()+"");
//        h.add("key",ex.getKey());
//        return handleExceptionInternal(ex, ex.getExtType().toJSON(),h
//                , HttpStatus.FORBIDDEN, request);
//    }
//
//
//    @ExceptionHandler({AuthenticationException.class })
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ResponseBody
//    protected ResponseEntity<Object> handleUauthorized(Exception ex, WebRequest request) {
//        String bodyOfResponse = ex.getMessage();
//        HttpHeaders h =  new HttpHeaders();
//        h.add("code",OXAuthenticationExcTypes.OX_UNAUTHORIZED.getCode()+"");
//        h.add("key",OXAuthenticationExcTypes.OX_UNAUTHORIZED.getKey());
//        return handleExceptionInternal(ex, OXAuthenticationExcTypes.OX_UNAUTHORIZED.toJSON(),h
//                , HttpStatus.UNAUTHORIZED, request);
//    }

    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    /*500*/public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        final String bodyOfResponse = "Sever Error Happened";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
