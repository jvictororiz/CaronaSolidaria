package br.com.joaoapps.faciplac.carona.view.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.joaov.faciplac.caronasolidaria.R;

/**
 * Created by joaov on 22/11/2017.
 */

public class DialogUtils {
    private static android.support.v7.app.AlertDialog alertDialog;


    public static void gpsAlert(Context context, String message, DialogInterface.OnClickListener clickPositive, DialogInterface.OnClickListener clickNegative){

        if(alertDialog!= null  && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("Sim",clickPositive)
                .setNegativeButton("Não",clickNegative);
        builder.setCancelable(false);
        alertDialog = builder.create();
        if(!((Activity) context).isFinishing()){
            alertDialog.show();
            Button bq = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            bq.setTextColor(Color.BLACK);
            Button bn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(Color.BLACK);
        }


    }

    public  static void gpsAlertDismiss(){
        if(alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public static  void showDialogConfirm(Context context, String text, DialogInterface.OnClickListener okClick){
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("Alerta");
        builder.setMessage(text);
        builder.setPositiveButton("OK", okClick );
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
