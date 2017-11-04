package com.marbit.hobbytrophies.chat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.adapters.ChatAdapter;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityView;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.chat.presenters.ChatActivityPresenter;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatActivityView, TextWatcher, View.OnClickListener {

    public final static String PARAM_ITEM_ID = "ITEM-ID";
    public static final String PARAM_ITEM_TITLE = "ITEM-TITLE";
    public final static String PARAM_BUYER = "BUYER";
    public final static String PARAM_SELLER = "SELLER";

    public final static String PARAM_CHAT_ID = "CHAT-ID";

    private ChatActivityPresenter presenter;
    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;
    private Chat chat;
    private String itemId;
    private String stringTitleItem;
    private ImageView avatarSeller;
    private TextView titleItem;
    private ImageButton buttonSend;
    private ImageView buttonBack;
    private EditText editTextMessage;
    private String buyer;
    private String seller;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.presenter = new ChatActivityPresenter(getApplicationContext(), this);
        this.itemId = getIntent().getStringExtra(PARAM_ITEM_ID);
        this.stringTitleItem = getIntent().getStringExtra(PARAM_ITEM_TITLE);
        this.buyer = getIntent().getStringExtra(PARAM_BUYER);
        this.seller = getIntent().getStringExtra(PARAM_SELLER);
        this.presenter = new ChatActivityPresenter(getApplicationContext(), this);
        this.recyclerViewChats = (RecyclerView) findViewById(R.id.recycler_view_chats);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerViewChats.setLayoutManager(mLayoutManager);
        this.buttonSend = (ImageButton) findViewById(R.id.button_send_message);
        this.buttonSend.setEnabled(false);
        this.editTextMessage = (EditText) findViewById(R.id.edit_text_message);
        this.editTextMessage.addTextChangedListener(this);
        this.chatAdapter = new ChatAdapter(getApplicationContext());
        this.recyclerViewChats.setAdapter(chatAdapter);
        this.buttonBack = (ImageView) findViewById(R.id.button_back);
        this.buttonBack.setOnClickListener(this);
        this.avatarSeller = (ImageView) findViewById(R.id.ic_avatar_seller);
        this.titleItem = (TextView) findViewById(R.id.text_view_title_item);
        this.setTitleItem(stringTitleItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.count = 0;
        if(getIntent().getStringExtra(PARAM_CHAT_ID) != null){
            this.loadChat(getIntent().getStringExtra(PARAM_CHAT_ID));
        }else {
            this.loadChat();
        }
    }

    @Override
    public void onPause() {
        presenter.removeListenerAddMessageChat();
        super.onPause();
    }

    private void refreshChat(List<Object> genericListMessages) {
        this.chatAdapter.clearAll();
        this.chatAdapter.setList(genericListMessages);
        this.chatAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadChatSuccessful(Chat chat, List<Object> genericListMessages) {
        this.chat = chat;
        refreshChat(genericListMessages);
        this.presenter.addListenerAddMessageChat(chat.getId());
        recyclerViewChats.scrollToPosition(chatAdapter.getItemCount() - 1);
        this.presenter.loadPartnerUserChat(Preferences.getUserName(getApplicationContext()).equals(chat.getSeller()) ? chat.getBuyer() : chat.getSeller());
        this.presenter.loadItem(chat.getItem());
    }

    @Override
    public void loadChat() {
        if(seller == null){
            this.presenter.loadChat(itemId, stringTitleItem, buyer, null);
        }else {
            this.presenter.loadChat(itemId, stringTitleItem, buyer, seller);
        }
    }

    @Override
    public void sendMessageSuccessful(String messageId) {
        // Update estado del mensaje con el tilde de enviado (Pero no tengo el id!!)
//        chatAdapter.confirmMessageSent(messageId);
//        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void addMessageToChat(MessageChat messageChat) {
        count++;
        if(count > chat.getMessages().size()) {
            chatAdapter.addMessage(messageChat);
            recyclerViewChats.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void loadUserBasicProfileSuccessful(User user) {
        setAvatarSeller(user.getAvatarUrl());
    }

    @Override
    public void setTitleItem(String titleItem) {
        this.titleItem.setText(titleItem);
    }

    @Override
    public void setAvatarSeller(String urlAvatar) {
        Picasso.with(getApplicationContext()).load(urlAvatar).transform(new CircleTransform()).fit().into(this.avatarSeller);
    }

    @Override
    public void loadChat(String chatId) {
        presenter.loadChat(chatId);
    }

    public void clickSendMessage(View view) {
        presenter.sendMessage(chat.getItem(), chat.getTitleItem(), chat.getBuyer(), chat.getSeller(), Preferences.getUserName(getApplicationContext()), editTextMessage.getText().toString());
        editTextMessage.setText("");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() > 0){
            buttonSend.setEnabled(true);
            buttonSend.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }else {
            buttonSend.setEnabled(false);
            buttonSend.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_600));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                onBackPressed();
                break;
        }
    }
}
