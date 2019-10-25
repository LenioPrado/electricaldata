function validatePlace(callback){
	  $("form[name='registerPlace']").validate({
		  rules: {
			  name: "required",
			  meter: "required"
		  },
		
		  messages: {
			  name: "Digite um nome válido",
			  meter: "Digite um medidor válido"
		  },
		  errorClass: "registerError",
		  validClass: "registerSuccess",
		
		  submitHandler: function(form) {
			  callback(form);					
		  }
	  });
}

function validateElectricalData(callback){
	  $("form[name='listElectricalData']").validate({
		  rules: {
			  place: "required",
			  measurementStartDate: "required",
			  measurementEndDate: "required"
		  },
		
		  messages: {
			  place: "Selecione um local",
			  measurementStartDate: "Selecione uma data inicial",
			  measurementEndDate: "Selecione uma data final"
		  },
		  errorClass: "registerError",
		  validClass: "registerSuccess",
		
		  submitHandler: function(form) {
			  callback(form);					
		  }
	  });
}

function validateUploadElectricalData(callback){
	  $("form[name='registerElectricalData']").validate({
		  rules: {
			  places: "required",
			  fileName: "required"
		  },
		
		  messages: {
			  places: "Selecione um local válido",
			  fileName: "Insira um arquivo válido",
		  },
		  errorClass: "registerError",
		  validClass: "registerSuccess",
		
		  submitHandler: function(form) {
			  callback(form);					
		  }
	  });
}

function validateLogin(callback){
    console.log("entrou no validationLogin");
	$("form[name='loginForm']").validate({
	    rules: {
	        userEmail: {
	            required: true,
	            email: true
	            },
	        userPassword: {
	            required: true,
	            minlength: 5
	        },
	    },
	    messages: {
	        userPassword: {
	            required: "Digite uma senha válida",
	            minlength: "Sua senha deve ter no mínimo 5 caracteres"
	        },
	        userEmail:  "Digite um email válido",
	    },
	    submitHandler: function(form) {
	    	callback(form);
	    },
        errorClass: "registerError",
        validClass: "registerSuccess"
	
	  });
}

function validateUserRegister(callback) {
	$("form[name='registerForm']").validate({
		rules: {
		name: "required",
		email: {
		    required: true,
		    email: true
		},
		password: {
		    required: true,
		    minlength: 5
		}
	},	
	messages: {
		name: "Digite um nome válido",	  	
		password: {
			required: "Digite uma senha válida",
			minlength: "Sua senha deve ter no mínimo 5 caracteres"
		},
		email:  "Digite um email válido"
	},
	errorClass: "registerError",
	validClass: "registerSuccess",
	
	submitHandler: function(form) {
		callback(form);
	}
  });
};