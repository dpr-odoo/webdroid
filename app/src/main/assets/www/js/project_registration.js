$(document).ready(function(){
	var Project_Registration = dashboard.extend({
		db: WDDatabase.pool("user_projects"),
		init: function(){
			this.bindData();
			var self = this;
			$("#btnSave").on('click', function(){
				self.createProject();
			});
		},
		bindData: function(){
			// resetting form
			$("#inputProjectName").val("");
			$("#inputMembers").val("");
			$("#inputStartDate").val("");
			$("#inputEndDate").val("");
			$("#checkboxOnGoing").val("");
			$("#inputSummary").val("");
			var self = this;
			// Get all details of project
			this.db.select().then(function(rows){
				var items = "";
				_.each(rows, function(row){
					var item = '<tr><td>';
		  				item += '<div class="row course-row"><div class="col-md-12">';
		  				item += '<h2  class="content-title inline-content">'+row.project_title+'</h2>';
						item += '<h5  class="content-subtitle">'+row.members+'</h5>';
						item += '<p>'+row.summary+'</p>';
			  			item += '<a href="javascript:void(0);" data-id="'+row._id+'" class="project_remove text-danger pdl16 pull-right"><span class="fa fa-remove fa-2x text-danger"></span> Remove</a>';
			  			item += '</div></div></td></tr>';
			  		items += item + "\n";
				});
				$("#table_body").html(items);
				$(".project_remove").each(function(i){
					$(this).on("click", function(){
						var id = $(this).attr("data-id");
						self.deleteProject(id);
					});
				});
			});
		},

		createProject: function(){
			var self = this;
			if(this.validForm()){
				this.db.create(this.getFormData()).then(function(newId){
					if(newId>0){
						self.wd.get("notify").call("showToast",{"message":"Project Added"});
						self.bindData();
					}	
				});
			}
		},
		validForm: function(){
			var project = $("#inputProjectName").val();
			var member = $("#inputMembers").val();
			var startDate = $("#inputStartDate").val();
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

			if(project == ""){
				this.notify("Provide Project Name");
				return false;
			}
			if(member == ""){
				this.notify("Add project members");
				return false;
			}
			if(startDate == ""){
				this.notify("Select start date of project");
				return false;
			}
			if(summary == ""){
				this.notify("Provide some information about project");
				return false;
			}
			
			return true;
		},
		notify: function(msg){
			this.wd.get("notify").call("showToast", {"message": msg});
		},
		deleteProject: function(lang_id){
			var self = this;
			this.wd.get('notify').call("showConfirm", {"message": "Are you sure want to delete ?"}, true)
			.then(function(res){
				if(res.key == "yes"){
					self.db.delete([["_id", "=", lang_id]]).then(function(res){
						if(res > 0){
							self.notify("Project deleted !");
							self.bindData();
						}
					});
				}
			});
		},
		getFormData: function(){
			return {
				'project_title': $("#inputProjectName").val(),
				'members': $("#inputMembers").val(),
				'start_date': $("#inputStartDate").val(),
				'end_date': $("#inputEndDate").val(),
				'on_going': $("#checkboxOnGoing").val(),
				'summary': $("#inputSummary").val(),
				'user_id': this.db.user_id
			};
		}
	});

	Project_Registration.init();
});
