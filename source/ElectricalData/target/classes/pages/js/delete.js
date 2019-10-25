function beforeDeletePlace(entity){
	// Alterar a entidade, se necessário.
}

function deleteEntity(urlToGetData, entity, entityId, beforeDeleteEntityCallback){	

	delete entity['edit'];
	delete entity['delete'];
	
	if(beforeDeleteEntityCallback){
		beforeDeleteEntityCallback(entity);
	}
	
	$.ajax({
		   type: "post",
		   dataType: "json",
		   url: getServerUrl() + urlToGetData,
		   data: JSON.stringify( entity ),
		   processData: true,
		   contentType: 'application/json',
		   success: function(data){
			   showMessage(data['message'],'success');
			   $('#tr'+entityId).hide();
			   window.scrollTo(0, 0);
		   },
		   error: function(jqXHR, exception) {
				var msg = jqXHR.responseText;
				showMessage(msg,'error');
				window.scrollTo(0, 0);
		   }
		});
 }