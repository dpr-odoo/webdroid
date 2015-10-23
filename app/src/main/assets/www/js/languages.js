$(document).ready(function(){
	var Languages = dashboard.extend({
		db: WDDatabase.pool("user_languages"),
		init: function(){
			this.bindData();
			var self = this;
			$("#btnSave").on('click', function(){
				self.createLanguage();
			});
		},
		bindData: function(){
			// resetting form
			$("#inputLanguageName").val("");
			var self = this;
			// Get all Languages of user
			this.db.select().then(function(rows){
				var items = "";
				_.each(rows, function(row){
					var item = '<tr><td>';
		  				item += '<div class="row course-row"><div class="col-md-12">';
		  				item += '<h2  class="content-title inline-content">'+row.lang_name+'</h2>';
			  			item += '<a href="javascript:void(0);" data-id="'+row._id+'" class="lang_remove text-danger pdl16 pull-right"><span class="fa fa-remove fa-2x text-danger"></span> Remove</a>';
			  			item += '</div></div></td></tr>';
			  		items += item + "\n";
				});
				$("#table_body").html(items);
				$(".lang_remove").each(function(i){
					$(this).on("click", function(){
						var id = $(this).attr("data-id");
						self.deleteLanguage(id);
					});
				});
			});
		},

		createLanguage: function(){
			var self = this;
			if(this.validForm()){
				this.db.create(this.getFormData()).then(function(newId){
					if(newId>0){
						self.wd.get("notify").call("showToast",{"message":"Language added"});
						self.bindData();
					}	
				});
			}
		},
		validForm: function(){
			var name = $("#inputLanguageName").val();
			if(name == ""){
				this.notify("Provide Language name");
				return false;
			}
			return true;
		},
		notify: function(msg){
			this.wd.get("notify").call("showToast", {"message": msg});
		},
		deleteLanguage: function(lang_id){
			var self = this;
			this.wd.get('notify').call("showConfirm", {"message": "Are you sure want to delete ?"}, true)
			.then(function(res){
				if(res.key == "yes"){
					self.db.delete([["_id", "=", lang_id]]).then(function(res){
						if(res > 0){
							self.notify("Language deleted !");
							self.bindData();
						}
					});
				}
			});
		},
		getFormData: function(){
			return {
				'lang_name': $("#inputLanguageName").val(),
				'user_id': this.db.user_id
			};
		}
	});

	Languages.init();
});
