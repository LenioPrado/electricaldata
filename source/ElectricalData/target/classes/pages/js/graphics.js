var chartsData = new function() {
    this.columnXAxis = 'measurementDateTime';
    this.columnsToIgnore = ['place','dataId','measurementDateTime'];
    this.dataArray = [];
    this.dateMinMax;
    this.isFirstTime = true;
    
    this.init = function(){
    	 this.dataArray = [];
    	 this.dateMinMax;
    	 this.isFirstTime = true;
    };
    this.compareDates = function(date){
    	
    	if(date < this.dateMinMax[0]){
    		this.dateMinMax[0] = date;
    	}
    	
    	if(date > this.dateMinMax[1]){
    		this.dateMinMax[1] = date;
    	}
    	return;
    };
    this.loadDataWithColumns = function(json){   		
    	var date = new Date(json[0][this.columnXAxis]);
    	this.dateMinMax = [date,date];
    	
    	//Cria um array com os nomes dos campos.
    	$.each(json[0], function(columnName) {    		
    		if($.inArray(columnName, chartsData.columnsToIgnore) == -1){
    			chartsData.dataArray.push([columnName, []]);
    		}
    	});
    	
    	this.isFirstTime = false;
    };
    this.loadColumnsWithValues = function(json){
    	
    	//Adiciona os valores de cada campo para o respectivo array.
        $.each(json, function(i, rowData) {	    	
        	$.each(rowData, function(columnName, value) {
        		var array = chartsData.dataArray.find(x => x[0] === columnName);
        		if(array){
        			var date = new Date(rowData[chartsData.columnXAxis]);
        			date.setTime(date.getTime() + date.getTimezoneOffset()*60*1000);
        			chartsData.compareDates(date);
        			array[1].push([date,rowData[columnName]]);	    			
        		}
        	});	    		
        });		
    };   
}

function graphicsElectricalData(urlToGetData, isReload){
	if(isReload){
		chartsData.init();	
	}
	
	google.charts.setOnLoadCallback(function(){loadChart(urlToGetData)});
}

function loadChart(urlToGetData){
	getEntity(urlToGetData, function(json)
	{
		if(json && json.length > 0){
			if(chartsData.isFirstTime){
				chartsData.loadDataWithColumns(json);
			} 
			chartsData.loadColumnsWithValues(json);
			drawCharts();

			if(automaticUpdate()){
				setLastDataLoadedId(json, urlToGetData, 'listElectricalDataTable');
			}
		}
	});	
}

function drawCharts() {
	
	$('#graphics').empty();
	
	data = chartsData.dataArray;
	
    $.each(data, function(index, fieldData) {
    	var data = new google.visualization.DataTable();
        data.addColumn('datetime', chartsData.columnXAxis);
        data.addColumn('number', fieldData[0]);	
        data.addRows(fieldData[1]);        
        
    	var chart = new google.visualization.LineChart(getElementToInsert(index));
    	var options = getOptions(index);
    	chart.draw(data, options);
    });
}

function getElementToInsert(index){
	var element = $('#graphics');
	var div = $('<div id="graphic'+index+'"></div>');
	element.append(div);
	return div[0];
}

function getOptions(index){
    var options = {
    		curveType: 'function',
    		legend: 'none',
    		colors: ['#a52714'],
    		crosshair: {
    	          color: '#000',
    	          trigger: 'selection'
    	        },
            hAxis: {
                title: 'Dia/Hora',                    
                viewWindow: {
                    min: chartsData.dateMinMax[0],
                    max: chartsData.dateMinMax[1]
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
    			title: chartsData.dataArray[index][0],
    		}
    }; 	
	return options;
}