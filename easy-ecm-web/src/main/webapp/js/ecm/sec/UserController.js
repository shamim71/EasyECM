define([
	"dojo/dom",
	"dojo/dom-style",
	"dojo/on", 
	"dijit/registry", 
	"dojo/_base/declare",
    "dojo/store/JsonRest",
    "dojo/store/Cache",
    "dojo/store/Memory",
    "dojo/data/ObjectStore",
    "dojox/grid/DataGrid",
    "ecm/common/EcmDialog",
    "dojo/_base/Deferred",
	"dojo/domReady!"
	], 
	function(dom,domStyle,on,registry,declare,jsonRestStore,cacheStore,memoryStore,objectStore,dataGrid,commonDialog,Deferred) {
	
	
	var ecmDialog = new commonDialog(),
	
	init = function() {

	},
	initializeToolBar = function(){
		var btnAdd = registry.byId('user.add');
		var btnEdit = registry.byId('user.edit');
		var btnDelete = registry.byId('user.delete');

		on(btnAdd, "click",showUserDialog);
		on(btnEdit, "click",loadUserDialog);
		on(btnDelete, "click",onDeleteUser);
		
		var dlgSaveButton = registry.byId('user.dialog.save')
		on(dlgSaveButton, "click",onSaveUser);
		
		var dlgEditButton = registry.byId('user.dialog.update')
		on(dlgEditButton, "click",onEditUser);
	},	
	showUserDialog = function(){
		var userForm = registry.byId("user.dialog.form");
		userForm.reset();
		
		domStyle.set(registry.byId('user.dialog.update').domNode, { visibility: 'hidden', display:'none'});
		domStyle.set(registry.byId('user.dialog.save').domNode, { visibility: 'visible', display:''});

		enableFormValue();
		registry.byId('userDialog').set("title", 'New user');
		registry.byId('userDialog').show();
	
	},	
	loadUserDialog = function(){
		var userForm = registry.byId("user.dialog.form");
		userForm.reset();
		
		domStyle.set(registry.byId('user.dialog.save').domNode, { visibility: 'hidden', display:'none'});
		domStyle.set(registry.byId('user.dialog.update').domNode, { visibility: 'visible', display:''});
		
		var grid = registry.byId("usersDataGrid");
		var selArray = grid.selection.getSelected();
		if (selArray.length == 0) {
			ecmDialog.showWarningDialog("No user has been selected.");
			return;
		}
		var row = selArray[0];
		
		/** Setting form values */
		registry.byId("user.dialog.name").set("value",row.distinguishName);
		registry.byId("user.dialog.display.name").set("value",row.displayName);
		registry.byId("user.dialog.email").set("value",row.emailAddress);
		registry.byId("user.dialog.password").set("value","dummy");
		registry.byId("user.dialog.confirm.password").set("value","dummy");
		
		disableFormValue();
		registry.byId('userDialog').set("title", 'Update user');
		registry.byId('userDialog').show();
			
	},
	onSaveUser = function(){
		console.debug("Saving store...");
		var userForm = registry.byId("user.dialog.form");

		if(userForm.validate() == false){
			ecmDialog.showWarningDialog("One or more input values are missing.");
			return;
		}
		var name = registry.byId("user.dialog.name").value;
		var displayName = registry.byId("user.dialog.display.name").value;
		var email = registry.byId("user.dialog.email").value;
		var password = registry.byId("user.dialog.password").value;
		var confirmPassword =  registry.byId("user.dialog.confirm.password").value;
		
		/** Validate passwords are same */
		if(password != confirmPassword){
			ecmDialog.showWarningDialog("Passwords mismatch");
			return;
		}
		var userObj = {distinguishName: name, displayName:displayName, emailAddress: email,password: password };
		console.debug(userObj);
		
		var store = registry.byId("usersDataGrid").store;
		console.debug("Saving User ..."+ store);

		store.newItem(userObj);
		store.save({onComplete: onSaveComplete, onError: onSaveError});	
	},
	createUserObject = function(){
		
		var name = registry.byId("user.dialog.name").value;
		var displayName = registry.byId("user.dialog.display.name").value;
		var email = registry.byId("user.dialog.email").value;
		var password = registry.byId("user.dialog.password").value;
		var confirmPassword =  registry.byId("user.dialog.confirm.password").value;
		
		var userObj = {distinguishName: name, displayName:displayName, emailAddress: email,password: password };
		console.debug(userObj);
		
		return userObj;
	},
	disableFormValue = function(){
		registry.byId("user.dialog.name").set("disabled",true);

	},
	enableFormValue = function(userForm){
		registry.byId("user.dialog.name").set("disabled",false);

	},	
	validateUserForm = function(){
		var userForm = registry.byId("user.dialog.form");
		
		if(userForm.validate() == false){
			ecmDialog.showWarningDialog("One or more input values are missing.");
			return false;
		}
		return true;
	},	
	onSaveComplete = function(){
		ecmDialog.showInformationDialog("User has been created successfully.");
		registry.byId('userDialog').hide();
	},
	onSaveError = function(){
		ecmDialog.showErrorDialog("Failed to create user.");
	},
	onEditUser = function(){
		console.debug("Editing User ...");
		
		if(!validateUserForm()){
			console.debug("Form validation is failed.");
			return;
		}
		var password = registry.byId("user.dialog.password").value;
		var confirmPassword =  registry.byId("user.dialog.confirm.password").value;
		
		/** Validate passwords are same */
		if(password != confirmPassword){
			ecmDialog.showWarningDialog("Passwords mismatch");
			return;
		}
		
		var user = createUserObject();	
		console.debug(user.distinguishName);

		var grid = registry.byId("usersDataGrid");
		var store = grid.store;

		store.fetchItemByIdentity({
			identity: store.getIdentity(user),
			onItem: function(item){
				store.setValue(item, "displayName", user.displayName);
				store.setValue(item, "emailAddress", user.emailAddress);
				store.setValue(item, "password", user.password);
				
				store.save({
					onComplete: function(){ 
						ecmDialog.showInformationDialog("User has been updated successfully.");
						registry.byId('userDialog').hide();
					}, 
					onError: function(){ 
						ecmDialog.showErrorDialog("Failed to update user.");
					}
				});	
				
			}});


	},
	onDeleteUser = function(){
		console.debug("Deleting selected users ...");
		var grid = registry.byId("usersDataGrid");
		var selArray = grid.selection.getSelected();
		if (selArray.length == 0) {
			ecmDialog.showWarningDialog("No user has been selected.");
			return;
		}
		var row = selArray[0];
		var store = grid.store;
		
		ecmDialog.showConfirmationDialog({onYes: function(){
				
				store.deleteItem(row);
				store.save({
					onComplete: function(){ 
						ecmDialog.showInformationDialog("User has been deleted successfully.");
					}, 
					onError: function(){ 
						ecmDialog.showErrorDialog("Failed to delete user.");
					}
				});	
			},
			msg:"Do you want to delete the selected user?"});

	},
	creatingUserService = function(){

		console.debug("creating json rest sstore...");
		
		var targUrl = "http://localhost:8080/easy-ecm-service/service/security/users/";
		
		var restStore = new jsonRestStore({target: targUrl,idProperty: "distinguishName" });
		//var myStore2 = new cacheStore( restStore, memoryStore({}));
		//var myStore2 = dojo.store.Memory({});
        var layout4 = [
            {name:"User name", field:"distinguishName", width: "30%"},
            {name:"Display name", field:"displayName", width: "30%"},
            {name:"Email address", field:"emailAddress", width: "30%"}
        ];

        var dataStore = new objectStore({objectStore: restStore});
              
        
	    var userGrid = new dataGrid({ id: 'usersDataGrid',
	        store: dataStore,
	        clientSort: true,
	        structure: layout4
	    }, document.createElement('div'));

	    // append the new grid to the div "gridContainer4":
	    dom.byId("userContentPane4x").appendChild(userGrid.domNode);

	    // Call startup, in order to render the grid:
	    userGrid.startup();
       
	    initializeToolBar();
	};
	
	return declare("ecm.sec.UserController", null, {
		
		initializeListener: function(){
	
			var userPage = registry.byId('scUserMgmt');
			on(userPage, "load",creatingUserService);

		}
	});

});