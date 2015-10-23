var wd = WebDroidEngine.init();

var WebDroidTest = {
	start: function(){
		var self = this;
		this.log("Starting WebDroid Test");
		if(wd.isReady()){
			this.log("WebDroid layer available");
			this.getPlugins();
			wd.get("notify").call("showToast", {"message": "Showing toast"});
			setTimeout(function(){
				self.log("Showing toast.");
				setTimeout(function(){
					self.getDeviceInfo();
					self.log("Testing callback method");
					wd.get("notify")
						.call("showAlert", {"message":"Simple alert with OK button"}, true)
						.then(function(result){
							if(result){
								self.log("User pressed OK Button");
								
								wd.get("notify").call("showConfirm", {"message":"Is WebDroid working ?"}, true).then(function(result){
									self.log("Your choice : " + result.key);

									self.log("TEST DONE SUCCESS");
								});
							}
						});

				}, 1500);
			}, 1000);
		}else{
			this.log("WebDroid layer not available", true);
		}
	},
	getDeviceInfo: function(){
		var self = this;
		this.log("Getting device information");
		var deviceInfo = wd.get("device").call("getDeviceInformation", {});
		var keys = _.keys(deviceInfo);
		_.each(keys, function(key){
			self.log("  " + key + " => " + deviceInfo[key]);
		});
	},

	getPlugins: function(){
		var self = this;
		self.log("Getting all plugins and its methods: \n")
		_.each(wd.getPlugins(), function(plugin){
			var str = plugin.name;
			_.each(plugin.methods, function(method){
				str += "\n\t - " + method;
			});
			self.log(str);	
		});
	},
	log: function(logMessage, failed){
		failed = !(typeof failed == 'undefined');
		var clr = (!failed) ? "#388E3C" : "#D50000"; 
		$("#js-test-log").append("<b><span style='color:" + clr + "'>- " +logMessage + "</span></b>\n");
	}
};