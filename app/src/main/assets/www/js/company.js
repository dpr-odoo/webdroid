$(document).ready(function(){
	var Company = dashboard.extend({
		db: WDDatabase.pool("user_company"),
		init: function(){
			this.bindData();
			var self = this;
			$("#btnSave").on('click', function(){
				self.createComapny();
			});
		},
		bindData: function(){
			// resetting form
			$("#inputCompanyName").val("");
			$("#inputDesignation").val("");
			$("#inputJoinDate").val("");
			$("#inputEndDate").val("");
			$("#checkboxOnGoing").val("");
			$("#inputSummary").val("");
			var self = this;
			// Get all details of company
			this.db.select().then(function(rows){
				var items = "";
				_.each(rows, function(row){
					var item = '<tr><td>';
		  				item += '<div class="row course-row"><div class="col-md-12">';
		  				item += '<h2  class="content-title inline-content">'+row.company_name+'</h2>';
						item += '<h5  class="content-subtitle">'+row.designation+'</h5>';
						item += '<p>'+row.summary+'</p>';
			  			item += '<a href="javascript:void(0);" data-id="'+row._id+'" class="company_remove text-danger pdl16 pull-right"><span class="fa fa-remove fa-2x text-danger"></span> Remove</a>';
			  			item += '</div></div></td></tr>';
			  		items += item + "\n";
				});
				$("#table_body").html(items);
				$(".company_remove").each(function(i){
					$(this).on("click", function(){
						var id = $(this).attr("data-id");
						self.deleteCompany(id);
					});
				});
			});
		},

		createComapny: function(){
			var self = this;
			if(this.validForm()){
				this.db.create(this.getFormData()).then(function(newId){
					if(newId>0){
						self.wd.get("notify").call("showToast",{"message":"Company Added"});
						self.bindData();
					}	
				});
			}
		},
		validForm: function(){
			var company = $("#inputCompanyName").val();
			var designation = $("#inputDesignation").val();
			var startDate = $("#inputJoinDate").val();
			var endDate = $("#inputEndDate").val();
			var isChecked = $('#checkboxOnGoing:checked').val()?true:false;
			var summary = $("#inputSummary").val();

			if(isChecked == "false"){
				if(endDate == ""){
					this.notify("Select end date or check On Going");
					return false;
				}
			}
			if(isChecked == "true"){
				$("#inputEndDate").val("");
			}

			if(company == ""){
				this.notify("Provide Company Name");
				return false;
			}
			if(designation == ""){
				this.notify("Provide Designation");
				return false;
			}
			if(startDate == ""){
				this.notify("Select join date");
				return false;
			}
			if(summary == ""){
				this.notify("Provide some information about company");
				return false;
			}
			
			
			return true;
		},
		notify: function(msg){
			this.wd.get("notify").call("showToast", {"message": msg});
		},
		deleteCompany: function(lang_id){
			var self = this;
			this.wd.get('notify').call("showConfirm", {"message": "Are you sure want to delete ?"}, true)
			.then(function(res){
				if(res.key == "yes"){
					self.db.delete([["_id", "=", lang_id]]).then(function(res){
						if(res > 0){
							self.notify("Company deleted !");
							self.bindData();
						}
					});
				}
			});
		},
		getFormData: function(){
			return {
				'company_name': $("#inputCompanyName").val(),
				'designation': $("#inputDesignation").val(),
				'join_date': $("#inputJoinDate").val(),
				'end_date': $("#inputEndDate").val(),
				'on_going': $("#checkboxOnGoing").val(),
				'summary': $("#inputSummary").val(),
				'user_id': this.db.user_id
			};
		}
	});

	Company.init();
});
