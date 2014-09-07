function init() {
    websocket = new WebSocket("ws://localhost:8528");
    requireTime = 0;
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
    var storage = window.localStorage;
    var getData = storage.getItem('SID');
    if(getData == null) {
        $("#login").css("display","block"); 
        $("#modelInterface").css("display","none"); 
    } else{
        if(websocket.readyState == 1 && requireTime == 0) {
            var storage = window.localStorage;
            console.log(storage.getItem('SID'));
            var getData = storage.getItem('SID');
            var sidJSON = eval('(' + getData + ')');
            var actionStatus = 0;
            var ssid = sidJSON.sid;
            var login_message = "null";
            var sendMessage = {
                action:actionStatus , 
                sid : ssid,
                data:login_message
            };
            websocket.send(json2str(sendMessage));
        }
        else {
                $("#login").css("display","block"); 
                $("#modelInterface").css("display","none"); 
        }
    }
    requireTime = 1;
}

function onMessage(event) {
    console.log(event.data);
    var message = eval('(' + event.data + ')');
    var action = message.action;
    if(action == 'login') {
        if(message.StatusCode == 0) {
            $('#login-username-email').popover('show');
        }
        else if(message.StatusCode == 1) {
            $('#login-password').popover('show');
        }
        else if(message.StatusCode == 2) {
            if(document.getElementById("check_id").checked){
                var login_username_email = $("#login-username-email").val();
                var user = {
                    sid : message.message,
                    name : login_username_email
                };
                var storage = window.localStorage;
                storage.setItem('SID', json2str(user));
                console.log(storage.getItem('SID'));
            }  else {
                var storage = window.localStorage;
                storage.removeItem('SID');
            }
            $("#login").css("display","none"); 
            $("#modelInterface").css("display","block"); 
            interfaceInit();
        }
        else if(message.StatusCode == 3) {
            $("#login").css("display","block"); 
            $("#modelInterface").css("display","none");     
        }
        else if(message.StatusCode == 4) {
            $("#login").css("display","none"); 
            $("#modelInterface").css("display","block"); 
            interfaceInit();
        }
    }
    else if(action == 'sign_up') {
        if(message.StatusCode == 5) {
            $('#sign-up-email').popover('show');
        }
        else if(message.StatusCode == 6){
            $('#sign-up-username').popover('show');
        }
        else if(message.StatusCode == 7){
            $('#myModal').modal('show');
        }

    }
    else if(action == 'logOut') {
        if(message.StatusCode == 0) {
            logOutFailed();
        }
        else if(message.StatusCode == 1) {
            logOutSuccess();
        }
    }
    else if(action == 'get model') {
        ModelInfomation = message.message;
        console.log("get model ! " + message.message);
        createModel();
    }
    else if(action == 'compile') {
        if(message.StatusCode == 0) {

        }
        else if(message.StatusCode == 1) {

        }
    }
}

function backToLogin(){
    $('#myModal').modal('hide');
    $('#form-sign-up')[0].reset();
    $('#login_a').click();
}

function onError(event) {
    $('#main-frame-error').css("display","block");
//    alert(event.data);
}

function onClose(event) {
    websocket.close();
}

function start() {
    var login_username_email = $("#login-username-email").val();
    var login_password = $("#login-password").val();
    var remember = false;
    if(document.getElementById("check_id").checked) {
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
    console.log(json2str(sendMessage));
    websocket.send(json2str(sendMessage));
}

function signUp(){
    var userName = $('#sign-up-username').val();
    var userEmail = $('#sign-up-email').val();
    var password = $('#sign-up-password').val();
    var actionStatus = 2;
    var ssid = "null";
    var sign_up_message = "<message><userName>"+userName+"</userName><userEmail>"+userEmail+"</userEmail><password>"+password+"</password></message>";
    var sendMessage = {
        action: actionStatus,
        sid: ssid,
        data: sign_up_message
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
