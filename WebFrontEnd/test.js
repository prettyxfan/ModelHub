var graph = new joint.dia.Graph;
var paper = new joint.dia.Paper({
    el: $('#paper'),
    width: 650, height: 200, gridSize: 1,
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

paper.on('cell:pointerdown', function(evt, x, y){
  var evtId = "#" + evt.id;
  var cellId = $(evtId).attr("model-id");
  var m = graph.getCell(cellId);
  if(m instanceof joint.shapes.devs.Model){
    for(var i in modeArray) {
      //console.log(modeArray[i].id + " " + cellId);
      if(modeArray[i].id == cellId){
        $('#content').html(modeArray[i].componentDescription);
      }
    }
  }
});

function Model(componentName, componentDescription, inputs, outputs, parameters){ 
  this.componentName = componentName; 
  this.componentDescription = componentDescription; 
  this.inputs = inputs; 
  this.outputs = outputs;
  this.parameters = parameters;
  this.id = '';
} 

modeArray = new Array();
modeNumber = 0;
componentArray = new Array();


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
      if (inputName != '') {
        inputArray[inputNumber] = new Array(inputName, inputType);
        inputNumber++;
      }
    });

    $(this).find("outputName").each(function(){
      var outputName = $(this).text();
      var outputType = $(this).next().text();
      if (outputName != '') {
        outputArray[outputNumber] = new Array(outputName, outputType);
        outputNumber++;
      }
    });

    $(this).find("parameterName").each(function(){
      var parameterName = $(this).text();
      var parameterType = $(this).next().text();
      if (parameterName != '') {
        parameterArray[parameterNumber] = new Array(parameterName, parameterType);
        parameterNumber++;
      }
    });

    var m1 = new Model(componentName, componentDescription, inputArray, outputArray, parameterArray);
    modeArray[modeNumber] = m1;
    modeNumber += 1;
  });

}

function createModel(){
  str = "<components><component><componentName>gopher</componentName><componentDescription>It's a long long story</componentDescription><inputs><input><inputName>in1</inputName><inputType>string</inputType></input><input><inputName>in2</inputName><inputType>string</inputType></input></inputs><outputs><output><outputName>out</outputName><outputType>integer</outputType></output></outputs><parameters><parameter><parameterName></parameterName><parameterType></parameterType></parameter></parameters></component><component><componentName>Hello World</componentName><componentDescription>The component just prints out the message.</componentDescription><inputs><input><inputName>message</inputName><inputType>string</inputType></input></inputs><outputs><output><outputName>out</outputName><outputType>string</outputType></output></outputs><parameters><parameter><parameterName></parameterName><parameterType></parameterType></parameter></parameters></component></components>";
  dealRecieveModel(str);
  for(var i=0; i<modeNumber; i++ ) {
    var inPortsArray = new Array();
    for (var j=0; j<modeArray[i].inputs.length; j++) {
      inPortsArray[j] = modeArray[i].inputs[j][0];
    }
    var outPortsArray = new Array();
    for (var j=0; j<modeArray[i].outputs.length; j++) {
      outPortsArray[j] = modeArray[i].outputs[j][0];
    }
    // var outPortsArray = new Array();
    // for (var j=0; j<modeArray[i].outputs.length; j++) {
    //   outPortsArray[j] = modeArray[i].outputs[j][0];
    // }
    // console.log(modeArray[i].componentName);
    // var test = ['df','sdf'];
    var m1 = new joint.shapes.devs.Model({
      position: { x: 70, y: 70*(i+1)},
      size: { width: 50, height: 50 },
      inPorts: inPortsArray,
      outPorts: outPortsArray,
      attrs: {
          '.label': { text: modeArray[i].componentName, 'ref-x': .3, 'ref-y': .45 },
          rect: { fill: '#fffff' },
          '.inPorts circle': { fill: '#16A085', magnet: 'passive', type: 'input' },
          '.outPorts circle': { fill: '#E74C3C', type: 'output' },
          text: { fill: '#676767','pointer-events': ''}
      }
    });
    modeArray[i].id = m1.id;
    //alert(m1.id);
    // componentArray[modeArray[i].componentName] = m1;

    graph.addCell(m1);
  }
}
    // componentXML = new XMLSerializer().serializeToString(xmobj);
