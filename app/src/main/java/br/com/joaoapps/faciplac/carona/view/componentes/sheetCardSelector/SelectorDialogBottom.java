package br.com.joaoapps.faciplac.carona.view.componentes.sheetCardSelector;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.componentes.SuperSheetPopup;

public class SelectorDialogBottom extends SuperSheetPopup {
    private ImageView imageRotate;
    private StatusCarona statusCarona;
    private View sheetView;
    private ImageView imgOferecerCarona;
    private ImageView imgPedirCarona;
    private TextView tvOferecerCarona;
    private TextView tvPedirCarona;

    private OnStatusCarona onStatusCarona;


    public SelectorDialogBottom(Context context, StatusCarona statusCarona, OnStatusCarona onStatusCarona) {
        super(context);
        this.onStatusCarona = onStatusCarona;
        this.statusCarona = statusCarona;
    }

    public SelectorDialogBottom(Context context, ImageView imageView, OnStatusCarona onStatusCarona) {
        super(context);
        this.onStatusCarona = onStatusCarona;
        this.imageRotate = imageView;
    }

    @Override
    public View createView() {
        sheetView = LayoutInflater.from(context).inflate(R.layout.popup_dialog_select_cards, null);
        setViews();
        setSelectedOption();

        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (imageRotate != null) {
                    imageRotate.animate().rotation(-0);
                }
            }
        });

        return sheetView;
    }

    public void show(StatusCarona statusCarona) {
        super.show();
        refreshStatusCarona(statusCarona);
    }

    private void setViews() {
        imgOferecerCarona = sheetView.findViewById(R.id.img_oferecer_carona);
        imgPedirCarona = sheetView.findViewById(R.id.img_pedir_carona);
        tvOferecerCarona = sheetView.findViewById(R.id.tv_oferecer_carona);
        tvPedirCarona = sheetView.findViewById(R.id.tv_pedir_carona);

    }

    public void setSelectedOption() {
        imgPedirCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshStatusCarona(StatusCarona.RECEBER_CARONA);
                dispathEventChangeStatus(StatusCarona.RECEBER_CARONA);
                dismiss();
            }
        });

        imgOferecerCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshStatusCarona(StatusCarona.DAR_CARONA);
                dispathEventChangeStatus(StatusCarona.DAR_CARONA);
                dismiss();
            }
        });
    }

    private void dispathEventChangeStatus(StatusCarona statusCarona){
        onStatusCarona.selected(statusCarona);
    }

    private void refreshStatusCarona(StatusCarona statusCarona) {
        if (statusCarona.equals(StatusCarona.DAR_CARONA)) {
            imgPedirCarona.setImageResource(R.drawable.icon_pedindo_carona_branco);
            imgPedirCarona.animate().alpha(0.3f);
            imgOferecerCarona.setImageResource(R.drawable.icon_carona);
            imgOferecerCarona.animate().alpha(1f);
            tvOferecerCarona.setTextColor(context.getResources().getColor(R.color.selectedIcon));
            tvPedirCarona.setTextColor(context.getResources().getColor(R.color.blue));
//                imgOferecerCarona.setBackgroundResource(R.drawable.ic_refeicao_desativado);
        } else {
            imgPedirCarona.setImageResource(R.drawable.icon_pedindo_carona);
            imgPedirCarona.animate().alpha(1f);
            imgOferecerCarona.setImageResource(R.drawable.icon_carona_branco);
            imgOferecerCarona.animate().alpha(0.3f);
            tvPedirCarona.setTextColor(context.getResources().getColor(R.color.selectedIcon));
            tvOferecerCarona.setTextColor(context.getResources().getColor(R.color.blue));
        }

    }

    public interface OnStatusCarona {
        void selected(StatusCarona statusCarona);
    }

}