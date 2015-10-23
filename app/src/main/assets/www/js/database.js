var WDDatabase = WebDroidEngine.extend({
	table: false,
	operation: "select",
	data: {},
	user_id: -1,
	user: false,
	domain: [],
	instance: false,
	pool: function(table_name){
		this.table = table_name;
		this.user = this.get("account").call("getUser");
		if(this.user != false){
			this.user_id = this.user._id;
		}
		return this;
	},
	select: function(){
		this.operation = "select";
		return this._call();
	},
	browse: function(domain){
		this.operation = "browse";
		this.domain = domain;
		return this._call();
	},
	create: function(values){
		this.operation = "create";
		this.data = values;
		return this._call();
	},
	update: function(values, domain){
		this.operation = "update";
		this.domain = domain;
		this.data = values;
		return this._call();
	},
	delete: function(domain){
		this.operation = "delete";
		this.domain = domain;
		return this._call();
	},
	count: function(domain){
		this.operation = "count";
		this.domain = domain;
		return this._call();
	},
	_call: function(){
		this.instance = this.get("database");
		return this.instance.call("perform", this.getParams(), true);
	},
	getParams: function(){
		return {
			"model": this.table,
			"operation": this.operation,
			"user_id": this.user_id,
			"data":this.data,
			"domain": this.domain
		};
	}
});