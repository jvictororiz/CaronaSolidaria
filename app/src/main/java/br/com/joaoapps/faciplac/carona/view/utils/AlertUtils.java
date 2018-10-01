package br.com.joaoapps.faciplac.carona.view.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.joaov.caronasolidaria.R;


public class AlertUtils {

    public static void showInfo(String text, Activity context) {
        TSnackbar snackbar = TSnackbar.make(context.findViewById(android.R.id.content), text, TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showInfo(String text, Activity context, View.OnClickListener onClickListener) {
        TSnackbar snackbar = TSnackbar.make(context.findViewById(android.R.id.content), text, TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setAction("", onClickListener);
        snackbar.show();
    }

    public static void showAlert(String text, Activity context) {
        TSnackbar snackbar = TSnackbar.make(context.findViewById(android.R.id.content), text, TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.vermelho));
        snackbar.show();
    }

    public static void showAlert(String text, String textClick, Activity context, View.OnClickListener onClickListener) {
        TSnackbar snackbar = TSnackbar.make(context.findViewById(android.R.id.content), text, TSnackbar.LENGTH_LONG);
        snackbar.setAction(textClick, onClickListener);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.vermelho));
        snackbar.show();
    }
}
