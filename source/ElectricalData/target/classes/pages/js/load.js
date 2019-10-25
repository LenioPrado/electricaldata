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

function loadElectricalData(actionToExecute, isGraphic){	
	
	$('#contentDiv').load("listElectricalData.html", function(){
		
		if(isGraphic){
			$('#sectionInfo').empty();
			$('#sectionInfo').html('Gráficos de Dados Elétricos');		
			$('#listElectricalDataButton').val('Gerar');
		}
		
		$('#listElectricalDataTable').hide();
		
		$('#noEndDate').click(function(){
			var endDate = $('#measurementEndDate');
			
			if($(this).prop("checked")){
				endDate.prop('disabled', true);
			}else{
				endDate.prop('disabled', false);				
			}
		})
		
		$('#listElectricalDataButton').click(function(){			
			validateElectricalData(function(form){
				
				var urlToGetData = getUrl();
				if(automaticUpdate()){
					setTimer(actionToExecute);
				}else{
					clearTimer();
				}
				actionToExecute(urlToGetData, true);
			});
		});
		
		clearTimer();	
	});	
	getElectricalData();		
}

function setTimer(actionToExecute){
	var timerId = setInterval(function(){
		var urlToGetData = getUrl();
		actionToExecute(urlToGetData);	
	}, 10000);
	saveTimer(timerId);
}

function getUrl(){
	var placeId = $('#place').val();
	var startDate = $('#measurementStartDate').val();
	var urlToGetData = "electricaldata/getByPlaceAndDateInterval/"+placeId+"/"+startDate;				

	if(!$('#noEndDate').prop("checked")){
		var endDate = $('#measurementEndDate').val();
		urlToGetData += "?endDate=" + endDate;
	}
	
	var lastDataId = getDataIdStored();	
	if(automaticUpdate() && lastDataId){
		if(urlToGetData.indexOf("?")<0){ //Is the first parameter
			urlToGetData += "?";
		} else{ //Has more parameters 
			urlToGetData += "&";
		}
		urlToGetData += "dataId="+lastDataId;
	} 

	return urlToGetData;
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