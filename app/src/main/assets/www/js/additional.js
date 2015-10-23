$(document).ready(function(){
	var Additional = dashboard.extend({
		db: WDDatabase.pool("user_additional_interest"),
		init: function(){
			this.bindData();
			var self = this;
			$("#btnSave").on('click', function(){
				self.createAdditional();
			});
		},
		bindData: function(){
			// resetting form
			$("#inputTitle").val("");
			$("#inputSummary").val("");
			var self = this;
			// Get all skills of user
			this.db.select().then(function(rows){
				var items = "";
				_.each(rows, function(row){
					var item = '<tr><td>';
		  				item += '<div class="row course-row"><div class="col-md-12">';
		  				item += '<h2  class="content-title inline-content">'+row.title+'</h2>';
						item += '<p>'+row.summary+'</p>';
			  			item += '<a href="javascript:void(0);" data-id="'+row._id+'" class="additional_remove text-danger pdl16 pull-right"><span class="fa fa-remove fa-2x text-danger"></span> Remove</a>';
			  			item += '</div></div></td></tr>';
			  		items += item + "\n";
				});
				$("#table_body").html(items);
				$(".additional_remove").each(function(i){
					$(this).on("click", function(){
						var id = $(this).attr("data-id");
						self.deleteAdditional(id);
					});
				});
			});
		},

		createAdditional: function(){
			var self = this;
			if(this.validForm()){
				this.db.create(this.getFormData()).then(function(newId){
					if(newId>0){
						self.wd.get("notify").call("showToast",{"message":"Additional Detaill added"});
						self.bindData();
					}	
				});
			}
		},
		validForm: function(){
			var title = $("#inputTitle").val();
			var summary = $("#inputSummary").val();
			if(title == ""){
				this.notify("Provide Additional Title");
				return false;
			}
			if(summary == ""){
				this.notify("Provide some details");
				return false;
			}
			return true;
		},
		notify: function(msg){
			this.wd.get("notify").call("showToast", {"message": msg});
		},
		deleteAdditional: function(lang_id){
			var self = this;
			this.wd.get('notify').call("showConfirm", {"message": "Are you sure want to delete ?"}, true)
			.then(function(res){
				if(res.key == "yes"){
					self.db.delete([["_id", "=", lang_id]]).then(function(res){
						if(res > 0){
							self.notify("Additional Detail deleted !");
							self.bindData();
						}
					});
				}
			});
		},
		getFormData: function(){
			return {
				'title': $("#inputTitle").val(),
				'summary': $("#inputSummary").val(),
				'user_id': this.db.user_id
			};
		}
	});

	Additional.init();
});
