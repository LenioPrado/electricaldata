function graphicsElectricalData(placeId, startDate, endDate){	
	google.charts.setOnLoadCallback(function(){loadChart(placeId, startDate, endDate)});
}

function loadChart(placeId, startDate, endDate){
	var urlToGetData = "electricaldata/getByPlaceAndDateInterval/"+placeId+"/"+startDate+"/"+endDate;
	
	getEntity(urlToGetData, function(json)
	{
		prepareData(json);		
	});	
}

function prepareData(json){

	if(json && json.length > 0){
		//var columnsSize = Object.keys(json[0]).length;
		var columnsToIgnore = ['place','dataId','measurementDateTime'];
		var columnXAxis = 'measurementDateTime';
		var fieldDataArray = [];
		var date = new Date(json[0][columnXAxis]);
		var dateMinMax = [date,date];
		
		//Cria um array com os nomes dos campos.
		$.each(json[0], function(columnName) {    		
			if($.inArray(columnName, columnsToIgnore) == -1){
				fieldDataArray.push([columnName, []]);
    		}
    	});		
	    
		//Adiciona os valores de cada campo para o respectivo array.
	    $.each(json, function(i, rowData) {	    	
	    	$.each(rowData, function(columnName, value) {
	    		var array = fieldDataArray.find(x => x[0] === columnName);
	    		if(array){
	    			var date = new Date(rowData[columnXAxis]);
	    			compareDates(dateMinMax, date);
	    			array[1].push([date,rowData[columnName]]);	    			
	    		}
	    	});	    		
	    });	
	    
	    drawCharts(fieldDataArray, columnXAxis, dateMinMax);
	}
}

function compareDates(dateMinMax, date){
	
	if(date < dateMinMax[0]){
		dateMinMax[0] = date;
	}
	
	if(date > dateMinMax[1]){
		dateMinMax[1] = date;
	}	
}

function drawCharts(fieldDataArray, columnXAxis, dateMinMax) {
	
	$('#graphics').empty();
	
    $.each(fieldDataArray, function(index, fieldData) {
    	var data = new google.visualization.DataTable();
        data.addColumn('datetime', columnXAxis);
        data.addColumn('number', fieldData[0]);	
        data.addRows(fieldData[1]);        
        
    	var chart = new google.visualization.LineChart(getElementToInsert(index));
        var options = {
        		curveType: 'function',
        		//legend: { position: 'bottom' },
        		legend: 'none',
        		colors: ['#a52714'],
        		crosshair: {
        	          color: '#000',
        	          trigger: 'selection'
        	        },
                hAxis: {
                    title: 'Dia/Hora',                    
                    viewWindow: {
                        min: dateMinMax[0],
                        max: dateMinMax[1]
                      },
                      gridlines: {
                        count: -1,
                        units: {
                          days: {format: ['MMM dd']},
                          hours: {format: ['HH:mm', 'ha']},
                        }
                      },
                      minorGridlines: {
                        units: {
                          hours: {format: ['hh:mm:ss a', 'ha']},
                          minutes: {format: ['HH:mm a Z', ':mm']}
                        }
                      }
                  },
        		vAxis: {
        			title: fieldData[0],
        		}
        };	
    	chart.draw(data, options);
    });	
}

function getElementToInsert(index){
	var element = $('#graphics');
	var div = $('<div id="graphic'+index+'"></div>');
	element.append(div);
	return div[0];
}