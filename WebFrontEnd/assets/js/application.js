function interfaceInit(){

    /**
    *decleration the globe variabel
    */

    componentDOM= null;
    IdCount = 0;
    sendXML = '';
    componentXML = '';
    dataflowXML = '';
    focusCellId = '';
    recievedModelArray = new Array();
    recievedModelNumber = 0;
    drawModelArray = new Array();
    drawModelNumber = 0;
    ModelInfomation = '';
    newModelName = '';
    newModelDescription = '';

    paperAvailable = false;
    createInterface();
    paper.visible = false;

    /**
    * send message to sever to get model infomation
    */
    var actionStatus = 3;
    var ssid = "null";
    var sign_up_message = "null";
    var sendMessage = {
        action: actionStatus,
        sid: ssid,
        data: sign_up_message
    };
    websocket.send(json2str(sendMessage));

}

/**
* Create the Whole Interface
*/

function createInterface(){

  /**
*Create the Graphics interface
*/

    graph = new joint.dia.Graph;
    paper = new joint.dia.Paper({
        el: $('#paper'),
        gridSize: 50,
        model: graph,
        defaultLink: new joint.dia.Link({
          attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' } }
        }),
        validateConnection: function(cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
            if (magnetS && magnetS.getAttribute('type') === 'input') return false;
            if (cellViewS === cellViewT) return false;
            return magnetT && magnetT.getAttribute('type') === 'input';
        },
        validateMagnet: function(cellView, magnet) {
            return magnet.getAttribute('magnet') !== 'passive';
        },
        snapLinks: { radius: 25 }
    });

    /**
    * Get the Link of Component
    *Create the Component XML and Dataflow XML
    */

    graph.on('change:source change:target', function(link) {

        sourcePort = link.get('source').port;
        sourceId = link.get('source').id;
        targetPort = link.get('target').port;
        targetId = link.get('target').id;

        var sourceNode = '';
        var targetNode = '';
        var xmobj = $.parseXML(componentXML);

        var obj = $(xmobj).find("part").each(function(){
          var xmlAttr = $(this).attr("id");
            if (xmlAttr == sourceId) {
                $(this).find("output").each(function() {
                    if ($(this).attr("portName") == sourcePort) {
                        if(targetId != undefined) {
                            if($(this).attr("targetPortId")!="") {
                                var a = $(this).attr("targetPortId") + "#" + targetId;
                                $(this).attr("targetPortId", a);
                            } else{
                                $(this).attr("targetPortId",targetId);
                            }
                        }
                        if(targetPort != undefined) {
                            if($(this).attr("targetPortName")!="") {
                                var b = $(this).attr("targetPortName") + "#" + targetPort;
                                $(this).attr("targetPortName", b);
                            } else{
                                $(this).attr("targetPortName",targetPort);
                            }
                        }
                    }
                });
            }
            else if (xmlAttr == targetId) {
                $(this).find("input").each(function() {
                    if ($(this).attr("portName") == targetPort) {
                        if(sourceId != undefined) {
                            if($(this).attr("sourcePortId")!="") {
                                var a = $(this).attr("sourcePortId") + "#" + sourceId;
                                $(this).attr("sourcePortId", a);
                            } else{
                                $(this).attr("sourcePortId", sourceId);
                            }
                        }
                        if(sourcePort != undefined) {
                            if($(this).attr("sourcePortName")!="") {
                                var b = $(this).attr("sourcePortName") + "#" + sourcePort;
                                $(this).attr("sourcePortName", b);
                            } else{
                                $(this).attr("sourcePortName",sourcePort);
                            }
                        }
                    }
                });
            }
        });
        componentXML = new XMLSerializer().serializeToString(xmobj);
//        console.log(componentXML)

    });

    /**
    * To Get the Pointer Focus Cell
    */

    paper.on('cell:pointerdown' ,function(evt, x, y) {

        var evtId = "#" + evt.id;
        var cellId = $(evtId).attr("model-id");
        focusCellId = cellId;
        var m = graph.getCell(cellId);
        displayAttributes(m);

    });

    /**
    * To Create the Component XML
    */

    graph.on('add', function(cell) {

        if(cell instanceof joint.shapes.devs.Model) {

            var xmobj = $.parseXML(componentXML);
            IdCount = cell.id;
            var inputNumber = cell.get('inPorts').length;
            var outputNumber = cell.get('outPorts').length;
            var part ='';

            var obj = $(xmobj).find("parts").each(function(){

                var part = componentDOM.createElement('part');
                part.setAttribute('id', IdCount);
                var componentId = '';
                for(var i in drawModelArray) {
                    //console.log(recievedModelArray[i].id + " " + cellId);
                    if(drawModelArray[i].id == IdCount){
                        componentId = drawModelArray[i].modelId;
                    }
                }
                part.setAttribute('componentId', componentId);

                for(var i=0; i<inputNumber; i++) {
                    var input = componentDOM.createElement('input');
                    input.setAttribute('portName', cell.get('inPorts')[i]);
                    input.setAttribute('sourcePortName', '');
                    input.setAttribute('sourcePortId','');
                    input.setAttribute('value','');
                    input.setAttribute('fileName','');
                    part.appendChild(input);
                }

                for(var i=0; i<outputNumber; i++) {
                    var output = componentDOM.createElement('output');
                    output.setAttribute('portName', cell.get('outPorts')[i]);
                    output.setAttribute('targetPortName', '');
                    output.setAttribute('targetPortId','');
                    part.appendChild(output);
                }

                $(this).append(part);

            });
            // xmobj.appendChild(part);

            componentXML = new XMLSerializer().serializeToString(xmobj);
            console.log(componentXML);

        }

    });

    /**
    * Create the Model Display area
    */
    graph1 = new joint.dia.Graph;
    paper1 = new joint.dia.Paper({
        el:$('#model'),
        gridSize: 20,
        model: graph1
    });


    paper1.on('cell:pointerdblclick' ,function(evt, x, y) {

        if(paperAvailable) {
            var evtId = "#" + evt.id;
            var cellId = $(evtId).attr("model-id");
            var m = graph1.getCell(cellId);
            var m1 = '';

            for (var i in recievedModelArray) {
                if (recievedModelArray[i].id == m.id) {
                    m1 = recievedModelArray[i];
                    recievedModelArray[i] = m1;
                    break;
                }
            }

            if (m1 != '') {
                var m2 = m.clone();
                m2.position(200,30);
//                m2.translate(100, 0);
                var m3 = new Model(m1.componentName, m1.componentDescription, m1.inputs, m1.outputs, m1.parameters, m1.modelId, m2.id);
                drawModelArray[drawModelNumber] = m3;
                drawModelNumber++;
                graph.addCell(m2);
            }
        }
    });

    /**
    * Create the Tool Display area
    */

    graph2 = new joint.dia.Graph;
    paper2 = new joint.dia.Paper({
        el: $('#tool'),
        gridSize: 20,
        model: graph2
    });

}

