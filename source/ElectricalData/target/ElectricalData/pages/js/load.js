function loadPlace(actionToExecute, loadedCallback){
	
	$('#contentDiv').load("registerPlace.html", function(){
		$('#submitPlace').click(function(){
			validatePlace(function(form){
				actionToExecute();
			});
		});
		
		if(loadedCallback){
			loadedCallback();
		}
	});
}

function loadElectricalData(actionToExecute, loadedCallback, isGraphic){	
	
	$('#contentDiv').load("listElectricalData.html", function(){
		
		if(isGraphic){
			$('#sectionInfo').empty();
			$('#sectionInfo').html('Gráficos de Dados Elétricos');		
			$('#listElectricalDataButton').val('Gerar');
		}
		
		$('#listElectricalDataTable').hide();
		
		$('#listElectricalDataButton').click(function(){			
			validateElectricalData(function(form){
				var place = $('#place').val();
				var startDate = $('#measurementStartDate').val();
				var endDate = $('#measurementEndDate').val();
				actionToExecute(place, startDate, endDate);				
			});
		});
	});	
	getElectricalData(loadedCallback);		
}

function getElectricalData(loadedCallback){
	getEntity('electricaldata/getPlacesMeasurements', function(json)
	{
		var places = [];
		
	    $.each(json, function(i, rowData) {	    	
    		var array = places.find(x => x['place_id'] === rowData['place_id']);
    		if(!array){
    			places.push(rowData);
    		}   		
	    });		
		
		fillSelect(places, 'place', 'place_id','name');
		
		var selectedPlace = $('#place').val();
		if(selectedPlace){
			placeSelectChanged(selectedPlace, json);
		}
		
		$("#place").change(function() {
			placeSelectChanged($(this).val(), json);
		});
		
		if(loadedCallback){
			loadedCallback();
		}
		$('.selectpicker').selectpicker({
			size: 4
		});
		$('.filter-option pull-left').text('Selecione');
	});		
}

function placeSelectChanged(placeId, json){
	var dates = [];
	var objects = [];
	
	$.each(json, function(index, value){
		if(value && value['place_id'] == placeId){			
			var date = convertSqlDateTimeToSqlDate(value['measurement_date_time']);
			value['measurement_date_time'] = date;
			if($.inArray(date, dates) == -1){
				dates.push(date);
				objects.push(value);
    		}			
		}
	});
	$('#measurementStartDate').empty();
	$('#measurementEndDate').empty();
	fillSelect(objects, 'measurementStartDate', 'measurement_date_time','measurement_date_time');
	fillSelect(objects, 'measurementEndDate', 'measurement_date_time','measurement_date_time');
}

function loadUploadElectricalDataLink(actionToExecute, loadedCallback){
	
	$('#contentDiv').load("uploadElectricalData.html", function(){
		$('#submitElectricalData').click(function(){
			validateUploadElectricalData(function(form){
				actionToExecute();
			});
		});
		
		getEntity('place', function(json)
		{		
			fillSelect(json, 'place', 'placeId','name');
			if(loadedCallback){
				loadedCallback();
			}
			$('.selectpicker').selectpicker({
				size: 4
			});
			$('.filter-option pull-left').text('Selecione');
		});		
	});
}