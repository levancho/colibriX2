  package com.despani.core.utils;


import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

  public class OXUtils {

    static Logger log = LoggerFactory.getLogger(OXUtils.class);

      private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
      private static final Pattern WHITESPACE = Pattern.compile("[\\s]");


      public static String makeSlug(String input) {
          String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
          String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
          String slug = NONLATIN.matcher(normalized).replaceAll("");
          return slug.toLowerCase(Locale.ENGLISH);
      }


      public static String GenerateMD5 (String pass) {

        try {
           return DigestUtils.md5Hex(pass).toString();
        } catch(Exception nsae){
            nsae.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }






      public  String replaceGroup(String regex, String source, int groupToReplace, String replacement) {
          return replaceGroup(regex, source, groupToReplace, 1, replacement);
      }

      public  String replaceGroup(String regex, String source, int groupToReplace, int groupOccurrence, String replacement) {
          Matcher m = Pattern.compile(regex).matcher(source);
          for (int i = 0; i < groupOccurrence; i++)
              if (!m.find()) return source; // pattern not met, may also throw an exception here
          return new StringBuilder(source).replace(m.start(groupToReplace), m.end(groupToReplace), replacement).toString();
      }

    public static String getURL(HttpServletRequest curRequest) {


        if(curRequest ==null) {
            return "http://localhost:8080/";
        }

        String scheme = curRequest.getScheme();       // http
        String serverName = curRequest .getServerName();     // hostname.com
        int serverPort = curRequest .getServerPort();        // 80
        String contextPath = curRequest .getContextPath();   // /mywebapp
        String servletPath = curRequest.getServletPath();   // /servlet/MyServlet
        String pathInfo = curRequest .getPathInfo();         // /a/b;c=123


        StringBuffer sb = new StringBuffer() ;

//        if(serverName !=null && serverName.equalsIgnoreCase("spingun.com")){
//            scheme="https";
//        }
        sb.append(scheme) ;
        sb.append("://") ;
        sb.append(serverName) ;

        if(serverPort!=80 && serverPort!=443) {
            sb.append(":") ;
            sb.append(serverPort) ;
        }


        sb.append(contextPath) ;


        return sb.toString();

    }

      public static String getHostAndPort(HttpServletRequest curRequest) {


          if(curRequest ==null) {
              return "localhost:8080";
          }


          String serverName = curRequest .getServerName();     // hostname.com
          int serverPort = curRequest .getServerPort();        // 80



          StringBuffer sb = new StringBuffer() ;

//        if(serverName !=null && serverName.equalsIgnoreCase("spingun.com")){
//            scheme="https";
//        }

          sb.append(serverName) ;

          if(serverPort!=80 && serverPort!=443) {
              sb.append(":") ;
              sb.append(serverPort) ;
          }



          return sb.toString();

      }


//    public static IExceptionTypes codeFromClientException(HttpClientErrorException e){
//        byte[] charz = e.getResponseBodyAsByteArray();
//
//        if(charz.length==38){
//            byte[] exists =getUserExist();
//            boolean userExists = true;
//            List a = new ArrayList();
//            for (byte i = 0; i <charz.length; i++) {
//                 userExists &= exists[i] ==charz[i];
//                System.out.println("userExists = [" + userExists+ "]");
//            }
//            System.out.println(" finaly userExists = [" + userExists+ "]");
//        String s = new String(a.toArray(),"UTF-8");
//
//            if(userExists){
//                return OXTreepexExcTypes.OX_TREEPEX_USER_EXISTS;
//            }
//        }
//            return null;
//
//    }


}
