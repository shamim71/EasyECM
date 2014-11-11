/**
 * 
 */
dojo.provide("ecm.SecurityController");

dojo.declare("ecm.SecurityController", [], {

	navigationTree: null,
	dataGrid: null,
	dataStore: null,
	
	constructor: function() {
		console.debug("Calling SecurityController constructor ....");
		this.dataGrid = dijit.byId('usersDataGrid');
	},
	
	initializeListener: function() {
		console.debug(" Initializing security controller....");
		dojo.connect(this.dataGrid, "onStyleRow", this, "onStyleRow");
	},
	
	onStyleRow: function(inRow) {
		
		console.debug(" calling on row style ....");
	
		with (inRow) {
			var i = index % 10;

			if (odd)
				customStyles += ' color: orange;';
			if (selected)	
				customClasses += ' selectedRow';
			if (over)	
				customClasses += ' overRow';
			if (!over && !selected)
				dojox.grid.DataGrid.prototype.onStyleRow.apply(this, arguments);
		}	
	}

});