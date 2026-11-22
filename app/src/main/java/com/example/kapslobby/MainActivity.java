/*
 * Copyright (c) 2026 Mohammed Husain Kapadia (MHK). All Rights Reserved.
 * Project: VicePhone - GTA 6 Duo Companion (Nexconn Build Challenge 2026)
 * Unauthorized copying or redistribution of this code is strictly prohibited.
 */
package com.example.kapslobby;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import ai.nexconn.chat.NCEngine;
import ai.nexconn.chat.params.ConnectParams;
import ai.nexconn.chat.params.InitParams;

public class MainActivity extends AppCompatActivity {

    private EditText etPassword;
    private Button btnLoginJason, btnLoginLucia;
    private TextView tvStatus;

    private static final String PLAYER1_PASS = "vice1";
    private static final String PLAYER2_PASS = "vice2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPassword = findViewById(R.id.etPassword);
        btnLoginJason = findViewById(R.id.btnLoginJason);
        btnLoginLucia = findViewById(R.id.btnLoginLucia);
        tvStatus = findViewById(R.id.tvStatus);

        btnLoginJason.setOnClickListener(v -> {
            String enteredPass = etPassword.getText().toString().trim();
            if (!enteredPass.equals(PLAYER1_PASS)) {
                tvStatus.setText("ACCESS DENIED: Invalid Passcode!");
                Toast.makeText(this, "Wrong Password for Player 1!", Toast.LENGTH_SHORT).show();
                return;
            }
            connectCharacter(
                    "Memon07",
                    "Player 1",
                    "Player_2",
                    "OcfI8lyBg5b77my6imRno8A2syQzEgqrYk7CGAcur/Q=@3qez.sg.rongnav.com;3qez.sg.rongcfg.com"
            );
        });

        btnLoginLucia.setOnClickListener(v -> {
            String enteredPass = etPassword.getText().toString().trim();
            if (!enteredPass.equals(PLAYER2_PASS)) {
                tvStatus.setText("ACCESS DENIED: Invalid Passcode!");
                Toast.makeText(this, "Wrong Password for Player 2!", Toast.LENGTH_SHORT).show();
                return;
            }
            connectCharacter(
                    "Player_2",
                    "Player 2",
                    "Memon07",
                    "MW2ZqEDfn9JEf3GbuLd+A51AzMvMYupV4T5dRyICE2s=@3qez.sg.rongnav.com;3qez.sg.rongcfg.com"
            );
        });
    }

    private void connectCharacter(String gamerId, String charName, String targetId, String token) {
        try {
            tvStatus.setText("Connecting " + charName + " to Leonida Network...");
            Toast.makeText(this, "Connecting " + charName + "...", Toast.LENGTH_SHORT).show();

            try {
                InitParams initParams = new InitParams(getApplicationContext(), "6tnym1br6ix17");
                NCEngine.initialize(initParams);
            } catch (Throwable ignored) {}

            ConnectParams params = new ConnectParams(token);
            params.setTimeout(10);

            NCEngine.connect(params, (userId, error) -> runOnUiThread(() -> {
                try {
                    if (error == null) {
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra("GAMER_ID", gamerId);
                        intent.putExtra("CHAR_NAME", charName);
                        intent.putExtra("TARGET_ID", targetId);
                        startActivity(intent);
                        finish();
                    } else {
                        tvStatus.setText("Connection Failed: " + error.getMessage());
                        Toast.makeText(MainActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Throwable ex) {
                    Toast.makeText(MainActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }));

        } catch (Throwable t) {
            Toast.makeText(this, "Critical Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}