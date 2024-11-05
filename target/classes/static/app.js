const apiUrl = "/api/chat";
let customerSessionId = null;  // Session-ID für Kunden
let operatorSessionId = null;  // Session-ID für Operator
let customerSocket = null;
let operatorSocket = null;

// Nachrichten über WebSocket anzeigen
function displayMessage(chatBoxId, sender, content) {
    const chatBox = document.getElementById(chatBoxId);
    const messageElement = document.createElement('div');
    const formattedTime = formatTime(new Date());

    messageElement.className = sender === "Operator" ? 'operator-message' : 'customer-message';
    messageElement.innerHTML = `<strong>${formattedTime} - ${sender}:</strong> ${content}`;
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}

// WebSocket-Verbindung für Kunden-Chat initialisieren
function initCustomerWebSocket() {
    if (!customerSocket) {
        customerSocket = new WebSocket(`ws://localhost:8080/chat/customer?sessionId=${customerSessionId}`);
        customerSocket.onopen = () => console.log("Kunden-WebSocket verbunden");
        customerSocket.onmessage = event => {
            const msg = JSON.parse(event.data);
            displayMessage('customer-chat-box', msg.sender, msg.content);
        };
        customerSocket.onclose = () => console.log("Kunden-WebSocket geschlossen");
        customerSocket.onerror = error => console.error("Kunden-WebSocket Fehler:", error);
    }
}

// WebSocket-Verbindung für Operator-Chat initialisieren
function initOperatorWebSocket() {
    if (!operatorSocket) {
        operatorSocket = new WebSocket(`ws://localhost:8080/chat/operator?sessionId=${operatorSessionId}`);
        operatorSocket.onopen = () => console.log("Operator-WebSocket verbunden");
        operatorSocket.onmessage = event => {
            const msg = JSON.parse(event.data);
            displayMessage('operator-chat-box', msg.sender, msg.content);
        };
        operatorSocket.onclose = () => console.log("Operator-WebSocket geschlossen");
        operatorSocket.onerror = error => console.error("Operator-WebSocket Fehler:", error);
    }
}

// Online-Status für den Kunden toggeln
function toggleCustomerOnline() {
    const isOnline = document.getElementById("customer-online").checked;
    document.getElementById("sender").disabled = !isOnline;
    document.getElementById("customer-message-content").disabled = !isOnline;

    if (isOnline) {
        // Wenn keine Session-ID existiert, erstellen Sie eine neue
        if (!customerSessionId) {
            customerSessionId = "session_" + Math.random().toString(36).substring(2, 15);
            console.log("Kunden-Session gestartet:", customerSessionId);
        }

        // WebSocket-Verbindung neu aufbauen, falls sie geschlossen wurde
        if (!customerSocket || customerSocket.readyState === WebSocket.CLOSED) {
            initCustomerWebSocket();
        }
    } else if (customerSocket) {
        // Schließen der WebSocket-Verbindung und auf null setzen, wenn offline
        customerSocket.close();
        customerSocket = null;
    }
}


// Online-Status für den Operator toggeln
function toggleOperatorOnline() {
	const isOnline = document.getElementById("operator-online").checked;
	document.getElementById("operator-message-content").disabled = !isOnline;

	if (isOnline) {
	    // Wenn keine Session-ID existiert, erstellen Sie eine neue
	    if (!operatorSessionId) {
	        operatorSessionId = "session_" + Math.random().toString(36).substring(2, 15);
	        console.log("Operator-Session gestartet:", operatorSessionId);
	    }

	    // WebSocket-Verbindung neu aufbauen, falls sie geschlossen wurde
	    if (!operatorSocket || operatorSocket.readyState === WebSocket.CLOSED) {
	        initOperatorWebSocket();
	    }
	} else if (operatorSocket) {
	    // Schließen der WebSocket-Verbindung und auf null setzen, wenn offline
	    operatorSocket.close();
	    operatorSocket = null;
	}
}

