# VicePhone — GTA 6 Duo Companion

**Team:** Kaps Labs  
**Developer:** Mohammed Husain Kapadia (MHK)  
**Track:** Gaming & Live Streams  
**SDK:** Nexconn Chat SDK (`ai.nexconn.chat:chat:26.4.0`)  

## Overview
**VicePhone** is a native Android (Java) real-time duo companion app inspired by the neon Vice City aesthetic of GTA 6. Built for co-op gamers who need instant, distraction-free communication without leaving their game, it combines encrypted-style 1-on-1 chat, passcode-protected character logins, 1-tap tactical heist alerts, and real-time push notifications.

## Key Features
* **Neon Splash Animation:** Smooth fade-in and zoom startup animation (`SplashActivity`).
* **Passcode-Protected Duo Login:** Dedicated credentials for Player 1 (`Memon07`) and Player 2 (`Player_2`) to prevent unauthorized access.
* **Real-Time Messaging:** Instant 1-on-1 chat with custom Vice City Hot Pink & Cyber Cyan message bubbles.
* **1-Tap Tactical Alerts:** Quick-action bar (*Cops on Me*, *Need Getaway*, *Heist Ready*, *Safehouse*) that renders messages as high-visibility Gold Alert Cards.
* **Push Notifications:** Real-time high-priority Android notifications on incoming messages and alerts.

## Nexconn SDK Integration
* **App Key:** `6tnym1br6ix17`
* **Initialization & Auth (`MainActivity.java`):** `NCEngine.initialize(InitParams)` and `NCEngine.connect(ConnectParams)` for token-based user authentication.
* **Messaging (`ChatActivity.java`):** `DirectChannel`, `TextMessage`, `SendMessageParams`, and `SendMessageHandler` for sending real-time texts and tactical alerts.
* **Live Listener (`ChatActivity.java`):** `NCEngine.addMessageHandler()` (`MessageHandler.onMessageReceived`) for streaming incoming messages and triggering notifications.

## Demo Credentials (For Judges)
* **Player 1 (`Memon07`):** Passcode `vice1`
* **Player 2 (`Player_2`):** Passcode `vice2`

## License
© 2026 Mohammed Husain Kapadia (MHK). All Rights Reserved. See `LICENSE` for details.
