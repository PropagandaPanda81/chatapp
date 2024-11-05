# ChatApp - Test Application for Customer-Operator Chat

ChatApp is a test application that simulates a basic real-time chat between a customer and an operator. This application was developed to demonstrate WebSocket communication and message delivery via RabbitMQ. Since this is a test setup, both the customer and operator views are placed on the same screen, allowing direct observation of the interaction between the two roles.

### Key Components:
- **WebSocket** for real-time communication
- **RabbitMQ** for message queuing when the recipient is offline
- **Docker Compose** to manage and deploy the necessary services (Spring Boot and RabbitMQ)

The application is designed to demonstrate message delivery in a test environment, accommodating both online and offline participants. This serves as a foundation for building a robust real-time communication system.


## Important Notes

> **Disclaimer:** This project is a simplified implementation created as a practice exercise. Certain limitations and shortcuts were made due to time constraints.

- **Session Management**: Proper session management has been simulated rather than implemented in full detail. The application assumes that the same customer and operator are consistently "online" after their initial connection. When the "Online" status is toggled off, the session ID is retained, allowing messages queued during offline periods to be delivered when the user toggles back to "Online."

- **Logging**: Comprehensive logging has been omitted to save time. For a production environment, proper logging would be essential for tracking activity, debugging, and maintaining application stability.

This simplified design allows the application to demonstrate core WebSocket communication and message queuing concepts without extensive session or logging management.



## Class Structure

The backend code for this chat application is organized into several packages, each containing classes that handle different parts of the application functionality.

### `com.hoesl.example.chatapp.api`

- **`ChatConstants`**: Contains constants like `CUSTOMER` and `OPERATOR`, used to distinguish between the different types of users.
- **`ChatMessageService`**: Defines methods for managing chat messages, including immediate message delivery if the recipient is online or queueing messages when the recipient is offline.
- **`WebSocketSessionManager`**: Manages active WebSocket sessions. This enables message delivery to online users by tracking who is currently connected.

### `com.hoesl.example.chatapp.config`

- **`RabbitMQConfig`**: Configures RabbitMQ for message queueing. It sets up connection properties and manages the chat message queue used for offline message delivery.
- **`WebSocketConfig`**: Sets up WebSocket endpoints and configures WebSocket handlers for customer and operator connections.

### `com.hoesl.example.chatapp.entities`

- **`ChatMessage`**: Represents a chat message with fields for sender, recipient, content, timestamp, and status.
- **`ChatMessageFactory`**: Creates instances of `ChatMessage`. This factory class ensures that message creation logic is encapsulated and consistent across the application.

### `com.hoesl.example.chatapp.enums`

- **`MessageStatus`**: Enum defining the status of a message, such as `PENDING` (waiting to be delivered) or `SENT` (successfully delivered).

### `com.hoesl.example.chatapp.impl`

- **`ChatMessageListener`**: Listens for messages in RabbitMQ and delivers them when the recipient becomes online.
- **`ChatMessageServiceImpl`**: Implements `ChatMessageService`. Manages the delivery of messages, sending them immediately to connected users or queuing them in RabbitMQ for offline users.

### `com.hoesl.example.chatapp.repository`

- **`ChatMessageRepository`**: Repository interface for performing CRUD operations on `ChatMessage` entities. It supports querying messages based on session IDs for efficient message retrieval.

### `com.hoesl.example.chatapp.socket.handler`

- **`DefaultWebSocketHandler`**: Base WebSocket handler with shared logic for managing connections and handling messages.
- **`CustomerWebSocketHandler`**: Extends `DefaultWebSocketHandler` to handle WebSocket connections specifically for customers.
- **`OperatorWebSocketHandler`**: Extends `DefaultWebSocketHandler` to handle WebSocket connections specifically for operators.

### Resources

- **`application.properties`**: Configures application settings, including RabbitMQ and WebSocket settings.
- **Static Files**: Contains frontend files like `index.html`, `app.js`, and CSS files, which together provide the user interface for the chat application.

This organization helps keep the application modular, with clear separation between backend logic, WebSocket handling, and frontend resources. Each part is responsible for a specific functionality, which makes it easy to understand, maintain, and expand upon.