/**
* Decleration of the Model Class
*/

function Model(componentName, componentDescription, inputs, outputs, parameters, modelId, id){ 
    this.componentName = componentName; 
    this.componentDescription = componentDescription; 
    this.inputs = inputs; 
    this.outputs = outputs;
    this.parameters = parameters;
    this.modelId = modelId;  //服务端保留的模型id
    this.id = id;   //界面modelid
} 

/**
* Create the Recieved Model
*/

function createModel(){
    if ( ModelInfomation != '') {
        dealRecieveModel(ModelInfomation);
        var position_y = 10;
        var modelHeight = 0;
        for(var i=0; i<recievedModelNumber; i++ ) {
            var inPortsArray = new Array();
            for (var j=0; j<recievedModelArray[i].inputs.length; j++) {
                inPortsArray[j] = recievedModelArray[i].inputs[j][0];
            }
            var outPortsArray = new Array();
            for (var j=0; j<recievedModelArray[i].outputs.length; j++) {
                outPortsArray[j] = recievedModelArray[i].outputs[j][0];
            }
            // var outPortsArray = new Array();
            // for (var j=0; j<recievedModelArray[i].outputs.length; j++) {
            //   outPortsArray[j] = recievedModelArray[i].outputs[j][0];
            // }
            // console.log(recievedModelArray[i].componentName);
            // var test = ['df','sdf'];
            if(inPortsArray.length > outPortsArray.length){
                modelHeight = 40*inPortsArray.length;
            }
            else {
                modelHeight = 40*outPortsArray.length;
            }
            var m1 = new joint.shapes.devs.Model({
                position: { x: 150, y: position_y},
                size: { width: 60, height: modelHeight },
                inPorts: inPortsArray,
                outPorts: outPortsArray,
                attrs: {
                    '.label': { text: recievedModelArray[i].componentName, 'ref-x': .60, 'ref-y': .25 },
                    rect: { fill: '#fffff' },
                    '.inPorts circle': { fill: '#98BDD4', magnet: 'passive', type: 'input' },
                    '.outPorts circle': { fill: '#CCCCCC', type: 'output' },
                    text: { fill: '#676767','pointer-events': ''}
                }
            });
            recievedModelArray[i].id = m1.id;
            graph1.addCell(m1);
            position_y += modelHeight + 20;
//            console.log(position_y);
        }
    }
}

