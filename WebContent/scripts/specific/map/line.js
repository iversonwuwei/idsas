/***
*divid 
*mapDate=[[,],[,]]
*/
function buildLineMap(divid,mapDate) {
	var distination = $("#"+divid);
	
    var options = {
        series: { 
            lines: { show: true, barWidth: 0.66, align: "center", horizontal: false}
        },	
        xaxis: {
        	mode: "categories",
        	autoscaleMargin: 0.02,
        	tickLength: 0
        }
    };
    var plot = $.plot(distination, [mapDate], options);	
}