function init() {
  componentDOM= null;
  IdCount = 0;
  sendXML = '';
  componentXML = '';
  dataflowXML = '';
  focusCellId = '';

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
  	console.log(componentXML); 
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

  websocket = new WebSocket("ws://localhost:8528");
  websocket.onopen = function(evt) { 
    console.log("connected ! " + evt.data);
  }; 
  websocket.onclose = function(evt) { 
    console.log("disconncted ! " + evt.data);
    websocket.close();
  }; 
  websocket.onmessage = function(evt) { 
    console.log("get message ! " + evt.data);
  }; 
  websocket.onerror = function(evt) { 
    console.log("error ! " + evt.data); 
  }; 
}
window.addEventListener("load", init(), false);


/**
*create graph
*/

var graph = new joint.dia.Graph;
var paper = new joint.dia.Paper({
     el: $('#paper'),
    gridSize: 20,
    model: graph,
    elementView: ConstraintElementView,
    defaultLink: new joint.dia.Link({
        attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' } }
    }),
    validateConnection: function(cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
        // Prevent linking from input ports.
        if (magnetS && magnetS.getAttribute('type') === 'input') return false;
        // Prevent linking from output ports to input ports within one element.
        if (cellViewS === cellViewT) return false;
        // Prevent linking to input ports.
        return magnetT && magnetT.getAttribute('type') === 'input';
    },
    validateMagnet: function(cellView, magnet) {
        // Note that this is the default behaviour. Just showing it here for reference.
        // Disable linking interaction for magnets marked as passive (see below `.inPorts circle`).
        return magnet.getAttribute('magnet') !== 'passive';
    },
    // Enable link snapping within 75px lookup radius
    snapLinks: { radius: 75 }
});

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


var thisModel;
var ConstraintElementView = joint.dia.ElementView.extend({

    pointerdown: function(evt, x, y) {
      thisModel = graph1.getCell(this.model.id);
    },

    pointermove: function(evt, x, y) {
    },

    pointerup: function(evt, x, y) {
      var copyModel = thisModel.clone();
      copyModel.translate(x.toString(), y.toString());
      graph2.addCell(copyModel);
        // joint.dia.ElementView.prototype.pointerdown.apply(this, [evt, intersection.x, intersection.y]);
    }
    // pointermove: function(evt, x, y) {
    //     var intersection = constraint.intersectionWithLineFromCenterToPoint(g.point(x, y));
    //     joint.dia.ElementView.prototype.pointermove.apply(this, [evt, intersection.x, intersection.y]);
    // }
});

graph1 = new joint.dia.Graph;
paper1 = new joint.dia.Paper({
  el:$('#model'),
  gridSize: 20,
  elementView: ConstraintElementView,
  model: graph1
});

graph2 = new joint.dia.Graph;
paper2 = new joint.dia.Paper({
  el: $('#tool'),
  gridSize: 20,
  elementView: ConstraintElementView,
  model: graph2
});

var m1 = new joint.shapes.devs.Model({
    position: { x: 40, y: 40 },
    size: { width: 50, height: 50 },
    inPorts: ['in1','in2'],
    outPorts: ['out'],
    attrs: {
        '.label': { text: 'Model', 'ref-x': .4, 'ref-y': .2 },
        rect: { fill: '#fff' },
        '.inPorts circle': { fill: '#16A085', magnet: 'passive', type: 'input' },
        '.outPorts circle': { fill: '#E74C3C', type: 'output' }
    }
});
graph1.addCell(m1);


paper1.on('cell:pointerdblclick' ,function(evt, x, y) {
    var evtId = "#" + evt.id;
    var cellId = $(evtId).attr("model-id");
    var m = graph1.getCell(cellId);
    var m2 = m.clone();
    m2.translate(100, 0);
    m2.attr('.label/text', 'Model');
    graph.addCell(m2);
});

paper.on('cell:pointerdown' ,function(evt, x, y) {
  var evtId = "#" + evt.id;
  var cellId = $(evtId).attr("model-id");
  focusCellId = cellId;
  // var xmobj = '';
  // if (focusCellId != '') {
  //   xmobj = $.parseXML(dataflowXML);
  //   var cell = graph.getCell(focusCellId);
  //     if(cell instanceof joint.shapes.devs.Model) {
  //       var obj = $(xmobj).find("part").each(function(){
  //         var xmlAttr=$(this).attr("id");
  //           if (xmlAttr == focusCellId) {
  //             $(this).remove()
  //             deleteCell.remove();
  //             }
  //           });
  //       }
                 
  //     componentXML = new XMLSerializer().serializeToString(xmobj);
  //     console.log(componentXML);
  // }
});

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