/**
* Deal with the Recieved Model XML
* Create the RecievedModel Array
* with the propoties: 
* omponentName, componentDescription, inputs, outputs, parameters
*/

function dealRecieveModel(modelXML){
    var obj =$(modelXML).find("component").each(function(){

        var componentId = $(this).find("componentId").text();;
        var inputArray = new Array();
        var inputNumber = 0;
        var outputArray = new Array();
        var outputNumber = 0;
        var parameterArray = new Array();
        var parameterNumber = 0;

        var componentName = $(this).find("componentName").text();
        var componentDescription = $(this).find("componentDescription").text();
        $(this).find("inputName").each(function(){
            var inputName = $(this).text();
            var inputType = $(this).next().text();
            var inputValue = '';
            if (inputName != '') {
                inputArray[inputNumber] = new Array(inputName, inputType, inputValue);
                inputNumber++;
            }
        });

        $(this).find("outputName").each(function(){
            var outputName = $(this).text();
            var outputType = $(this).next().text();
            var outputValue = '';
            if (outputName != '') {
                outputArray[outputNumber] = new Array(outputName, outputType, outputValue);
                outputNumber++;
            }
        });

        $(this).find("parameterName").each(function(){
            var parameterName = $(this).text();
            var parameterType = $(this).next().text();
            var parameterValue = '';
            if (parameterName != '') {
                parameterArray[parameterNumber] = new Array(parameterName, parameterType, parameterValue);
                parameterNumber++;
            }
        });

        var m1 = new Model(componentName, componentDescription, inputArray, outputArray, parameterArray, componentId, "");
        recievedModelArray[recievedModelNumber] = m1;
        recievedModelNumber += 1;
    });

}

function newModel(){
    if(!paperAvailable) {
        $('#newModel').modal('show');
    } else{
        clearComponent();
        $('#attributesArea').html("");
        $("#console").html("");


    }

}

function newModelConfirm() {
    if(!$('#inputModelName').val()) {
        $('#inputModelName').popover('show');
    } else{
        newModelName = $('#inputModelName').val();
        newModelDescription = $("#textArea").val();
        $('#newModel').modal('hide');
        $('#paper').css('background-color', '#ffffff');
        paperAvailable = true;

        if(document.implementation && document.implementation.createDocument) {
            componentDOM = document.implementation.createDocument('', '', null);
            var component = componentDOM.createElement('component');
            var from = componentDOM.createElement('from');
            component.appendChild(from);
            var to = componentDOM.createElement('to');
            component.appendChild(to);
            var componentName = componentDOM.createElement('name');
            var nameText = componentDOM.createTextNode(newModelName);
            componentName.appendChild(nameText);
            component.appendChild(componentName);
            var parts = componentDOM.createElement('parts');
            component.appendChild(parts);
            componentXML = "";
            componentXML = new XMLSerializer().serializeToString(component);
            //console.log(componentXML);
        }
    }
}

