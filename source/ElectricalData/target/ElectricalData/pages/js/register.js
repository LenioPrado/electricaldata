function registerPlace(){
	registerEntity('registerPlace', 'place/create', 'Local cadastrado com sucesso.');	
}

function registerElectricalData(){	
	var entity = getFormData('registerElectricalData');

	entity['place'] = { 'placeId' : entity['place'] };

	delete entity['fileName'];
	
	var file = $('#fileName').get(0).files[0];
	var cleanFileName = cleanString(file.name);
	
	var formData = new FormData();
	formData.append('uploadfile', file, cleanFileName);
	formData.append('entity', JSON.stringify( entity ));
	 
	var bar = $('.bar');
	var percent = $('.percent');
	var status = $('#status');
	
	$.ajax({
		url: getServerUrl() + "file/upload",
		type: 'POST',
		xhr: function() {
			var myXhr = $.ajaxSettings.xhr();
		    if (myXhr.upload) {
		        myXhr.upload.addEventListener('progress', function (evt) {
		        	var percentComplete = Math.ceil((evt.loaded / evt.total) * 100);
			        var percentVal = percentComplete + '%';
			        bar.width(percentVal)
			        percent.html(percentVal);
		            
		        }, false);
		    }
		    return myXhr;
		},
		success: function(data) {
			showMessage('Dados Elétricos importados com sucesso.','success');
		},
		error: function(jqXHR, exception) {
			var msg = jqXHR.responseText;
			showMessage(msg,'error');
		},
		data: formData,
		cache: false,
		contentType: false,
		processData: false
	});
}

function registerEntity(formName, urlToRegister, successMessage){
	var entity = getFormData(formName);
	
	$.ajax({
	   type: "post",
	   dataType: "json",
	   url: getServerUrl() + urlToRegister,
	   data: JSON.stringify( entity ),
	   processData: false,
	   contentType: 'application/json',
	   success: function(data){
		   showMessage('Registro criado com sucesso!','success');
	   },
	   error: function(jqXHR, exception) {
			var msg = jqXHR.responseText;
			showMessage(msg,'error');
	   }
	});	
}