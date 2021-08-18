package com.ftunicamp.tcc.utils;

import java.util.Calendar;

public class DateUtils {

    private DateUtils() {
    }

    public static int getAnoAtual() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static String nomeDoMes(int i) {
        String[] mes = {"janeiro", "fevereiro", "março", "abril",
                "maio", "junho", "julho", "agosto", "setembro", "outubro",
                "novembro", "dezembro"};
        return (mes[i - 1]);
    }
}
