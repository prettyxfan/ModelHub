function init() {

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

  createInterface();

  websocket = new WebSocket("ws://localhost:8528");
  websocket.onopen = function(evt) { 
    console.log("connected ! " + evt.data);
    websocket.send("Get Model");
  }; 
  websocket.onclose = function(evt) { 
    console.log("disconncted ! " + evt.data);
    websocket.close();
  }; 
  websocket.onmessage = function(evt) { 
    ModelInfomation = evt.data;
    console.log("get message ! " + evt.data);
    createModel();
  }; 
  websocket.onerror = function(evt) { 
    console.log("error ! " + evt.data); 
  }; 

  if(document.implementation && document.implementation.createDocument) {
    componentDOM = document.implementation.createDocument('', '', null);
    var component = componentDOM.createElement('component');
    var from = componentDOM.createElement('from');
    component.appendChild(from);
    var to = componentDOM.createElement('to');
    component.appendChild(to);
    var parts = componentDOM.createElement('parts');
    component.appendChild(parts);
    componentXML = "";
    componentXML = new XMLSerializer().serializeToString(component);
    //console.log(componentXML); 
  }

  // if(document.implementation && document.implementation.createDocument) {
  //   dataFlowDOM = document.implementation.createDocument('', '', null);
  //   var dataflow = dataFlowDOM.createElement('dataflow');
  //   var from = dataFlowDOM.createElement('from');
  //   dataflow.appendChild(from);
  //   var to = dataFlowDOM.createElement('to');
  //   dataflow.appendChild(to);
  //   var components = dataFlowDOM.createElement('components');
  //   dataflow.appendChild(components);
  //   dataflowXML = "";
  //   dataflowXML = new XMLSerializer().serializeToString(dataflow);
  //   console.log(dataflowXML); 
  // }

}