function logOut() {
    $('#logOutConform').modal('show');
}

function logOutConform(){
    var actionStatus = 1;
    var ssid = "null";
    var message = "null";
    var sendMessage = {
        action: actionStatus,
        sid: ssid,
        data: message
    };
    websocket.send(json2str(sendMessage));
    console.log(json2str(sendMessage));
}
function logOutSuccess(){
    $('#logOutConform').modal('hide');
    $('#logOutSuccess').modal('show');
    var storage = window.localStorage;
    storage.removeItem('SID');
}
function logOutFailed(){
    $('#logOutConform').modal('hide');
    $('#logOutFailed').modal('show');
}
function relogin(){
    location.reload();
}

function sendXmlToServer() {
    var actionStatus = 4;
    var ssid = "null";
    var modelMessage = componentXML.replace(/"([^"]*)"/g, "'$1'");
    var sendMessage = {
        action: actionStatus,
        sid: ssid,
        data: modelMessage
    };
    console.log("here:"+json2str(sendMessage));
    websocket.send(json2str(sendMessage));
}

function clearComponent() {
    var xmobj = $.parseXML(componentXML);
    var obj = $(xmobj).find("part").each(function(){
        $(this).remove()
    });
    graph.clear();        
    componentXML = new XMLSerializer().serializeToString(xmobj);
    console.log(componentXML);
}

function deleteComponent() {
    if (focusCellId != '') {
        var xmobj = $.parseXML(componentXML);
        var deleteCell = graph.getCell(focusCellId);
        if(deleteCell instanceof joint.shapes.devs.Model) {
            var obj = $(xmobj).find("part").each(function(){
                var xmlAttr=$(this).attr("id");
                if (xmlAttr == focusCellId) {
                    $(this).remove()
                    deleteCell.remove();
                    focusCellId = '';
                }
            });
        }
         
        componentXML = new XMLSerializer().serializeToString(xmobj);
        console.log(componentXML);
    }

}

function displayAttributes(m){
    //name 为 input（or output or parameter） 的portName的最后一个单词
    //id 为 input（or output or parameter） 的portName（去除所有的点）

    var node = '';
    if(m instanceof joint.shapes.devs.Model){
        for(var i in drawModelArray) {
            if(drawModelArray[i].id == m.id){
            node = drawModelArray[i];
            break;
            }
        }

        var html = '<div class="jumbotron">' +
            '<h5>Description:</h5> ' +
            '<p>'+ node.componentDescription +'</p></div>';

        if (node.inputs.length != 0){
            html += '<div class="col-lg-12"><h4 class="list-group-item-heading">Inputs</h4></div>';
            for (var i in node.inputs){
                if(!judgeInputEnable(node.id,node.inputs[i][0])) {

                    html += '<div class="form-group">'
                        + '<label class="control-label" for="inputSmall">'
                        + node.inputs[i][0]
                        +'</label>'
                        +'<div class="input-group">'
                        + '<input type="text" disabled="" class="form-control input-sm" id="'+ node.inputs[i][0].replace(/\./g,"") +'" ' +
                        'placeholder="' + node.inputs[i][1] + '" value="' + showExistValue(node.id,node.inputs[i][0]) +'"/>' +
                        '<span class="input-group-btn">' +
                        '<button class="btn btn-primary btn-sm" type="button" disabled>...</button>'
                        +'</div></div>';
                }
                else{
                    html += '<div class="form-group">'
                        + '<span for="inputSmall">'
                        + node.inputs[i][0]
                        +'</span>'
                        +'<div class="input-group">'
                        +'<input type="text" class="form-control input-sm"id="'+ node.inputs[i][0].replace(/\./g,"") +'" ' +
                        ' placeholder="' + node.inputs[i][1] + '" value="' + showExistValue(node.id,node.inputs[i][0])
                        + '" /><span class="input-group-btn"><button class="btn btn-primary btn-sm" type="button" onclick="createInputDialog(\''
                        +node.inputs[i][0].replace(/\./g,"")+"dialog"+'\')">...</button>' +
                        '</div></div>';
                }
            }
        }
//        if (node.parameters.length != 0){
//
//            html += '<div class="col-lg-12"><h4 class="list-group-item-heading">Parameters</h4></div>';
//            for (var i in node.parameters){
//                html += '<div class="form-group">'
//                    + '<span for="inputSmall">'
//                    + node.parameters[i][0]
//                    +'</span>'
//                    +'<div class="input-group">'
//                    +'<input type="text" class="form-control input-sm"id="'+ node.parameters[i][0].replace(/\./g,"") +'" ' +
//                    ' placeholder="' + node.parameters[i][1] + '" value="' + showExistValue(node.id,node.parameters[i][0])
//                    + '" /><span class="input-group-btn"><button class="btn btn-primary btn-sm" type="button" onclick="createInputDialog(\''
//                    +node.parameters[i][0].replace(/\./g,"")+"dialog"+'\')">...</button>' +
//                    '</div></div>';
//            }
//        }
//        if (node.outputs.length != 0){
//
//            html += '<div class="form-group"><label class="label label-primary">Outputs</label>';
//            for (var i in node.outputs){
//                html += '<div class="input-group"><span class="input-group-addon input-sm">'
//                    +node.outputs[i][0].substring(node.outputs[i][0].lastIndexOf(".")+1, node.outputs[i][0].length)
//                + '</span><input type="text" class="form-control input-sm" disabled="" id="'+ node.outputs[i][0].replace(/\./g,"") +'" placeholder="'+ node.outputs[i][1]
//                +'" ><span class="input-group-btn"><button class="btn btn-primary btn-sm" type="button">...</button></span></div>';
//
//            }
//            html += '</div>';
//        }
        html += '</div>';
        html += '<button type="button" class="btn btn-default btn-sm btn-block" onclick="setSingleData()">确定</button><button type="button" class="btn btn-default btn-sm btn-block">取消</button>';
        $('#attributesArea').html(html);
    }
}

