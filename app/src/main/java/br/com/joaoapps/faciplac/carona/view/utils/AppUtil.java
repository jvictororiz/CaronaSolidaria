package br.com.joaoapps.faciplac.carona.view.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Todas as aplicacoes vao utilizar isso
 *
 * @author Jonas Baggio
 * @create 21/02/2013 09:56:22
 */
@SuppressWarnings("unused")
public class AppUtil {

    /**
     * TAG
     */
    public static String TAG = "AppUtil";

    /**
     * Controla a visualização dos logs referente a infra da aplicação
     */
    public static boolean LOG_INFRA_DESENVOLVIMENTO = true;

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.i("LOG  ----------- ", "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void vibrate(Context context, long time) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        assert v != null;
        v.vibrate(300);
    }

    public static String getB64fromBitmap(Bitmap fotoSelfie) {
        if (fotoSelfie == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        fotoSelfie.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String getB64fromBitmap(Bitmap.CompressFormat format, int quality, Bitmap fotoSelfie) {
        if (fotoSelfie == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        fotoSelfie.compress(format, quality, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static boolean isOpeningApp(Context context) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().get(0).importance == 100;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] byteArray = byteBuffer.array();
        return byteArray;
    }

    public static Bitmap getBitmapfromB64(String fotoSelfieB64) {
        if (fotoSelfieB64 == null)
            return null;
        byte[] byteArray = Base64.decode(fotoSelfieB64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


    public static String saveToExternalStorage(Bitmap finalBitmap, String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static File loadFileFromStorage(String path) {
        return new File(path);
    }

    public static Bitmap loadImageFromStorage(String path) {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createBitmapFromView(View view) {
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    public static Bitmap loadImageFromStorage(String path, String file) {
        try {
            File f = new File(path, file + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Null-safe equivalent of {@code a.equals(b)}.
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }


    public static Drawable setDrawableColor(Drawable drawable, @ColorInt int color, int alpha) {
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            drawable.setAlpha(alpha);
        }
        return drawable;
    }

    public static Drawable setDrawableColor(Context context, Drawable drawable, @ColorRes int colorRes) {
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                drawable.setColorFilter(context.getResources().getColor(colorRes, context.getTheme()), PorterDuff.Mode.SRC_ATOP);
            } else {
                drawable.setColorFilter(context.getResources().getColor(colorRes), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return drawable;
    }

    public static void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    // Ordena por KEY Map<key, value>
    public static Map<String, Double> sortByValue(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) Integer.parseInt(((Map.Entry) (o1)).getKey().toString())).compareTo(Integer.parseInt(((Map.Entry) (o2)).getKey().toString()));
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }


    public static String getTextString(View view) {

        if (view == null) {
            return null;
        }

        if (view instanceof TextView) {

            TextView txt = (TextView) view;
            String s = txt.getText().toString();
            return s;
        }

        if (view instanceof EditText) {

            EditText t = (EditText) view;
            String s = t.getText().toString();
            return s;
        }

        return null;
    }


    public static boolean isPackageInstalled(Context context, String packagename) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static TextView getBarcode128(Context context, TextView textView, String linhaDigital) {
        Typeface BarcodeFontFace = Typeface.createFromAsset(context.getAssets(), "fonts/code128.ttf");
        textView.setTypeface(BarcodeFontFace);
        textView.setText(linhaDigital);
        return textView;

    }

    public static String getNumeroMascaradoSemFormatacao(String numeroMascarado) {
        return numeroMascarado.replaceAll("[ ]", "");
    }

    public static String getNumGrupo1(String numeroMascarado) {
        return getNumeroMascaradoSemFormatacao(numeroMascarado).substring(0, 4);
    }

    public static String getNumGrupo2(String numeroMascarado) {
        return getNumeroMascaradoSemFormatacao(numeroMascarado).substring(4, 8);
    }

    public static String getNumGrupo3(String numeroMascarado) {
        return getNumeroMascaradoSemFormatacao(numeroMascarado).substring(8, 12);
    }

    public static String getNumGrupo4(String numeroMascarado) {
        return getNumeroMascaradoSemFormatacao(numeroMascarado).substring(12, 16);
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageManager pack = context.getPackageManager();
            String pacote = context.getPackageName();
            PackageInfo packageInfo = pack.getPackageInfo(pacote, 0);
            String versao = packageInfo.versionName;
            return versao;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        }
    }

    public static String getDeviceName() {
        String name = null;
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            name = mBluetoothAdapter.getName();
        } catch (Exception ignored) {
        }
        if (name == null) {
            name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL).toUpperCase();
        }
        return name;
    }

    public static int getResourceIdByAttr(Context context, final int attr) {
        int[] attrs = {attr};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int colorId = ta.getResourceId(0, 0);
        ta.recycle();
        return colorId;
    }

    public static String getStringByAttr(Context context, final int attr) {
        int stringId = getResourceIdByAttr(context, attr);
        return context.getResources().getString(stringId);
    }

    public static final void underlineTextView(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void callPhone(Activity context, String phone, int REQUEST_PHONE_CALL) {
        String uri = "tel:" + phone;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            context.startActivity(intent);
        }
    }

    public static void downloadFile(Context context, String url, String filePath) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath);
        dm.enqueue(request);

    }

    public static void adjustImageViewToImageSize(Context context, Bitmap image, int minWidthDp, ImageView imageView) {
        try {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            float imageWidthRatio = (float) imageWidth / (float) imageHeight;
            int minHeight = AppUtil.dpToPx(context, minWidthDp);

            if (imageHeight < minHeight) {
                imageHeight = minHeight;
            }
            imageView.getLayoutParams().height = (int) (imageView.getLayoutParams().height * imageWidthRatio);
//            imageView.getLayoutParams().width =(int)  (imageWidth * imageWidthRatio);
            imageView.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openLinkInBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void showPasword(EditText editText) {
        editText.setTransformationMethod(null);
    }

    public static void hidePasword(EditText editText) {
        editText.setTransformationMethod(new PasswordTransformationMethod());
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
