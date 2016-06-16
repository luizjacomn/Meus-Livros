package br.edu.unicatolica.meuslivros.util;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by luizjaco on 16/06/16.
 */
public class AppUtils {

    private long dataEmprestimoMillis;

    public static String formatarData(int dia, int mes, int ano) {
        return (dia < 10 ? "0" + dia : dia) + "/" +
                (mes + 1 < 10 ? "0" + (mes + 1) : mes + 1) + "/" +
                ano;
    }

    public static void mensagemToast(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
    }
}

