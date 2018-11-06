package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;

public class MessageActivity extends AppCompatActivity {
    public static final String INTENT = "INTENT";
    public static String TITLE = "TITLE";
    public static String SUBTITLE = "SUBTITLE";
    public static String ICON_SUCCESS = "SUCCESS";
    public  static String ICON_ERROR = "ERROR";
    public  static String CODE_ICON = "CODE_ICON";

    private TextView tvTitle;
    private TextView tvSubtitle;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tvTitle =  findViewById(R.id.tv_title);
        tvSubtitle =  findViewById(R.id.tv_subtitle);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String title = bundle.getString(TITLE);
            String subtitle = bundle.getString(SUBTITLE);
            String codeIcon = bundle.getString(CODE_ICON);
            intent = bundle.getParcelable(INTENT);

            if(codeIcon != null){
                tvTitle.setVisibility(View.GONE);

                if(codeIcon.equals(ICON_SUCCESS)){
                }else if(codeIcon.equals(ICON_ERROR)){
                    tvSubtitle.setTextColor(Color.RED);
                }
            }else{
                tvTitle.setText(title);
            }
            tvSubtitle.setText(subtitle);

        }

        initTime();
    }

    public void initTime (){
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                if(intent != null) {
                    startActivity(intent);
                }
                finish();
            }
        }, 1200 );
    }
}
