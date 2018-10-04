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

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.view.activity.SuperActivity;

public class MessageActivity extends SuperActivity {
    public static final String INTENT = "INTENT";
    public static String TITLE = "TITLE";
    public static String SUBTITLE = "SUBTITLE";
    public static String ICON_SUCCESS = "SUCCESS";
    public  static String ICON_ERROR = "ERROR";
    public  static String CODE_ICON = "CODE_ICON";

    private TextView tvTitle;
    private TextView tvSubtitle;
    private Intent intent;
    private ImageView imgIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        imgIcon = (ImageView) findViewById(R.id.img_icon);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String title = bundle.getString(TITLE);
            String subtitle = bundle.getString(SUBTITLE);
            String codeIcon = bundle.getString(CODE_ICON);
            intent = bundle.getParcelable(INTENT);

            if(codeIcon != null){
                tvTitle.setVisibility(View.GONE);
                imgIcon.setVisibility(View.VISIBLE);

                if(codeIcon.equals(ICON_SUCCESS)){
                    imgIcon.setImageResource(R.drawable.correct);
                }else if(codeIcon.equals(ICON_ERROR)){
                    imgIcon.setImageResource(R.drawable.negado);
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
