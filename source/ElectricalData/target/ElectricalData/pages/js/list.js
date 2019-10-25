function listElectricalData(placeId, startDate, endDate){
	
	$('#listElectricalDataTable').show();
	
	var urlToGetData = "electricaldata/getByPlaceAndDateInterval/"+placeId+"/"+startDate+"/"+endDate;

	var fieldsArray = ['place.name','measurementDateTime','currentTotal',
		'phaseVoltageAb','phaseVoltageBc','phaseVoltageCa'];	

	genericList(urlToGetData, "listElectricalDataTable", "dataId", fieldsArray, null, null, eachElectricalDataRowListing);
}

function eachElectricalDataRowListing(data){
	data['measurementDateTime'] = convertSqlDateToFormDate(data['measurementDateTime']);
}

function listPlaces(){	
	var urlToLoad = "listPlaces.html";
	var urlToGetData = "place/";
	
	loadPage(urlToLoad, function(){
		loadDeleteConfirmModal(urlToGetData+'delete', deleteEntity, beforeDeletePlace);
		genericList(urlToGetData, "place", "placeId", ['name','meter', 'edit', 'delete'], editDeleteButton);
	});
}

function editDeleteButton(data, dataTableId, entityId){
	editButton(data, dataTableId, entityId);
	deleteButton(data, dataTableId, entityId)
}

function editButton(data, dataTableId, entityId){
	data['edit'] = '<input type="image" entity-data="'+data[entityId]+'" onclick="edit'+dataTableId+'(this);" src="assets/css/images/bt-editar.png"/>';
}

function deleteButton(data, dataTableId, entityId){
	data['delete'] = '<input type="image" entity-data="'+data[entityId]+'" data-toggle="modal" data-target="#confirmAction" src="assets/css/images/bt-deletar.png"/>';
}

function loadPage(htmlPageToLoad, loadedCallback){
	$('#contentDiv').load(htmlPageToLoad, loadedCallback);
}

function genericList(urlToGetData, dataTableId, entityId, dataTableFieldsArray, includeButtonsFunction, parameter, eachRowCallback){

	getEntity(urlToGetData, function(json)
			{		
		        $.each(json, function(i, data) {        	
		        	
		        	if(eachRowCallback){
		        		eachRowCallback(data);
		        	}
		        	
		        	if(includeButtonsFunction){
		        		includeButtonsFunction(data, dataTableId, entityId);
		        	}

					var row = '<tr id="tr'+data[entityId]+'">' +
						getTableData(data, dataTableFieldsArray) +
			  		  	'</tr>';
			        	
		        	$('#'+dataTableId).find('tbody:last').append(row);
		        	$('#tr'+data[entityId]).data('key',data);
		        });
			},{parameter});			
}