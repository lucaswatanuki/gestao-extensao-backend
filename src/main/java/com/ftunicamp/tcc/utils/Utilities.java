package com.ftunicamp.tcc.utils;

import javax.servlet.http.HttpServletRequest;

public class Utilities {

    private Utilities() {}

    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString();
        return baseUrl.replace(request.getServletPath(), "");
    }
}
