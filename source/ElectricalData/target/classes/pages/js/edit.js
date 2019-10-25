function editplace(input){
	var entityData = {
			'formId':'registerPlace',
			'loadFunction': loadPlace,
			'submitInputId':'submitPlace',
			'url':'place/edit',
			'pageTitle':'Edição de Locais',
			'successMessage':'Local alterado com sucesso!'
		};
	changeEntityPageSettings(input, entityData);
}

function changeEntityPageSettings(input, entityRelatedData, afterGetEntityCallback){
	var entityId = $(input).attr('entity-data');
	var entity = $('#tr'+entityId).data('key');
	
	entityRelatedData['loadFunction'](
			function(){				
				editEntity(entity, entityRelatedData, afterGetEntityCallback)
			}, 
			function(){
				$('#pageTitle').text(entityRelatedData['pageTitle']);
				$('#'+entityRelatedData['submitInputId']).val('Atualizar');
				
				$.each($(".notEditable"), function(i, input){
					$(input).hide();
				});	
				
				setFormData(entity);
				if(entityRelatedData['select-multiple-id']){
					setSelectMultipleValues(entity, entityRelatedData);
				}
			}
	);
}

function editEntity(oldEntity, entityRelatedData, afterGetEntityCallback){
	var entity = getFormData(entityRelatedData['formId']);
	
	$.each(oldEntity, function(field, value){
		if(!(field in entity)){
			entity[field] = value;
		}
	});
	
	delete entity['edit'];
	delete entity['delete'];
	
	if(afterGetEntityCallback){
		afterGetEntityCallback(entity);
	}
	
	$.ajax({
	   type: "post",
	   dataType: "json",
	   url: getServerUrl() + entityRelatedData['url'],
	   data: JSON.stringify( entity ),
	   processData: false,
	   contentType: 'application/json',
	   success: function(data){
		   showMessage(entityRelatedData['successMessage'],'success');
		   window.scrollTo(0, 0);
	   },
	   error: function(jqXHR, exception) {
			var msg = jqXHR.responseText;
			showMessage(msg,'error');
			window.scrollTo(0, 0);
	   }
	});	
}