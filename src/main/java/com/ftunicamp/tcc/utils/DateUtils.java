package com.ftunicamp.tcc.utils;

import java.util.Calendar;

public class DateUtils {

    private DateUtils() {
    }

    public static int getSemestreAtual(int mesAtual) {
        if (mesAtual >= 1 && mesAtual <= 6) {
            return 1;
        }
        return 2;
    }

    public static int getAnoAtual() {
        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        return ano;
    }

    public static int getMesAtual() {
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH) + 6;
        return mes;
    }
}
