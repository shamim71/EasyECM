/**
 * 
 */
dojo.provide("ecm.sec.SecDataController");

dojo.declare("ecm.sec.SecDataController", [], {

	dataGrid: null,
	dataStore: null,
	btnAdd: null,
	btnEdit: null,
	btnDelete: null,
	userDialog: null,
	dlgSaveButton: null,
	dlgCancelButton: null,
	restStore: null,
	commonDialog: null,
	
	constructor: function() {
		//this.dataGrid = args;
		//this.initializeListener();
	},
	
	initializeListener: function() {

		this.creatingUserService();
		
		//dojo.connect(this.dataGrid, "onStyleRow", this, "onStyleRow");
	
		this.initializeToolBar();
		this.commonDialog = new ecm.common.EcmDialog();

	},

	initializeToolBar: function(){
		this.btnAdd = dijit.byId('user.add');
		this.btnEdit = dijit.byId('user.edit')
		this.userDialog = dijit.byId('userDialog');
		
		dojo.connect(this.btnAdd, "onClick", this, "showUserDialog");
		dojo.connect(this.btnEdit, "onClick", this, "loadUserDialog");
		
		this.dlgSaveButton = dijit.byId('user.dialog.save')
		
		/***/
		dojo.connect(this.dlgSaveButton, "onClick", this, "onSaveUser");
		
	},

	showUserDialog: function(){
		var userForm = dojo.byId("user.dialog.form");
		
		userForm.reset();
		this.enableFormValue(userForm);
		this.userDialog.show();

	},
	loadUserDialog: function(){
		
		var selArray = this.dataGrid.selection.getSelected();
		if (selArray.length == 0) {
			this.commonDialog.showWarningDialog("No user has been selected.");
			return;
		}
		var row = selArray[0];
		console.debug(row);
		var userForm = dojo.byId("user.dialog.form");
		userForm.reset();
		
		userForm["user.dialog.name"].value = row.distinguishName;
		userForm["user.dialog.display.name"].value = row.displayName;
		userForm["user.dialog.email"].value = row.emailAddress;
		userForm["user.dialog.password"].value= "dummy";
		userForm["user.dialog.confirm.password"].value= "dummy";
		
		this.disableFormValue(userForm);

		this.userDialog.show();

	},
	disableFormValue: function(userForm){
		userForm["user.dialog.name"].disabled= true;
		userForm["user.dialog.password"].disabled= true;
		userForm["user.dialog.confirm.password"].disabled= true;
	},
	enableFormValue: function(userForm){
		userForm["user.dialog.name"].disabled= false;
		userForm["user.dialog.password"].disabled= false;
		userForm["user.dialog.confirm.password"].disabled= false;
	},
	hideUserDialog: function(){
		this.userDialog.hide();
	},
	
	onSaveUser: function(){
		console.debug("Saving store...");
		var userForm = dojo.byId("user.dialog.form");
		
		var name = userForm["user.dialog.name"].value;
		var displayName = userForm["user.dialog.display.name"].value;
		var email = userForm["user.dialog.email"].value;
		var password = userForm["user.dialog.password"].value;
		var confirmPassword = userForm["user.dialog.confirm.password"].value;
		
		var userObj = {distinguishName: name, displayName:displayName, emailAddress: email,password: password };
		console.debug(userObj);
		
		//this.dataStore.newItem(userObj);
		//this.dataStore.save();
		
		
		
		this.commonDialog.showInformationDialog("User has been created successfully.");
		this.userDialog.hide();
	},
	
	validateUser: function(){
		
	},
	editUser: function(){
		
	},	
	creatingUserService: function(){
		//this.userDialog.hide();
		console.debug("creating json rest sstore...");
		
		var targUrl = "http://localhost:8080/easy-ecm-service/service/security/users/";
		
		this.restStore = new dojo.store.JsonRest({target: targUrl});
		var myStore2 = dojo.store.Cache( this.restStore, dojo.store.Memory({}));
		//var myStore2 = dojo.store.Memory({});
        var layout4 = [
            {name:"User name", field:"distinguishName", width: "30%"},
            {name:"Display name", field:"displayName", width: "30%"},
            {name:"Email address", field:"emailAddress", width: "30%"}
        ];

        this.dataStore = dojo.data.ObjectStore({objectStore: myStore2});
              
        
	    this.dataGrid = new dojox.grid.DataGrid({ id: 'usersDataGrid',
	        store: this.dataStore,
	        clientSort: true,
	        structure: layout4
	    }, document.createElement('div'));

    // append the new grid to the div "gridContainer4":
    dojo.byId("userContentPane4x").appendChild(this.dataGrid.domNode);

    // Call startup, in order to render the grid:
    this.dataGrid.startup();
       
	
	},
	
	formatGridCell: function(value, row, cell) {
		
	//var pkg = acp.sysconfig.notifMgmt;
		var cellField = cell.getValue("field");
		console.debug(row);
		return "todo"+ value + " "+ row ;
	
	},
	
	onStyleRow: function(row) {
		with (row) {
			if (odd)
				customClasses = "oddRow";
			if (selected)	
				customClasses ="selectedRow";
			if (over)	
				customClasses ="overRow";
		}	

	},
	
	formatGridCellName: function(value, row, cell) {

		var userPicture = "<img src='http://localhost:8080/easy-ecm-web/images/businessman.gif' alt='image:checked'>";
		var userName = "<br/><strong>Shamim Ahmmed</strong>";
		var cellField = cell.getValue("field");
		var content = "<div style='border: 1px solid red; text-align: left';margin: 0;>";
		content = content + userPicture;
		content = content + userName;
		content = content + "</div>";
		
		//console.debug(":: "+value);
		
		return content ;
	
	}
});