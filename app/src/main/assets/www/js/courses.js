$(document).ready(function(){
	var Courses = dashboard.extend({
		db: WDDatabase.pool("user_courses"),
		init: function(){
			this.bindData();
			var self = this;
			$("#btnSave").on('click', function(){
				self.createCourse();
			});
		},
		bindData: function(){
			// resetting form
			$("#inputCollegeName").val("");
			$("#inputField").val("");
			$("#inputDegree").val("");
			$("#inputStartDate").val("");
			$("#inputEndDate").val("");
			$("#checkboxOnGoing").val("");
			$("#inputGrade").val("");
			$("#inputActivity").val("");
			$("#inputSummary").val("");
			var self = this;
			// Get all details of course
			this.db.select().then(function(rows){
				var items = "";
				_.each(rows, function(row){
					var item = '<tr><td>';
		  				item += '<div class="row course-row"><div class="col-md-12">';
		  				item += '<h2  class="content-title inline-content">'+row.institute+'</h2>';
						item += '<h5  class="content-subtitle">'+row.field_of_study+'</h5>';
						item += '<p>'+row.grade+'</p>';
						item += '<p>'+row.activities_associated+'</p>';
						item += '<p>'+row.summary+'</p>';
			  			item += '<a href="javascript:void(0);" data-id="'+row._id+'" class="course_remove text-danger pdl16 pull-right"><span class="fa fa-remove fa-2x text-danger"></span> Remove</a>';
			  			item += '</div></div></td></tr>';
			  		items += item + "\n";
				});
				$("#table_body").html(items);
				$(".course_remove").each(function(i){
					$(this).on("click", function(){
						var id = $(this).attr("data-id");
						self.deleteCourse(id);
					});
				});
			});
		},

		createCourse: function(){
			var self = this;
			if(this.validForm()){
				this.db.create(this.getFormData()).then(function(newId){
					if(newId>0){
						self.wd.get("notify").call("showToast",{"message":"New course added"});
						self.bindData();
					}	
				});
			}
		},
		validForm: function(){
			var college = $("#inputCollegeName").val();
			var field = $("#inputField").val();
			var degree = $("#inputDegree").val();
			var startDate = $("#inputStartDate").val();
			var endDate = $("#inputEndDate").val();
			var grade = $("#inputGrade").val();
			var isChecked = $('#checkboxOnGoing:checked').val()?true:false;
			var activity = $("#inputActivity").val();
			var summary = $("#inputSummary").val();

			if(isChecked == "false"){
				if(endDate == ""){
					this.notify("Select end date or check On Going");
					return false;
				}
				if(grade == ""){
					this.notify("Select start date of course");
					return false;
				}
			}
			if(isChecked == "true"){
				$("#inputEndDate").val("");
			}

			if(college == ""){
				this.notify("Provide School/College Name");
				return false;
			}
			if(field == ""){
				this.notify("Provide Field of study");
				return false;
			}
			if(degree == ""){
				this.notify("Provide Degree Information");
				return false;
			}
			if(startDate == ""){
				this.notify("Select start date of course");
				return false;
			}
			if(activity == ""){
				this.notify("Provide activity iniformation");
				return false;
			}
			if(summary == ""){
				this.notify("Provide some information about course");
				return false;
			}
			
			
			return true;
		},
		notify: function(msg){
			this.wd.get("notify").call("showToast", {"message": msg});
		},
		deleteCourse: function(lang_id){
			var self = this;
			this.wd.get('notify').call("showConfirm", {"message": "Are you sure want to delete ?"}, true)
			.then(function(res){
				if(res.key == "yes"){
					self.db.delete([["_id", "=", lang_id]]).then(function(res){
						if(res > 0){
							self.notify("Course deleted !");
							self.bindData();
						}
					});
				}
			});
		},
		getFormData: function(){
			return {
				'institute': $("#inputCollegeName").val(),
				'field_of_study': $("#inputField").val(),
				'degree': $("#inputDegree").val(),
				'start_date': $("#inputStartDate").val(),
				'end_date': $("#inputEndDate").val(),
				'on_going': $("#checkboxOnGoing").val(),
				'grade': $("#inputGrade").val(),
				'activities_associated': $("#inputActivity").val(),
				'summary': $("#inputSummary").val(),
				'user_id': this.db.user_id
			};
		}
	});

	Courses.init();
});