window.addEventListener("load", init(), false);





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
      gridSize: 1,
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
      snapLinks: { radius: 35 }
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
      var xmlAttr=$(this).attr("id");
        if (xmlAttr == sourceId) {
          $(this).find("outputId").each(function() {
            if ($(this).attr("portName") == sourcePort) {
              $(this).text(targetId);
              $(this).attr("targetPortName", targetPort);
            }
          });
        }
        else if (xmlAttr == targetId) {
          $(this).find("inputId").each(function() {
            if ($(this).attr("portName") == targetPort) {
              $(this).text(sourceId);
              $(this).attr("sourcePortName", sourcePort);
            }
          }); 
        }
    });
    componentXML = new XMLSerializer().serializeToString(xmobj);
        
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

          for(var i=0; i<inputNumber; i++) {
            var inputId = componentDOM.createElement('inputId');
            inputId.setAttribute('portName', cell.get('inPorts')[i]);
            inputId.setAttribute('sourcePortName', '');
            part.appendChild(inputId);
          }

          for(var i=0; i<outputNumber; i++) {
            var outputId = componentDOM.createElement('outputId');
            outputId.setAttribute('portName', cell.get('outPorts')[i]);
            outputId.setAttribute('targetPortName', '');
            part.appendChild(outputId);
          }

          $(this).append(part);

        });
      // xmobj.appendChild(part);
           
      componentXML = new XMLSerializer().serializeToString(xmobj);
      console.log(componentXML);

     //  var xmobj = $.parseXML(dataflowXML);
     //  IdCount = cell.id;
     //  var inputNumber = cell.get('inPorts').length;
     //  var outputNumber = cell.get('outPorts').length;
     //  // var component ='';
     //    var obj = $(xmobj).find("components").each(function(){
     //      var component = dataFlowDOM.createElement('component'); 
     //      component.setAttribute('id', IdCount);
     //      component.setAttribute('type', 'model');

     //      for(var i=0; i<inputNumber; i++) {
     //        var inputId = componentDOM.createElement('inputId');
     //        inputId.setAttribute('portName', cell.get('inPorts')[i]);
     //        inputId.setAttribute('sourcePortName', '');
     //        component.appendChild(inputId);
     //      }
     //      for(var i=0; i<outputNumber; i++) {
     //        var outputId = componentDOM.createElement('outputId');
     //        outputId.setAttribute('portName', cell.get('outPorts')[i]);
     //        // outputId.setAttribute('targetPortName', '');
     //        component.appendChild(outputId);
     //      }
     //      $(this).append(component);

     //    });
           
     //  dataflowXML = new XMLSerializer().serializeToString(xmobj);
     // console.log(dataflowXML);
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

  /**
  * To Show the Simple Introduction of the Model
  */

  paper1.on('cell:pointerdown', function(evt, x, y){

    var evtId = "#" + evt.id;
    var id = $(evtId).attr("model-id");
    var m = graph1.getCell(id);

    if( m instanceof joint.shapes.devs.Model ){
      for(var i in recievedModelArray) {
        //console.log(recievedModelArray[i].id + " " + cellId);
        if(recievedModelArray[i].id == id){
          $('#content').html(recievedModelArray[i].componentDescription);
        }
      }
    }
    else {
      $('#content').html('');
    }

  });

  paper1.on('cell:pointerdblclick' ,function(evt, x, y) {
    var evtId = "#" + evt.id;
    var cellId = $(evtId).attr("model-id");
    var m = graph1.getCell(cellId);
    var m1 = '';


    for(var i in recievedModelArray) {
      // console.log(recievedModelArray[i]);
      if( recievedModelArray[i].id == m.id ) {
        m1 = recievedModelArray[i];
        break;
      }
    }
    if( m1!='') {
      var m2 = m.clone();
      m2.translate(100, 0);
      m1.id = m2.id;
      drawModelArray[drawModelNumber] = m1;
      //console.log(drawModelArray[drawModelNumber]);
      drawModelNumber++;
      graph.addCell(m2);
      // console.log(m2.id);
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

function Model(componentName, componentDescription, inputs, outputs, parameters){ 
  this.componentName = componentName; 
  this.componentDescription = componentDescription; 
  this.inputs = inputs; 
  this.outputs = outputs;
  this.parameters = parameters;
  this.id = '';
} 

/**
* Create the Recieved Model
*/

function createModel(){
  //alert("done");
  if ( ModelInfomation != '') {
    dealRecieveModel(ModelInfomation);
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
      var m1 = new joint.shapes.devs.Model({
        position: { x: 70, y: 70*(i+1)},
        size: { width: 50, height: 50 },
        inPorts: inPortsArray,
        outPorts: outPortsArray,
        attrs: {
            '.label': { text: recievedModelArray[i].componentName, 'ref-x': .3, 'ref-y': .45 },
            rect: { fill: '#fffff' },
            '.inPorts circle': { fill: '#16A085', magnet: 'passive', type: 'input' },
            '.outPorts circle': { fill: '#E74C3C', type: 'output' },
            text: { fill: '#676767','pointer-events': ''}
        }
      });
      recievedModelArray[i].id = m1.id;
      //alert(m1.id);
      graph1.addCell(m1);
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

    var inputArray = new Array();
    var inputNumber = 0;
    var outputArray = new Array();
    var outputNumber = 0;
    var parameterArray = new Array();
    var parameterNumber = 0;

    var componentName = $(this).find("componentName").text();
    // document.write(componentName);
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

    var m1 = new Model(componentName, componentDescription, inputArray, outputArray, parameterArray);
    recievedModelArray[recievedModelNumber] = m1;
    recievedModelNumber += 1;
  });

}



function sendXmlToServer() {
  websocket.send(componentXML);
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

function addComponent() {
  var d = new ElementWithPorts({
      position: { x: 150, y: 150 },
      size: { width: 80, height: 80 },
      attrs: {
          '.port1 text': { text: 'input1' },
          '.port2 text': { text: 'input2' },
          '.port3 text': { text: 'output3' },
          '.port1': { ref: 'rect', 'ref-y': .4 },
          '.port2': { ref: 'rect', 'ref-y': .6 },
          '.port3': { ref: 'rect', 'ref-y': .5, 'ref-dx': 0 },
      }
  });
  graph.addCell(d);
  createXMLDocument();
}

function displayAttributes(m){
  var node = '';
  if(m instanceof joint.shapes.devs.Model){
    for(var i in drawModelArray) {
      //console.log(recievedModelArray[i].id + " " + cellId);
      if(drawModelArray[i].id == m.id){
        node = drawModelArray[i];
        break;
      }
    }
    var html = '<li class="nav-header">Attributes</li>'
    html +='<li>' + node.componentDescription + '</li>'
    if (node.inputs.length != 0){
      html += '<li><a href="#">Input</a></li>';
      html += '<li><div class="input-group"';
      for (var i in node.inputs){
        html += '<span class="input-group-addon">' + node.inputs[i][0] + '</span>';
        html += '<input type="text" class="form-control" placeholder="' + node.inputs[i][1] + '"/>';
      }
      html += '</div></li>'
    }
    if (node.parameters.length != 0){
      html += '<li><a href="#">Parameter</a></li>';
      html += '<li><div class="input-group"';
      for (var i in node.parameters){
        html += '<span class="input-group-addon">' + node.parameters[i][0] + '</span>';
        html += '<input type="text" class="form-control" placeholder="' + node.parameters[i][1] + '"/>';
      }
      html += '</div></li>'
    }
    if (node.outputs.length != 0){
      html += '<li><a href="#">Output</a></li>';
      html += '<li><div class="input-group"';
      for (var i in node.outputs){
        html += '<span class="input-group-addon">' + node.outputs[i][0] + '</span>';
        html += '<input type="text" class="form-control" placeholder="' + node.outputs[i][1] + '"/>';
      }
      html += '</div></li>'
    }
    html += '<button type="button" class="btn btn-default">确定</button><button type="button" class="btn btn-default">取消</button>';
    $('#attributesArea').html(html);
  }
}




