/*
 * Copyright (c) 2026 Mohammed Husain Kapadia (MHK). All Rights Reserved.
 * Project: VicePhone - GTA 6 Duo Companion (Nexconn Build Challenge 2026)
 */
package com.example.kapslobby;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import ai.nexconn.chat.NCEngine;
import ai.nexconn.chat.channel.DirectChannel;
import ai.nexconn.chat.error.NCError;
import ai.nexconn.chat.handler.MessageHandler;
import ai.nexconn.chat.handler.SendMessageHandler;
import ai.nexconn.chat.message.Message;
import ai.nexconn.chat.message.TextMessage;
import ai.nexconn.chat.message.model.MessageDeletedEvent;
import ai.nexconn.chat.message.model.MessageReceivedEvent;
import ai.nexconn.chat.message.model.MessagesUpdatedEvent;
import ai.nexconn.chat.params.SendMessageParams;

public class ChatActivity extends AppCompatActivity {

    private EditText etMessage;
    private Button btnSend, btnAlertCops, btnAlertCar, btnAlertHeist, btnAlertSafe;
    private LinearLayout chatContainer;
    private ScrollView scrollView;
    private TextView tvSubHeader;
    private DirectChannel squadChannel;
    private String currentUserId, charName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_chat);

            currentUserId = getIntent().getStringExtra("GAMER_ID");
            charName = getIntent().getStringExtra("CHAR_NAME");
            String targetId = getIntent().getStringExtra("TARGET_ID");

            if (currentUserId == null) currentUserId = "Memon07";
            if (charName == null) charName = "Player 1";
            if (targetId == null || targetId.isEmpty()) {
                targetId = currentUserId.equals("Memon07") ? "Player_2" : "Memon07";
            }

            etMessage = findViewById(R.id.etMessage);
            btnSend = findViewById(R.id.btnSend);
            chatContainer = findViewById(R.id.chatContainer);
            scrollView = findViewById(R.id.scrollView);
            tvSubHeader = findViewById(R.id.tvSubHeader);

            btnAlertCops = findViewById(R.id.btnAlertCops);
            btnAlertCar = findViewById(R.id.btnAlertCar);
            btnAlertHeist = findViewById(R.id.btnAlertHeist);
            btnAlertSafe = findViewById(R.id.btnAlertSafe);

            tvSubHeader.setText("LOGGED IN AS: " + charName + " • LIVE SYNC");

            squadChannel = new DirectChannel(targetId);

            try {
                NCEngine.removeMessageHandler("VicePhoneListener");
            } catch (Throwable ignored) {}

            createNotificationChannel();
            requestNotificationPermission();

            setupMessageListener();

            btnSend.setOnClickListener(v -> {
                String msg = etMessage.getText().toString().trim();
                if (!msg.isEmpty()) sendMessage(msg);
            });

            // 1-Tap GTA 6 Tactical Alerts
            btnAlertCops.setOnClickListener(v -> sendMessage("[ALERT] 🚓 COPS ON ME! ★★★ WANTED LEVEL!"));
            btnAlertCar.setOnClickListener(v -> sendMessage("[ALERT] 🚗 NEED GETAWAY CAR ASAP!"));
            btnAlertHeist.setOnClickListener(v -> sendMessage("[ALERT] 💰 HEIST GEAR READY. MOVE IN!"));
            btnAlertSafe.setOnClickListener(v -> sendMessage("[ALERT] 📍 MEET AT VICE BEACH SAFEHOUSE!"));

        } catch (Throwable t) {
            Toast.makeText(this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendMessage(String messageText) {
        try {
            TextMessage textMessage = new TextMessage(messageText);
            SendMessageParams params = new SendMessageParams(textMessage);

            squadChannel.sendMessage(params, new SendMessageHandler() {
                @Override
                public void onAttached(Message message) {}

                @Override
                public void onResult(Message message, NCError error) {
                    runOnUiThread(() -> {
                        if (error == null) {
                            addMessageToScreen(charName + ": " + messageText, true, messageText.startsWith("[ALERT]"));
                            etMessage.setText("");
                        } else {
                            Toast.makeText(ChatActivity.this, "Send Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Throwable t) {
            Toast.makeText(this, "Send Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMessageListener() {
        try {
            NCEngine.addMessageHandler("VicePhoneListener", new MessageHandler() {
                @Override
                public void onMessageReceived(MessageReceivedEvent event) {
                    try {
                        Message message = event.getMessage();
                        if (message != null && message.getContent() instanceof TextMessage) {
                            TextMessage txtMsg = (TextMessage) message.getContent();
                            String senderId = message.getSenderUserId();
                            String text = txtMsg.getText();

                            if (senderId == null || !senderId.equals(currentUserId)) {
                                String senderName = "Memon07".equals(senderId) ? "Player 1" : "Player 2";
                                runOnUiThread(() -> {
                                    addMessageToScreen(senderName + ": " + text, false, text.startsWith("[ALERT]"));
                                    showPushNotification(senderName, text.replace("[ALERT] ", ""));
                                });
                            }
                        }
                    } catch (Throwable ignored) {}
                }
                @Override
                public void onMessageDeleted(MessageDeletedEvent event) {}
                @Override
                public void onMessagesUpdated(MessagesUpdatedEvent event) {}
            });
        } catch (Throwable ignored) {}
    }

    private void addMessageToScreen(String text, boolean isSender, boolean isAlert) {
        try {
            TextView tv = new TextView(this);
            tv.setText(text.replace("[ALERT] ", ""));
            tv.setPadding(36, 22, 36, 22);
            tv.setTextSize(15f);

            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(28f);

            if (isAlert) {
                bg.setColor(Color.parseColor("#FFB800")); // Gold Tactical Alert Card
                tv.setTextColor(Color.parseColor("#0D0221"));
                tv.setTypeface(null, Typeface.BOLD);
            } else if (isSender) {
                bg.setColor(Color.parseColor("#FF2A85")); // Vice City Hot Pink
                tv.setTextColor(Color.WHITE);
            } else {
                bg.setColor(Color.parseColor("#00F0FF")); // Cyber Cyan
                tv.setTextColor(Color.parseColor("#0D0221"));
            }

            tv.setBackground(bg);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10);
            params.gravity = isAlert ? Gravity.CENTER : (isSender ? Gravity.END : Gravity.START);
            tv.setLayoutParams(params);

            chatContainer.addView(tv);
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        } catch (Throwable ignored) {}
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "vicephone_alerts",
                    "VicePhone Tactical Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Real-time messages and heist alerts");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void showPushNotification(String sender, String message) {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "vicephone_alerts")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("VicePhone • " + sender)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify((int) System.currentTimeMillis(), builder.build());
            }
        } catch (Throwable ignored) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            NCEngine.removeMessageHandler("VicePhoneListener");
        } catch (Throwable ignored) {}
    }
}