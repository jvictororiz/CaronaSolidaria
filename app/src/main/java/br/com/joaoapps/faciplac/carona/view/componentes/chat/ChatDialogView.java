package br.com.joaoapps.faciplac.carona.view.componentes.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.ImageLoaderPicasso;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.Message;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.UsuarioMessage;

public class ChatDialogView extends OwlBottomSheetJv implements DateFormatter.Formatter {
    private View imgClose;
    private MessagesList messagesList;
    private MessageInput messageInput;

    private MessagesListAdapter<Message> adapter;
    private List<Message> listMessages;
    private UsuarioMessage otherUser;
    private TextView tvNameChat;
    private boolean prepared;

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


    public void prepareChat(AppCompatActivity activity, OnExpandBottomSheetListener onExpandBottomSheetListener, final CaronaUsuario myUser, final CaronaUsuario otherUser, OnSendMessage onSendMessage) {
        prepared = true;
        setActivityView(activity);
        setOnExpandBottomSheetListener(onExpandBottomSheetListener);
        this.otherUser = new UsuarioMessage(otherUser);
        messageInput.setInputListener(input -> {
            sendMessage(input.toString(), myUser);
            if (validateMessage(input.toString())) {
                onSendMessage.onSend(input.toString());
            }
            return true;
        });


        initAdapter(myUser);
        imgClose.setOnClickListener(view -> collapse());
    }

    private boolean validateMessage(String message) {
        return !message.isEmpty();
    }


    private void initAdapter(CaronaUsuario myUser) {
        listMessages = new ArrayList<>();
        ImageLoaderPicasso imageLoaderPicasso = new ImageLoaderPicasso(getContext());
        adapter = new MessagesListAdapter<>(myUser.getPushId(), imageLoaderPicasso);
        adapter.setDateHeadersFormatter(this);
        adapter.setOnMessageClickListener(null);
        adapter.setOnMessageViewClickListener(null);
        messagesList.setAdapter(adapter);

    }

    public boolean isPrepared() {
        return prepared;
    }

    public void sendMessage(String sendMessage, final CaronaUsuario user) {
        if (tvNameChat.getText().toString().isEmpty()) {
            tvNameChat.setText(user.getNome());
        }
        if (validateMessage(sendMessage)) {
            Message message = bindMessage(sendMessage, new UsuarioMessage(user));
            listMessages.add(message);
            message.setUrlImage(null);
            adapter.addToStart(message, true);

        }
    }

    private Message bindMessage(String message, UsuarioMessage usuarioMessage) {
        return new Message(message, usuarioMessage.getId(), usuarioMessage, new Date(), this.otherUser.getAvatar());
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
