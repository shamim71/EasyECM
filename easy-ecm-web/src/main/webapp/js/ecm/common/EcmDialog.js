/**
 * Contains some command dialog information that will be used 
 * all over the application
 */

 define([
	"dojo/dom", 
	"dijit/registry", 
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/on", 
	"dojo/domReady!"
	], 
	function(dom,registry,declare,lang,on) {

	return declare("ecm.common.EcmDialog", null, {
		buttonHandlers: [],
		showWarningDialog: function(msg) {
			dom.byId('application.dialog.message.warning.container').innerHTML = msg;
			registry.byId("application.dialog.message.warning").show();
		},
		
		showInformationDialog: function(msg) {
			dom.byId('application.dialog.message.container').innerHTML = msg;
			registry.byId("application.dialog.message").show();
		},
		
		showErrorDialog: function(msg) {
			dom.byId('application.dialog.message.error.container').innerHTML = msg;
			registry.byId("application.dialog.message.error").show();
		},
		showConfirmationDialog: function(args) {
			dom.byId('application.dialog.message.confirm.containerx').innerHTML = args.msg;
			var dialog = registry.byId("application.dialog.message.confirm");
			var okButton = registry.byId('confirm.dialog.yes');
			
			for(var i=0; i<this.buttonHandlers.length; i++){
				var dirty = this.buttonHandlers[i];
				dirty.remove();
			}
			
			var closeHanlder= on(okButton, "click",this.closeConfirm);
			var handler = on(okButton, "click",args.onYes);
			
			dialog.show();
			
			this.buttonHandlers.push(handler);
			this.buttonHandlers.push(closeHanlder);
			
			return dialog;
		},
		closeConfirm: function(){
			//declare.safeMixin(this, options);
			registry.byId("application.dialog.message.confirm").hide();
		}	
	});

});