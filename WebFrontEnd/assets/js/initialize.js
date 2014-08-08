function init() {
    websocket = new WebSocket("ws://localhost:8528");
    websocket.onopen = function(event) { 
        console.log("connected ! " + evt.data);
        onOpen(event);
    }; 
    websocket.onclose = function(event) { 
        console.log("disconncted ! " + evt.data);
        onClose(event);
    }; 
    websocket.onmessage = function(event) { 
        console.log("get message ! " + evt.data);
        onMessage(event);
    }; 
    websocket.onerror = function(event) { 
        console.log("error ! " + evt.data); 
        onError(event);
    }; 
}

window.addEventListener("load", init(), false);

function onOpen(event) {
    console.log("Connection established");
}

function onMessage(event) {
    console.log(event.data);
}

function onError(event) {
    alert(event.data);
}

function onClose(event) {
    websocket.close();
}

function start() {
    var login_username_email = $("#login-username-email").val();
    var login_password = $("#login-password").val();
    var remember = false;
    if($("#chk_id").attr('checked') != undefined) {
        remember = true;
    }
    var login_message = "<login><action>login</action><username_email>" + login_username_email + "</login_username_email><password>" + login_password + "</password><remember>" + remember + "</remember></login>";
    webSocket.send('hello');
    return false;
}

