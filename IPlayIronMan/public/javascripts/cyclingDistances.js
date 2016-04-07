$(document).ready(function() {
    google.charts.load('current', {'packages':['corechart']});
    $.get( "/iamironman/data/cyclingts", function( data ) {
    var jsonData = JSON.parse(data);
    jsonData.unshift(["Date","Distance"]);
    console.log(jsonData);
     drawChart(jsonData);
    });
});

function drawChart(dataArr) {

    // Create the data table.
    var data = google.visualization.arrayToDataTable(dataArr)

    // Set chart options
    var options = {
         title: 'Cycling times..',
         curveType: 'function',
         legend: { position: 'bottom' }
         };

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.LineChart(document.getElementById('chartArea'));
    chart.draw(data, options);
}
