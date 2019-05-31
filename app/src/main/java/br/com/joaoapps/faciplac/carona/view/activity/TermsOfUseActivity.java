package br.com.joaoapps.faciplac.carona.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.joaov.faciplac.caronasolidaria.R;

public class TermsOfUseActivity extends SuperActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_use);
        setupToolbar("Termos e condição de uso");

        WebView webView = findViewById(R.id.web_view);

    }
}