function judgeInputEnable(nodeId,portName){
    var xmobj = $.parseXML(componentXML);
    var key = 0;
    var obj = $(xmobj).find("part").each(function(){
        var xmlAttr = $(this).attr("id");
        if (xmlAttr == nodeId) {
            $(this).find("input").each(function() {
                if ($(this).attr("portName") == portName) {
                    if($(this).attr("sourcePortId") == ''){
                        key = 0;
                    } else{
                        key = 1;
                    }
                }
            });
        }
    });
    if(key == 1){
        return false;
    }
    else return true;
}

function showExistValue(nodeId,portName){

    var xmobj = $.parseXML(componentXML);
    var value = '';
    var obj = $(xmobj).find("part").each(function(){
        var xmlAttr = $(this).attr("id");
        if (xmlAttr == nodeId) {
            $(this).find("input").each(function() {
                if ($(this).attr("portName") == portName) {
                    value = $(this).attr("value");
                }
            });
        }
    });
    return value;
}

function setSingleData() {

    var xmobj = $.parseXML(componentXML);
    var node = '';
    var obj = $(xmobj).find("part").each(function(){
        var xmlAttr = $(this).attr("id");
        if (xmlAttr == focusCellId) {
            for(var i in drawModelArray) {
                if(drawModelArray[i].id == focusCellId){
                    node = drawModelArray[i];
                    break;
                }
            }
            $(this).find("input").each(function() {

                for(var i=0;i<node.inputs.length;i++) {
                    if ($(this).attr("portName") == node.inputs[i][0]) {
                        var elementId = "#"+node.inputs[i][0].replace(/\./g,"");
                        if($(this).attr("fileName") == "") {
                            $(this).attr("value", $(elementId).val());
                            console.log("value" + $(elementId).val())
                        }
                    }
                }
            });
        }
    });
    componentXML = new XMLSerializer().serializeToString(xmobj);

    console.log(componentXML);

}

