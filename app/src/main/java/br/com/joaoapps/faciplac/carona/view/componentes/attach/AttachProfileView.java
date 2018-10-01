package br.com.joaoapps.faciplac.carona.view.componentes.attach;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.joaov.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;

import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.componentes.sheetCardSelector.SelectorDialogBottom;


public class AttachProfileView extends LinearLayout {
    public static int SEARCH_IMAGE_REQUEST = 1;
    public static int PICK_IMAGE_CAMERA_REQUEST = 2;

    AppCompatActivity activity;
    private Button btnBuscarFoto, btnTirarFoto;
    private CircleImageView imgProfile;
    private View view;
    private Bitmap bitmap;


    public AttachProfileView(Context context) {
        super(context);
        init();
    }

    public AttachProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttachProfileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void setViews() {
        btnBuscarFoto = view.findViewById(R.id.btn_buscar_foto);
        btnTirarFoto = view.findViewById(R.id.btn_tirar_foto);
        imgProfile = view.findViewById(R.id.img_icon);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.attach_document_view, this, true);
        setViews();
    }

    public void config(final AppCompatActivity activity) {
        btnBuscarFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), SEARCH_IMAGE_REQUEST);
            }
        });

        btnTirarFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(takePicture, PICK_IMAGE_CAMERA_REQUEST);
            }
        });
    }


    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        imgProfile.setImageBitmap(bitmap);
    }

    public boolean containsBitmap() {
        return bitmap != null;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
