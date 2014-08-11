function init() {
    websocket = new WebSocket("ws://localhost:8528");
    websocket.onopen = function(event) { 
        console.log("connected ! " + event.data);
        onOpen(event);
    }; 
    websocket.onclose = function(event) { 
        console.log("disconncted ! " + event.data);
        onClose(event);
    }; 
    websocket.onmessage = function(event) { 
        console.log("get message ! " + event.data);
        onMessage(event);
    }; 
    websocket.onerror = function(event) { 
        console.log("error ! " + event.data); 
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
    if(!!!$("#check_id").attr("checked")) {
        remember = true;
    }
    var actionStatus = 0;
    var ssid = "null";
    var login_message = "<message><username_email>"+login_username_email+"</username_email><password>"+login_password+"</password><remember>"+remember+"</remember></message>"
    var sendMessage = {
        action:actionStatus , 
        sid : ssid,
        data:login_message
    };
    websocket.send(json2str(sendMessage));

}


function json2str(obj) {
  var S = [];
  for(var i in obj){
      if(typeof obj[i] == 'string'){
          S.push(i + ':"' + obj[i] + '"'); 
      }
      else           
        S.push(i + ':' + obj[i]); 
  }
    return '{'+S.join(',')+'}';
}