function createInputDialog(dialogId){
    var fileName = "";
    var fileData = "";
    var baseId = dialogId.substring(0,dialogId.length-6);
    var xmobj = $.parseXML(componentXML);
    var node = '';
    var obj = $(xmobj).find("part").each(function(){
        var xmlAttr = $(this).attr("id");
        if (xmlAttr == focusCellId) {
            for(var i in drawModelArray) {
                if(drawModelArray[i].id == focusCellId){
                    node = drawModelArray[i];
                    break;
                }
            }
            $(this).find("input").each(function() {
                if ($(this).attr("portName").replace(/\./g,"")+"dialog" == dialogId) {
                    var elementId = "#" + baseId + "Text";
                    console.log(elementId);
                    console.log($(elementId).val());
                    fileName = $(this).attr("fileName");
                    if(fileName != ""){
                        fileData = $(this).attr("value").replace(/\<br\>/g, "\n");
                    }
                }
            });
        }
    });

    console.log("content = " + fileData);
    var dialogHtml = '' +
        '<div class="modal fade" id="'+dialogId+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<span>Input</span>' +
        '<button type="button" class="close" data-dismiss="modal" onclick="">' +
        '<span aria-hidden="true">&times;</span>' +
        '<span class="sr-only">Close</span>' +
        '</button></div>' +
        '<div class="modal-body">' +
        '<form class="form-horizontal">' +
        '<fieldset>' +
        '<div class="form-group">' +
        '<label for="'+baseId+"FileName"+'" class="col-lg-2 control-label">FileName</label>' +
        '<div class="col-lg-10"><input type="text" class="form-control" id="'+baseId+"FileName"+'" placeholder="File Name"' +
        ' data-container="body" data-toggle="popover" data-placement="right" data-content="文件名不能为空" value="">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="'+baseId+"Text"+'" class="col-lg-2 control-label">Data</label>' +
        '<div class="col-lg-10">' +
        '<textarea class="form-control" rows="10" id="'+baseId+"Text"+'" value="">' +
        '</textarea>' +
        '<span class="help-block">use "," to split inputs.</span>' +
        '</div></div>' +
        '</fieldset>' +
        '</form></div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>' +
        '<button type="button" class="btn btn-primary" onclick="setMultipleData(\''+dialogId+'\')">Finish</button>' +
        '</div></div></div></div>';

    $("#inputDialog").html(dialogHtml);
    $("#"+baseId+"FileName").val(fileName);
    $("#"+baseId+"Text").val(fileData);
    $('#'+dialogId).modal("show");
}

function setMultipleData(dialogId){

    var baseId = dialogId.substring(0,dialogId.length-6);
    var fileName = $("#" + baseId + "FileName").val();
    if(fileName == ""){
        $("#" + baseId + "FileName").popover("show");
        return;
    }
    var xmobj = $.parseXML(componentXML);
    var node = '';
    var obj = $(xmobj).find("part").each(function(){
        var xmlAttr = $(this).attr("id");
        if (xmlAttr == focusCellId) {
            for(var i in drawModelArray) {
                if(drawModelArray[i].id == focusCellId){
                    node = drawModelArray[i];
                    break;
                }
            }
            $(this).find("input").each(function() {
//                console.log($(this).attr("portName").replace(/\./g,"")+"dialog");
//                console.log(dialogId);
                if ($(this).attr("portName").replace(/\./g,"")+"dialog" == dialogId) {
                    var elementId = "#" + baseId + "Text";
//                    console.log(elementId);
//                    console.log($(elementId).val());
                    $(this).attr("value", $(elementId).val().replace(/\n/g, "<br>"));
                    $(this).attr("fileName",fileName);
                }
            });
        }
    });
    componentXML = new XMLSerializer().serializeToString(xmobj);
    console.log(componentXML);
    $('#'+dialogId).modal("hide");
//    $('#'+dialogId).remove();

}

function exportModel(){
    var actionStatus = 6;
    var ssid = "null";
    var exportMessage = "<message><modelName>"+newModelName+"</modelName><description>"+newModelDescription+"</description></message>"
    var sendMessage = {
        action:actionStatus ,
        sid : ssid,
        data:exportMessage
    };
    console.log(json2str(sendMessage));
    websocket.send(json2str(sendMessage));
}