var dashboard = false;
$(document).ready(function(){
	var Dashboard = {
		childProp: false,
		wd : WebDroidEngine.init(),
		init: function(){
			if(this.wd.isReady()){
				this.user = this.wd.get("account").call("getUser");
				if(this.user != false){
					this.bindUser(this.user);
					this.start();
				}
			}
		},
		extend: function(params){
			this.childProp = true;
			this.init();
			var subClass =  _.extendOwn(this, params);
			subClass.bindBackNavigation();

			return subClass;
		},
		bindBackNavigation: function(){
			console.info("Binding back navigation for page");			
			$(".appbar-back").on("click", function(){
				window.history.back();
			});
		},
		bindUser: function(user){
			$("#user_name").html(user.full_name);
		},
		start: function(){
			var self = this;
			$("#btnLogout").on("click", function(){
				self.logout(self.wd);
			});
			if(!this.childProp){
				this.bindActions();
			}
		},
		logout: function(wd){
			wd.get("notify").call("showConfirm", 
				{"message": "Are you sure want to logout ?"}, true)
			.then(function(result){
				if(result.key == "yes"){
					wd.get("account").call("logout", {}, true).then(function(){
						wd.get("notify").call("showToast", {"message": "Logged out successful !"});
						window.location.replace("login.html");
					});
				}
			});
		},
		bindActions: function(){
			var self = this;
			$("#user_profile").on("click", function(){
				window.location.href = "full_profile.html";
			});
			$("#download_pdf").on("click", function(){
				self.wd.get("resume").call("download",{"user_id": self.user._id});
			});
			
			$("#personal_detail").on("click", function(){
				window.location.href = "personal_details.html";
			});
			$("#courses").on("click", function(){
				window.location.href = "courses.html";
			});
			$("#work").on("click", function(){
				window.location.href = "company.html";
			});
			$("#achivements").on("click", function(){
				window.location.href = "achivements.html";
			});
			$("#projects").on("click", function(){
				window.location.href = "project_registration.html";
			});
			$("#skills").on("click", function(){
				window.location.href = "skills.html";
			});
			$("#languages").on("click", function(){
				window.location.href = "about_languages.html";
			});
			$("#additional").on("click", function(){
				window.location.href = "additional.html";
			});
		}
	};
	Dashboard.init();
	dashboard = Dashboard;
});