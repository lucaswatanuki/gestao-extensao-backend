package com.ftunicamp.tcc.utils;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;

public class Utilities {

    private Utilities() {}

    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString();
        return baseUrl.replace(request.getServletPath(), "");
    }

   public static DateTimeFormatter formatarData() {
       return DateTimeFormatter.ofPattern("dd/MM/yyyy");
   }
}
