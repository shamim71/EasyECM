/**
 * Contains some commond dialog information that will be used 
 * all over the application
 */
dojo.provide("ecm.common.EcmDialog");

dojo.declare("ecm.common.EcmDialog", [], {

	constructor: function() {
	},
	
	showWarningDialog: function(msg) {
		dojo.byId('application.dialog.message.warning.container').innerHTML = msg;
		dijit.byId("application.dialog.message.warning").show();
	},
	
	showInformationDialog: function(msg) {
		dojo.byId('application.dialog.message.container').innerHTML = msg;
		dijit.byId("application.dialog.message").show();
	},
	
	showErrorDialog: function(msg) {
		dojo.byId('application.dialog.message.error.container').innerHTML = msg;
		dijit.byId("application.dialog.message.error").show();
	}

});