package br.com.joaoapps.faciplac.carona.view.componentes.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.joaoapps.faciplac.carona.SuperApplication;
import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.LatLng;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.ImageLoaderPicasso;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.Message;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.UsuarioMessage;
import br.com.joaoapps.faciplac.carona.view.utils.AppUtil;
import br.com.joaoapps.faciplac.carona.view.utils.GpsUtils;
import br.vince.owlbottomsheet.OwlBottomSheet;

public class ChatDialogView extends OwlBottomSheetJv implements DateFormatter.Formatter {
    private final String TYPE_LOCATION = "TYPE_LOCATION";
    private View imgClose;
    private MessagesList messagesList;
    private MessageInput messageInput;

    private MessagesListAdapter<Message> adapter;
    private List<Message> listMessages;
    private UsuarioMessage usuarioMessage;
    private TextView tvNameChat;
    private AppCompatActivity context;
    private ImageLoaderPicasso imageLoaderPicasso;

    public ChatDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public ChatDialogView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ChatDialogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        attachContentView(R.layout.layout_bottom_sheet_chat);
        View view1 = getContentView();
        messagesList = view1.findViewById(R.id.messagesList);
        messageInput = view1.findViewById(R.id.input);
        imgClose = view1.findViewById(R.id.img_close);
        tvNameChat = view1.findViewById(R.id.tv_name_chat);
        setIcon(R.drawable.ic_chat);
    }


    public void prepareChat(AppCompatActivity activity, final CaronaUsuario usuario, OnSendMessage onSendMessage) {
        setActivityView(activity);
        this.context = activity;
        this.usuarioMessage = new UsuarioMessage(usuario);
        messageInput.setInputListener(input -> {
            sendMessage(input.toString(), usuario);
            onSendMessage.onSend(input.toString());
            return true;
        });

        messageInput.setAttachmentsListener(() -> {
            sendLocation(getLatLng(usuario), usuarioMessage);
            onSendMessage.onSend(TYPE_LOCATION);
        });

        initAdapter();
        imgClose.setOnClickListener(view -> collapse());
    }

    private void sendLocation(LatLng latLng, UsuarioMessage usuario) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout_location, null);
        bindLocationView(v, usuario);
        Bitmap bitmapFromView = AppUtil.createBitmapFromView(v);
        Message messageLocation = new Message(String.valueOf(latLng.getLatitude()) + " " + String.valueOf(latLng.getLongitude()), usuario.getId(), usuario, new Date(), usuario.getAvatar());
        listMessages.add(messageLocation);
        imageLoaderPicasso.setBitmap(bitmapFromView);
        adapter.addToStart(messageLocation, true);

    }

    private void bindLocationView(View v, UsuarioMessage usuarioMessage) {
        ((TextView) v.findViewById(R.id.tv_location)).setText(GpsUtils.getNameLocale(context, usuarioMessage.getUsuario().getPositionResidence().getLatitude(), usuarioMessage.getUsuario().getPositionResidence().getLongitude()));
        ((TextView) v.findViewById(R.id.tv_type_location)).setText("Localização da casa de " + usuarioMessage.getUsuario().getNome());
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(SuperApplication.getLocale(), "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", ChatDialogView.this.usuarioMessage.getUsuario().getPositionResidence().getLatitude(), ChatDialogView.this.usuarioMessage.getUsuario().getPositionResidence().getLatitude(), "Home Sweet Home", usuarioMessage.getUsuario().getPositionResidence().getLongitude(), usuarioMessage.getUsuario().getLatitude(), "Where the party is at");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        });
    }

    private void initAdapter() {
        listMessages = new ArrayList<>();
        imageLoaderPicasso = new ImageLoaderPicasso(getContext());
        adapter = new MessagesListAdapter<>(usuarioMessage.getId(), imageLoaderPicasso);
        adapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(adapter);

    }

    public void sendMessage(String sendMessage, final CaronaUsuario usuario) {
        switch (sendMessage) {
            case TYPE_LOCATION:
                sendLocation(getLatLng(usuario), usuarioMessage);
                break;
            default:
                if (tvNameChat.getText().toString().isEmpty()) {
                    tvNameChat.setText(usuario.getNome());
                }
                Message message = bindMessage(sendMessage, new UsuarioMessage(usuario));
                listMessages.add(message);
                message.setUrlImage(null);
                adapter.addToStart(message, true);
                break;
        }
    }

    private LatLng getLatLng(CaronaUsuario usuario) {
        return new LatLng(usuario.getLatitude(), usuario.getLongitude());
    }

    private Message bindMessage(String message, UsuarioMessage usuarioMessage) {
        return new Message(message, usuarioMessage.getId(), usuarioMessage, new Date(), this.usuarioMessage.getAvatar());
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return getContext().getString(R.string.date_header_today);
        } else if (DateFormatter.isYesterday(date)) {
            return getContext().getString(R.string.date_header_yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    public interface OnSendMessage {
        void onSend(String message);
    }
}
