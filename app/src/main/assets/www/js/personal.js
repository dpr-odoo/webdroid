$(document).ready(function(){
	var Personal = dashboard.extend({
		db: WDDatabase.pool("personal_details"),
		updateRecord: false,
		init: function(){
			this.bindEvents();
			this.fillForm();
		},
		fillForm: function(){
			var self = this;
			this.db.browse([["user_id","=",this.db.user_id]]).then(function(record){
				if(record != false){
					self.setImage(record.image);
					self.updateRecord = true;
				}
				if(record.full_name == false){
					record.full_name = self.db.user.full_name;
				}
				$("#inputName").val(record.full_name);
				if(record.address!=false)
					$("#inputAddress").val(record.address);

				var dob = record.date_of_birth + "";
				if(dob != "" || dob != "false"){
					dob = dob.split("-");
				}
				if(dob.length == 3){

					$("#dob_date option[value="+dob[0]+"]").attr("selected","selected");
					$("#dob_month option[value="+dob[1]+"]").attr("selected","selected");
					$("#dob_year option[value="+dob[2]+"]").attr("selected","selected");
				}
				var gender = record.gender;
				$("#inputGender option[value="+gender+"]").attr("selected","selected");
				$("#inputState option[value="+record.state+"]").attr("selected","selected");
				$("#inputCountry option[value="+record.country+"]").attr("selected","selected");
				$("#inputCity option[value="+record.city+"]").attr("selected","selected");
				if(record.mobile_number != false)
					$("#inputMobileNumber").val(record.mobile_number);
				if(record.summary != false)
					$("#inputSummary").val(record.summary);

				$("#btnSave").on("click", function(){
					self.saveData();
				});
			});
		},
		bindEvents: function(){
			var self = this;
			$("#userImage").on("click", function(){
				self.wd.get("gallery").call("requestImage",{},true).then(function(image){
					if(image != false){
						self.storeImage(image);
						self.setImage(image);
					}
				});
			});
		},
		storeImage : function(base64){
			var self = this;
			this.db.count([["user_id", "=", this.db.user_id]]).then(function(count){
				var img = {
					"image": base64
				};
				if(count > 0){
					// Update
					self.db.update(img, [["user_id","=",self.db.user_id]]).then(function(res){
						console.log("Image updated");
					});
				}else{
					// New
					self.db.create(img).then(function(newId){
						console.log("New record creted with image");
					});
				}
			});
		},
		setImage : function(base64){
			if(base64!=false){
				$("#userImage").attr("src","data:image/jpeg;base64, "+base64);	
			}else{
				$("#userImage").attr("src","images/no-avatar.png");
			}
		},
		notify : function(msg){
			this.wd.get("notify").call("showToast", {"message": msg});
		},
		saveData: function(){
			var self = this;
			if(this.validForm()){
				if(this.updateRecord){
					this.db.update(this.getData(), [["user_id","=",this.db.user_id]])
						.then(function(count){
							if(count>0){
								self.notify("Information saved.");
							}	
						});
				}else{
					this.db.create(this.getData()).then(function(newId){
						if(newId>0){
							self.notify("Information saved.");
						}
					});
				}
			}
		},
		validForm: function(){
			if($("#inputName").val() == ""){
				this.notify("Provide full name");
				return false;
			}
			if($("#inputAddress").val() == ""){
				this.notify("Provide address");
				return false;
			}
			if($("#dob_date").val() =="-1" ||
				 $("#dob_month").val() =="-1" ||  $("#dob_year").val() == "-1"){
				this.notify("Select valid date of birth");
				return false;
			}
			if($("#inputGender").val() == "select"){
				this.notify("Select gender");
				return false;
			}

			if($("#inputCity").val() == "select"){
				this.notify("Select city");
				return false;
			}
			if($("#inputState").val() == "select"){
				this.notify("Select sate");
				return false;
			}
			if($("#inputCountry").val() == "select"){
				this.notify("Select country");
				return false;
			}
			if($("#inputMobileNumber").val() == ""){
				this.notify("Provide mobile number");
				return false;
			}
			return true;
		},
		getData: function(){
			return {
				"full_name":  $("#inputName").val(),
				"address":  $("#inputAddress").val(),
				"date_of_birth": $("#dob_date").val() + "-" + $("#dob_month").val() + "-" + $("#dob_year").val(),
				"gender": $("#inputGender").val(),
				"city":$("#inputCity").val(),
				"state":$("#inputState").val(),
				"country":$("#inputCountry").val(),
				"mobile_number": $("#inputMobileNumber").val(),
				"summary": $("#inputSummary").val()
			};
		}
	});
	Personal.init();
});