// Nachricht senden von Kundenansicht
function sendCustomerMessage() {
	
	console.log("sendCustomerMessage aufgerufen");
    // Überprüfen, ob der Kunde auf Online geschaltet ist
    if (!document.getElementById("customer-online").checked) {
        alert("Bitte schalten Sie den Kunden-Chat auf Online.");
        console.log("Kunde ist offline, Nachricht wird nicht gesendet.");
        return;
    }

    const sender = document.getElementById('sender').value;
    const content = document.getElementById('customer-message-content').value;

    // Überprüfen, ob der Name des Senders und die Nachricht ausgefüllt sind
    if (!sender || !content) {
        alert('Bitte alle Felder ausfüllen');
        console.log("Name oder Nachricht fehlt:", { sender, content });
        return;
    }

    // Falls keine Kunden-Session-ID vorhanden ist, generieren wir eine neue
    if (!customerSessionId) {
        customerSessionId = "session_" + Math.random().toString(36).substring(2, 15);
        console.log("Kunden-Session gestartet:", customerSessionId);
    }

    // Überprüfen, ob der WebSocket initialisiert ist
    if (!customerSocket || customerSocket.readyState !== WebSocket.OPEN) {
        console.error("Kunden-WebSocket ist nicht verbunden oder geschlossen.");
        alert("WebSocket-Verbindung zum Server ist nicht aktiv. Bitte versuchen Sie es später erneut.");
        return;
    }

    const message = JSON.stringify({
        sender: sender,
        recipient: "Operator",
        content: content,
        sessionId: customerSessionId
    });

    // Loggen der Nachricht, bevor sie gesendet wird
    console.log("Senden der Nachricht über WebSocket:", message);

    // Senden der Nachricht über WebSocket
    try {
        customerSocket.send(message);
        console.log("Nachricht erfolgreich gesendet:", message);
    } catch (error) {
        console.error("Fehler beim Senden der Nachricht über WebSocket:", error);
    }

    // Nachricht lokal anzeigen
    displayMessage('customer-chat-box', sender, content);
    document.getElementById('customer-message-content').value = '';
}


function sendOperatorMessage() {
    console.log("sendOperatorMessage aufgerufen");

    // Überprüfen, ob der Operator auf Online geschaltet ist
    if (!document.getElementById("operator-online").checked) {
        alert("Bitte schalten Sie den Operator-Chat auf Online.");
        console.log("Operator ist offline, Nachricht wird nicht gesendet.");
        return;
    }

    const content = document.getElementById('operator-message-content').value;

    // Überprüfen, ob die Nachricht ausgefüllt ist
    if (!content) {
        alert('Bitte eine Nachricht eingeben');
        console.log("Nachricht fehlt:", { content });
        return;
    }

    // Falls keine Operator-Session-ID vorhanden ist, generieren wir eine neue
    if (!operatorSessionId) {
        operatorSessionId = "session_" + Math.random().toString(36).substring(2, 15);
        console.log("Operator-Session gestartet:", operatorSessionId);
    }

    // Überprüfen, ob der WebSocket initialisiert ist
    if (!operatorSocket || operatorSocket.readyState !== WebSocket.OPEN) {
        console.error("Operator-WebSocket ist nicht verbunden oder geschlossen.");
        alert("WebSocket-Verbindung zum Server ist nicht aktiv. Bitte versuchen Sie es später erneut.");
        return;
    }

    const message = JSON.stringify({
        sender: "Operator",
        recipient: "Customer",
        content: content,
        sessionId: operatorSessionId
    });

    // Loggen der Nachricht, bevor sie gesendet wird
    console.log("Senden der Nachricht über WebSocket:", message);

    // Senden der Nachricht über WebSocket
    try {
        operatorSocket.send(message);
        console.log("Nachricht erfolgreich gesendet:", message);
    } catch (error) {
        console.error("Fehler beim Senden der Nachricht über WebSocket:", error);
    }

    // Nachricht lokal anzeigen
    displayMessage('operator-chat-box', "Operator", content);
    document.getElementById('operator-message-content').value = '';
}


function setupEnterKeyListeners() {
    const customerMessageInput = document.getElementById('customer-message-content');
    const operatorMessageInput = document.getElementById('operator-message-content');

    customerMessageInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' && event.ctrlKey) {
            event.preventDefault(); // Verhindert einen Zeilenumbruch
            sendCustomerMessage();
        }
    });

    operatorMessageInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' && event.ctrlKey) {
            event.preventDefault(); // Verhindert einen Zeilenumbruch
            sendOperatorMessage();
        }
    });
}



// Hilfsfunktion zum Formatieren der Uhrzeit
function formatTime(date) {
    return `${date.getHours()}:${date.getMinutes().toString().padStart(2, '0')}`;
}

// Initialisierung beim Laden der Seite
window.onload = () => {
    toggleCustomerOnline();
    toggleOperatorOnline();
	setupEnterKeyListeners();
};
