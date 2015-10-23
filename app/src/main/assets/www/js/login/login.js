$(document).ready(function(){
	var Login = {
		init: function(){
			this.wd = WebDroidEngine.init();
			var wd = this.wd;
			var self = this;
			if(wd.isReady()){
				if(wd.get("resume").call("isFirstLaunch",{})){
					wd.get("database").call("create",{}, true).then(function(result){
						if(result.success){
							self.start();
						}
					});
				}else{
					self.start();
				}
			}
		},
		start: function(){
			if(this.wd.get("account").call("hasAnyUser",{})){
				// Redirect to dashboard
				window.location.replace("dashboard.html");
			}else{
				this.bindLoginForm();	
			}
		},
		bindLoginForm: function(){
			var self = this;
			this.bindExistUsers();
			$("#btnLogin").on("click", function(){
				var username = $("#inputEmail").val();
				var password = $("#inputPassword").val();
				if(username == ""){
					alert("Username required !");
					return;
				}
				if(password == ""){
					alert("Password required !");
					return;
				}
				self.wd.get("account").call("authenticate", {"username": username, "password":password}, true)
					.then(function(result){
						if(result.success){
							// redirect to dashboard
							window.location.replace("dashboard.html");
						}else{
							alert("Invalid username or password ! Try again");
						}
					});
			});
		},
		bindExistUsers: function(){
			var self = this;
			this.wd.get("account").call("getAvailableUsers", {}, true).then(function(records){
				$("#user_list_container").html("");
				_.each(records, function(record){
					var item = '<div class="user-item" data-id="'+record._id+'" id="user_"'+record._id+'>';
					item += '<img src="data:image/jpeg;base64, '+record.image+'" class="user-image img-circle"/>';
					item += '<p class="name">'+record.full_name+'</p>';
					item += '<p class="email">'+record.email+'</p></div>';
					$("#user_list_container").append(item);
				});
				if(records.length == 0){
					$("#user_list_container").html("<h3 class='text-center'>No any recent users.</h3>");
				}
				
				$(".user-item").each(function(i){
					$(this).on('click', self.onUserClick);
				});
			});
		},
		onUserClick: function(e){
			var user_id = $(this).attr('data-id');
			var wd = WebDroidEngine.init();
			wd.get("account").call("requestQuickLogin", {"user_id": user_id}, true)
				.then(function(result){
					if(result.success){
						// redirect to dashboard
						window.location.replace("dashboard.html");
					}else{
						alert("Invalid username or password ! Try again");
					}
				});
			}
	};
	Login.init();
});
