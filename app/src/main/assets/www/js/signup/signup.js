$(document).ready(function(){
	var SignUp = {
		init: function(){
			this.wd = WebDroidEngine.init();
			this.start();
		},
		start: function(){
			this.bindSignUpForm();
		},
		bindSignUpForm: function(){
			var self = this;
			$("#btnSignup").on("click", function(){
				var full_name = $("#inputName").val();
				var username = $("#inputEmail").val();
				var password = $("#inputPassword").val();
				var passwordConfirm = $("#inputPasswordConfirm").val();
				if(full_name == ""){
					alert("Your name required !");
					return;
				}
				if(username == ""){
					alert("Username required !");
					return;
				}
				if(password == ""){
					alert("Password required !");
					return;
				}
				if(passwordConfirm == ""){
					alert("Confirm password required !");
					return;
				}
				if(password != passwordConfirm){
					alert("Password does not match !");
					return;
				}
				var data = {
					"full_name": full_name,
					"username": username,
					"password" : password
				};
				self.wd.get("account").call("signup", data, true)
					.then(function(result){
						if(result.success){
							// redirect to dashboard
							window.location.replace("dashboard.html");
						}
					});
			});
		}
	};
	SignUp.init();
});
