# 🔐 Signal Protocol E2E Chat

A demonstration of **true end-to-end encryption** using the Signal Protocol, built with Spring Boot and vanilla JavaScript.

![Security](https://img.shields.io/badge/security-E2E%20encrypted-green)
![Protocol](https://img.shields.io/badge/protocol-Signal-blue)
![Backend](https://img.shields.io/badge/backend-Spring%20Boot-brightgreen)
![Frontend](https://img.shields.io/badge/frontend-Vanilla%20JS-yellow)

##  Features

- **True End-to-End Encryption** - Server cannot decrypt messages
- **Signal Protocol** - Industry-standard Double Ratchet Algorithm
- **Forward Secrecy** - Past messages remain secure even if keys are compromised
- **Post-Compromise Security** - Future messages secure after compromise
- **Real-time Messaging** - WebSocket (STOMP) for instant delivery
- **Client-Side Cryptography** - All keys generated in the browser

##  Architecture

```
┌─────────────────┐                    ┌──────────────────┐
│   Browser A     │                    │    Browser B     │
│  ┌───────────┐  │                    │  ┌────────────┐  │
│  │ libsignal │  │                    │  │ libsignal  │  │
│  │  Protocol │  │                    │  │  Protocol  │  │
│  └─────┬─────┘  │                    │  └─────┬──────┘  │
│        │        │                    │        │         │
│   [Encrypt]     │                    │   [Decrypt]      │
└────────┼────────┘                    └────────┼─────────┘
         │                                      │
         │        ┌──────────────────┐          │
         └───────>│  Spring Boot     │<─────────┘
                  │  (Message Relay) │
                  │                  │
                  │  ❌ Can't decrypt│
                  └────────┬─────────┘
                           │
                  ┌────────▼─────────┐
                  │      Redis       │
                  │  - User Data     │
                  │  - PreKey Bundles│
                  └──────────────────┘
```

##  Tech Stack

**Backend:**
- Spring Boot 
- Spring WebSocket (STOMP)
- Redis (Session & Key storage)
- Rate Limiting (Redis-based)

**Frontend:**
- Vanilla JavaScript
- libsignal-protocol.js
- SockJS + STOMP
- localStorage (key persistence)

**Cryptography:**
- **Signal Protocol** (Double Ratchet Algorithm)
- **Curve25519** (ECDH key agreement)
- **AES-256-CBC** (Symmetric encryption)
- **HMAC-SHA256** (Message authentication)

##  Getting Started

### Prerequisites

- Java 17+
- Redis server
- Modern web browser

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/cryptologicpsy/cryptochat.git
```

2. **Start Redis**
```bash
redis-server
```

3. **Run the application**
```bash
./mvnw spring-boot:run
```

4. **Open in browser**
```
http://localhost:8080
```

## 📖 How It Works

### 1. User Registration
```javascript
// Client-side key generation
- Generate Identity Key Pair (Curve25519)
- Generate Signed PreKey + Signature
- Generate One-Time PreKey

// Upload ONLY public keys to server
- Server stores PreKeyBundle in Redis
- Private keys NEVER leave the browser
```

### 2. Session Establishment (X3DH)
```javascript
// Alice wants to message Bob
1. Fetch Bob's PreKeyBundle from server
2. Verify signature on Signed PreKey
3. Perform Triple Diffie-Hellman (X3DH)
4. Derive shared secret
5. Initialize Double Ratchet
```

### 3. Message Exchange
```javascript
// Encryption (Alice)
1. Get current chain key from Double Ratchet
2. Derive message key
3. Encrypt with AES-256-CBC
4. Compute HMAC-SHA256
5. Send ciphertext via WebSocket

// Relay (Server)
- Forwards encrypted message
- CANNOT decrypt (doesn't have keys)

// Decryption (Bob)
1. Receive ciphertext
2. Verify HMAC
3. Decrypt with AES-256-CBC
4. Advance Double Ratchet
```
Each message derives a unique one-time message key from the current chain key
using a one-way key derivation function (KDF). After encryption, the chain key
is advanced and the message key is immediately discarded. This guarantees
forward secrecy at the message level, even within an established session.

## 🔒 Security Features

### ✅ What's Secure

- **E2E Encryption** - Server is untrusted, cannot read messages
- **Forward Secrecy** - Compromise today doesn't reveal past messages
- **Post-Compromise Security** - Self-healing after key compromise
- **Authentication** - Identity keys prevent impersonation
- **Deniability** - MACs instead of signatures
- **Client-Side Keys** - All cryptographic keys generated in browser


### Planned Improvements

- ** localStorage Encryption** - Encrypt keys at rest with password-derived key
- ** Message History** - Encrypted message storage
- ** Delivery Receipts** - Message delivery confirmation
- ** Multi-Device** - Support multiple devices per user
- ** Session Backup** - Encrypted session backup to server

```

## 🔬 API Endpoints

### User Management
```
POST   /api/users/register          # Register new user
GET    /api/users/{userId}          # Get user info
PUT    /api/users/{userId}/online   # Update online status
```

### Key Management
```
POST   /api/keys/{userId}/upload    # Upload PreKeyBundle
GET    /api/keys/{userId}/bundle    # Fetch PreKeyBundle
DELETE /api/keys/{userId}/bundle    # Delete PreKeyBundle
```

### Messaging (WebSocket)
```
CONNECT    /chat-websocket          # Establish WebSocket
SUBSCRIBE  /topic/user/{userId}     # Listen for messages
SEND       /app/chat.send            # Send encrypted message
```

## 🧪 Testing

Open two browser windows:

1. **Window 1 (Alice)**
   - Register as "alice"
   - Copy User ID

2. **Window 2 (Bob)**
   - Register as "bob"
   - Copy User ID

3. **Alice → Bob**
   - Paste Bob's User ID
   - Click "Connect to Recipient"
   - Type message → Send

4. **Verify**
   - Bob receives decrypted message
   - Check browser console for encryption logs
   - Check server logs - server sees only ciphertext


## 📚 Learn More

### Signal Protocol
- [Signal Protocol Specification](https://signal.org/docs/)
- [X3DH Key Agreement](https://signal.org/docs/specifications/x3dh/)
- [Double Ratchet Algorithm](https://signal.org/docs/specifications/doubleratchet/)

### Libraries
- [libsignal-protocol-javascript](https://github.com/signalapp/libsignal-protocol-javascript)
- [Spring WebSocket Documentation](https://docs.spring.io/spring-framework/reference/web/websocket.html)


## ⚠️ Disclaimer

This is a **demonstration project** for educational purposes. It showcases how the Signal Protocol works but is **NOT production-ready**. Do not use for sensitive communications without implementing proper:

- User authentication & authorization
- CORS security
- localStorage encryption
- Input validation & sanitization
- Rate limiting hardening
- Comprehensive error handling
- Security auditing

## 🤝 Contributing

Contributions are welcome! Areas needing improvement:

1. User authentication system
2. localStorage encryption implementation
3. Message history with encryption
4. Multi-device support
5. Group chat functionality
6. Comprehensive testing

## 👨‍💻 Author

**Cryptologic** - [GitHub Profile](https://github.com/cryptologicpsy)

---

⭐ **Star this repo** if you found it helpful for learning about E2E encryption!

🔒 **Remember:** True privacy means the server can't read your messages. This app demonstrates that principle.
