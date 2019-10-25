window.onload = function() {
	
	$('#sidebarDiv').load("sidebar.html", function(){
		// Sidebar Load Event
		assignEvents();
		assignAjaxEvents();
		isLogged();
	});	
	
	$('#header').load("header.html", function(){
		// Header Load Event
		loadExitConfirmModal();
	});	
	
	$('#footer').load("footer.html", function(){
		// Footer Load Event
	});	
};

function assignAjaxEvents(){	
	$('.overlay').hide();
	
	$(document).bind("ajaxSend", function(){
		$('.overlay').show();
	}).bind("ajaxComplete", function(){
		$('.overlay').hide();
	});
}

function assignEvents(){
	
	$('#registerPlaceLink').click(function(){loadPlace(registerPlace);});
	
	$('#listPlaceLink').click(function(){listPlaces();});
	$('#listElectricalDataLink').click(function(){loadElectricalData(listElectricalData);});
	
	$('#uploadElectricalDataLink').click(function(){loadUploadElectricalDataLink(registerElectricalData);});	

	$('#graphicsElectricalDataLink').click(function(){loadElectricalData(graphicsElectricalData, null, true);});
}