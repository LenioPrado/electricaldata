window.onload = function() {
	assignEvents();
};

function assignEvents(){	
	$('#loginSubmit').click(function(){
		validateLogin(function(form){
			checkUser();
		});
	});
	
	$('#registerUser').click(function(){
		validateUserRegister(function(form){
			registerUser();
		});
	});	
}

function registerUser()
{
	var user = getFormData('registerForm');
	
	console.log(user);

	$.ajax({
	   type: "post",
	   dataType: "json",
	   url: getServerUrl() + "user/create",
	   data: JSON.stringify( user ),
	   processData: true,
	   contentType: 'application/json',
	   success: function(data){
		   showIndexMessage('messagesRegister', 'Usuário cadastrado com sucesso!','success');
	   },
	   error: function(jqXHR, status, error) {
		   var msg = jqXHR.responseText;	
		   showIndexMessage('messagesRegister', msg,'error');
	   }
	});    	
};
  
function checkUser(){
	var data = getFormData('loginForm');
	var url = "user/login" + "/" + data['userEmail'] + "/" + data['userPassword'] 
	$.ajax({
		   type: "get",
		   dataType: "json",
		   url: getServerUrl() + url,
		   processData: true,
		   contentType: 'application/json',
		   success: function(data){
			   if(data['message']){
				   showIndexMessage('messagesLogin', data['message'],'error');
			   } else{
				   // Fazer algo com o nome do usuário!
				   hideMessage();
				   window.location.replace("mainUser.html");
			   }			   
		   },
		   error: function(jqXHR, exception) {
				if(jqXHR.responseText['message']){
					showIndexMessage('messagesLogin', data['message'],'error');
				} else {
					var msg = jqXHR.responseText;
					showIndexMessage('messagesLogin', msg,'error');
				}				
		   }
	});
}