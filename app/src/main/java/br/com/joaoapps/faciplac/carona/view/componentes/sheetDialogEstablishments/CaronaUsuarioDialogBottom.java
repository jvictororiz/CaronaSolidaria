package br.com.joaoapps.faciplac.carona.view.componentes.sheetDialogEstablishments;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.joaov.caronasolidaria.R;
import java.util.List;
import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.view.componentes.SuperSheetPopup;
import br.com.joaoapps.faciplac.carona.view.componentes.dialogSelectorItemMap.DialogSelectorItemMapView;

public class CaronaUsuarioDialogBottom extends SuperSheetPopup implements OnClickPosition {
    private List<CaronaUsuario> caronaUsuarios;
    private CaronaUsuario myCaronaUsuario;
    private EstablishmentsAdapter establishmentsAdapter;
    private RecyclerView optionsList;
    private DialogSelectorItemMapView.OnSelectCaronaUsuario onSelectCaronaUsuario;
    private View sheetView;
    private ImageView imgClose;

    public void init(CaronaUsuario caronaUsuario, DialogSelectorItemMapView.OnSelectCaronaUsuario onSelectedListener) {
        this.onSelectCaronaUsuario = onSelectedListener;
        this.myCaronaUsuario = caronaUsuario;
    }

    public CaronaUsuarioDialogBottom(Context context) {
        super(context);
    }

    @Override
    public View createView() {
        sheetView = LayoutInflater.from(context).inflate(R.layout.popup_dialog_establishments, null);
        optionsList = sheetView.findViewById(R.id.option_list);
        imgClose = sheetView.findViewById(R.id.img_close);

        refresh();
        setEvents();
        return sheetView;
    }

    private void setEvents() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void refresh() {
        establishmentsAdapter = new EstablishmentsAdapter(myCaronaUsuario, caronaUsuarios, context, this);
        optionsList.setLayoutManager(new LinearLayoutManager(context));
        optionsList.setAdapter(establishmentsAdapter);

    }

    public void show(List<CaronaUsuario> caronaUsuarios) {
        this.caronaUsuarios = caronaUsuarios;
        resetView(createView());
        show();
    }

    @Override
    public void click(int position) {
        if (onSelectCaronaUsuario != null) {
            onSelectCaronaUsuario.select(caronaUsuarios.get(position));
        }
        dismiss();
    }
}
