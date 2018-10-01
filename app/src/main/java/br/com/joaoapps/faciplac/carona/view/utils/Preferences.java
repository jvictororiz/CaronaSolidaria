package br.com.joaoapps.faciplac.carona.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by joaov on 05/11/2017.
 */

public class Preferences {

    private static final String TOCKEN_ID = "TOCKEN_ID";
    private static final String LAST_LOGIN = "LAST_LOGIN";
    private static final String LAST_CPF_LOGGED = "LAST_CPF_LOGGED";

    public Preferences() {
    }

    public static void saveTockenId(Context context, String tocken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TOCKEN_ID, Context.MODE_PRIVATE).edit();
        editor.putString("TOCKEN", tocken);
        editor.apply();
    }

    public static String getTockenId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TOCKEN_ID, Context.MODE_PRIVATE);
        String restoredText = prefs.getString("TOCKEN", null);
        return restoredText;
    }

    public static void saveLastLogin(Context context, String lastLogin) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LAST_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString(LAST_LOGIN, lastLogin);
        editor.apply();
    }

    public static String getLastLogin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LAST_LOGIN, Context.MODE_PRIVATE);
        return prefs.getString(LAST_LOGIN, null);
    }

    public static void saveLastCpfLocgged(Context context, String cpf) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LAST_CPF_LOGGED, Context.MODE_PRIVATE).edit();
        editor.putString(LAST_CPF_LOGGED, cpf);
        editor.apply();
    }

    public static String getLastCpfLogged(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LAST_CPF_LOGGED, Context.MODE_PRIVATE);
        return prefs.getString(LAST_CPF_LOGGED, null);
    }
}
