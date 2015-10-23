var WebDroidCallbacks = {
	callBacks: {},
	register: function(callbackId, successCallback, failCallback){
		this.callBacks[callbackId] = {
			"successCallback" : successCallback,
			"failCallback": failCallback,
			"id": callbackId
		};
	},
	invoke: function(callbackId, data){
		var self = WebDroidCallbacks;
		if(self.callBacks.hasOwnProperty(callbackId)){
			if(data.success){
				self.callBacks[callbackId].successCallback(data.result);
			}else{
				self.callBacks[callbackId].failCallback(data.result);
			}
		}

	}
};

var WebDroidEngine = {
	plugin: false,
	init: function() {
		this.isLoaded = typeof WebDroid != 'undefined';
		return this;
	},
	get: function(plugin){
		this.plugin = plugin;
		return this;
	},
	isReady: function (){
		return this.isLoaded;
	},
	call: function(method, args, deffered){
		if(!this.plugin){
			return {"error": "Plugin not defined. Add plugin name in get() method."};
		}
		var deffered = (typeof deffered != 'undefined') ? deffered : false;
		var action = this.plugin + "." + method;
		args = (typeof args == 'undefined') ? {} : args;
		if(deffered){
			return this.deffered_action(action, args);
		}else{
			return this.simple_action(action, args);
		}

	},
	deffered_action: function(action, args){
		if(this.isLoaded){
			var def = $.Deferred();
			var successCallback = function(data){
				def.resolve(data);
			}
			var failCallback = function(data){
				def.reject(data);
			}
			var callbackId = "_wd_" + new Date().getTime();
			WebDroidCallbacks.register(callbackId, successCallback, failCallback);
			WebDroid.execute(action, JSON.stringify(args), callbackId);
			return def;
		}else{
			return $.Deferred().reject({"error": "WebDroid service available only on mobile."});
		}
	},
	simple_action: function(action, args){
		if(this.isLoaded){
			return JSON.parse(WebDroid.execute(action, JSON.stringify(args), null));
		}else{
			return {"error": "WebDroid service available only on mobile."};
		}
	},
	getPlugins: function(){
		if(this.isLoaded){
			return JSON.parse(WebDroid.getPlugins());
		}
		return false;
	}
};