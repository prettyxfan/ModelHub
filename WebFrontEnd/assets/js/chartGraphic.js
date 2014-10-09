
function line(x, y1, y2) {
    chartData = {
        labels : x,
        datasets : [{
            label: "My First dataset",
            fillColor : "rgba(220,220,220,0.2)",
            strokeColor : "rgba(220,220,220,1)",
            pointColor : "rgba(220,220,220,1)",
            pointStrokeColor : "#fff",
            pointHighlightFill : "#fff",
            pointHighlightStroke : "rgba(220,220,220,1)",
            data : y1
        },{
            label: "My Second dataset",
            fillColor : "rgba(151,187,205,0.2)",
            strokeColor : "rgba(151,187,205,1)",
            pointColor : "rgba(151,187,205,1)",
            pointStrokeColor : "#fff",
            pointHighlightFill : "#fff",
            pointHighlightStroke : "rgba(151,187,205,1)",
            data : y2
        }
        ]
    }

    window.graph = new Chart(analysisCanvas).Line(chartData, {
        responsive: false
    });
}

function bar(x, y1, y2) {
    chartData = {
        labels : x,
        datasets : [{
            fillColor : "rgba(220,220,220,0.5)",
            strokeColor : "rgba(220,220,220,0.8)",
            highlightFill: "rgba(220,220,220,0.75)",
            highlightStroke: "rgba(220,220,220,1)",
            data : y1
        },{
            fillColor : "rgba(151,187,205,0.5)",
            strokeColor : "rgba(151,187,205,0.8)",
            highlightFill : "rgba(151,187,205,0.75)",
            highlightStroke : "rgba(151,187,205,1)",
            data : y2
        }
        ]
    }

    window.graph = new Chart(analysisCanvas).Bar(chartData, {
        responsive: false
    });
}

window.onload = function() {
//    ctx = document.getElementById("canvas").getContext("2d");
    analysisCanvas = document.getElementById("analysisCanvas").getContext("2d");
}