# CryptoChat Application

## Overview

The Secure Chat Application is a web-based chat application that ensures end-to-end encryption, meaning that the server does not have access to any messages exchanged between clients. This is achieved using asymmetric RSA encryption, where each client generates a pair of cryptographic keys: a public key for encryption and a private key for decryption. The backend server is implemented using Spring Boot with WebSocket, serving solely as a mediator for message transmission.

## Features

- **End-to-End Encryption**: Messages are encrypted on the client side before being sent to the server, ensuring that only the intended recipient can decrypt and read the messages.
- **Asymmetric RSA Algorithm**: Each client generates a pair of keys (public and private). Public keys are exchanged for encryption purposes, while private keys remain confidential.
- **Spring Boot Backend**: The backend server operates with Spring Boot and WebSocket, functioning only as a message broker.
- **User-Friendly Interface**: A simple and clean user interface that allows users to send and receive messages easily.

## How It Works

1. **Key Generation**: Each client generates their own pair of cryptographic keys (public and private) upon clicking the "Generate Keys" button.
2. **Public Key Exchange**: Clients can enter the recipient's public key to send encrypted messages.
3. **Message Encryption**: When a message is sent, it is encrypted using the recipient's public key.
4. **Message Transmission**: The encrypted message is sent to the Spring Boot backend server, which forwards it to the intended recipient without decrypting it.
5. **Message Decryption**: Upon receiving a message, the recipient uses their private key to decrypt the message and read its content.
6. **Message Logging**: The application logs all sent and received messages for the user's reference.

## Technologies Used

- **Frontend**: 
  - **HTML/CSS**: For the structure and styling of the application.
  - **JavaScript**: For client-side functionality and cryptographic operations.
  - **SockJS**: For WebSocket communication.
  - **STOMP**: For messaging protocol.
  - **JSEncrypt**: For RSA encryption and decryption.
  - **CryptoJS**: For additional cryptographic functionalities.

- **Backend**:
  - **Spring Boot**: For building the backend server.
  - **WebSocket**: For real-time communication between clients.
