window.onload = function() {
	
	$('#sidebarDiv').click(function(){
		clearTimer();
	});
	
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

function assignEvents(){
	
	$('#registerPlaceLink').click(function(){loadPlace(registerPlace);});
	
	$('#listPlaceLink').click(function(){listPlaces();});
	$('#listElectricalDataLink').click(function(){loadElectricalData(listElectricalData);});
	
	$('#uploadElectricalDataLink').click(function(){loadUploadElectricalDataLink(registerElectricalData);});	

	$('#graphicsElectricalDataLink').click(function(){loadElectricalData(graphicsElectricalData, true);});
}