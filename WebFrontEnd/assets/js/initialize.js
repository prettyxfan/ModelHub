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
    result = "";
    T = new Array();
    X = new Array();
    Y1 = new Array();
    Y2 = new Array();
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
//    console.log(event.data);
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
    else if(action == 'run') {
        if(message.StatusCode == 0){

        }
        else if(message.StatusCode == 1){
            console.log(message.message);
            var span = document.createElement('span');
            span.innerHTML = message.message + "<br/>";
            $("#console").append(span);
        }
    }
    else if(action == 'analysis') {
        if(message.StatusCode == 0) {

        }
        else if(message.StatusCode == 1){
            result = message.message;
        }
    }
}

function dealAnalysisData(s){
    var strings = result.split("#");
    var x1 = [];
    var y1 = [];
    var y2 = [];
    var tableNumber = 0;
    var i = 0;
    for(var x in strings){
        var s = strings[x].split(",");
        if(s.length == 2 && s[0] == '@T'){
            T[tableNumber] = s[1];
            if(tableNumber!=0){
                X[tableNumber-1] = x1;
                Y1[tableNumber-1] = y1;
                Y2[tableNumber-1] = y2;
            }
            tableNumber++;
            x1 = [];
            y1 = [];
            y2 = [];
            i = 0;
            continue;
        }
        if(s.length == 4 && s[0] == ""){
            x1[i] = s[1];
            y1[i] = parseFloat(s[2]);
            y2[i] = parseFloat(s[3]);
            i++;
        }
    }

}

function displayLineChart(){
    result = "@T, tw# Created, Thu Oct 09 10:20:09 CST 2014# date_format, yyyy-MM-dd#@H, date, runoff, daylen# Type  , Date,   Real,   Real# Format, yyyy-MM-dd,0.00,0.00#,1960-01-01,   75.00,   10.21#,1960-02-01,   47.93,   11.01#,1960-03-01,   26.12,   12.05#,1960-04-01,   13.06,   13.20#,1960-05-01,   22.00,   14.16#,1960-06-01,   11.00,   14.66#,1960-07-01,    5.50,   14.42#,1960-08-01,    2.75,   13.57#,1960-09-01,    1.38,   12.44#,1960-10-01,    0.69,   11.33#,1960-11-01,    0.34,   10.39#,1960-12-01,    0.17,    9.96#,1961-01-01,    0.09,   10.21#,1961-02-01,    0.97,   11.01#,1961-03-01,   24.52,   12.05#,1961-04-01,   12.26,   13.20#,1961-05-01,    6.13,   14.16#,1961-06-01,    3.07,   14.66#,1961-07-01,    1.53,   14.42#,1961-08-01,    0.77,   13.57#,1961-09-01,    0.38,   12.44#,1961-10-01,    0.19,   11.33#,1961-11-01,   27.25,   10.39#,1961-12-01,   25.37,    9.96#,1962-01-01,   19.52,   10.21#,1962-02-01,    9.76,   11.01#,1962-03-01,    5.58,   12.05#,1962-04-01,    8.93,   13.20#,1962-05-01,    4.47,   14.16#,1962-06-01,    2.23,   14.66#,1962-07-01,    1.12,   14.42#,1962-08-01,    0.56,   13.57#,1962-09-01,    0.28,   12.44#,1962-10-01,    0.14,   11.33#,1962-11-01,    0.07,   10.39#,1962-12-01,    0.03,    9.96#,1963-01-01,    0.02,   10.21#,1963-02-01,    5.78,   11.01#,1963-03-01,    2.89,   12.05#,1963-04-01,    1.45,   13.20#,1963-05-01,    0.72,   14.16#,1963-06-01,    0.36,   14.66#,1963-07-01,    0.18,   14.42#,1963-08-01,    0.09,   13.57#,1963-09-01,    0.05,   12.44#,1963-10-01,    0.02,   11.33#,1963-11-01,    0.01,   10.39#,1963-12-01,    0.01,    9.96#,1964-01-01,    0.00,   10.21#,1964-02-01,    0.00,   11.01#,1964-03-01,    0.00,   12.05#,1964-04-01,    0.00,   13.20#,1964-05-01,    0.00,   14.16#,1964-06-01,    0.00,   14.66#,1964-07-01,    0.00,   14.42#,1964-08-01,    0.00,   13.57#,1964-09-01,    0.00,   12.44#,1964-10-01,    0.00,   11.33#,1964-11-01,    0.00,   10.39#,1964-12-01,    0.00,    9.96#,1965-01-01,    0.00,   10.21#,1965-02-01,    0.00,   11.01#,1965-03-01,    0.00,   12.05#,1965-04-01,    0.00,   13.20#,1965-05-01,    0.00,   14.16#,1965-06-01,    0.00,   14.66#,1965-07-01,    0.00,   14.42#,1965-08-01,    0.00,   13.57#,1965-09-01,    0.00,   12.44#,1965-10-01,    0.00,   11.33#,1965-11-01,    0.00,   10.39#,1965-12-01,    0.00,    9.96#,1966-01-01,    0.00,   10.21#,1966-02-01,    0.00,   11.01#,1966-03-01,    0.00,   12.05#,1966-04-01,    0.00,   13.20#,1966-05-01,    0.00,   14.16#,1966-06-01,    0.00,   14.66#,1966-07-01,    0.00,   14.42#,1966-08-01,    0.00,   13.57#,1966-09-01,    0.00,   12.44#,1966-10-01,    0.00,   11.33#,1966-11-01,    0.00,   10.39#,1966-12-01,    0.00,    9.96#,1967-01-01,    0.00,   10.21#,1967-02-01,    0.00,   11.01#,1967-03-01,    0.00,   12.05#,1967-04-01,    0.00,   13.20#,1967-05-01,    0.00,   14.16#,1967-06-01,    0.00,   14.66#,1967-07-01,    0.00,   14.42#,1967-08-01,    0.00,   13.57#,1967-09-01,    0.00,   12.44#,1967-10-01,    0.00,   11.33#,1967-11-01,    0.00,   10.39#";
    var strings = result.split("#");
    var x1 = new Array();
    var y1 = new Array();
    var y2 = new Array();
    var i = 0;
    for(var x in strings){
        var s = strings[x].split(",");
        if(s.length == 4 && s[0] == ""){
            x1[i] = s[1];
            y1[i] = parseFloat(s[2]);
            y2[i] = parseFloat(s[3]);
            i++;
        }
        // console.log(line.length);
    }
    console.log(y1);
    $("#canvasDialog").modal('show');
    line(x1,y1,y2);
}

function displayBarChart(){
    $("#canvasDialog").modal('show');
    bar(x,y1,y2);

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
