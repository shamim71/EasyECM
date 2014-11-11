define([
	"dojo/dom", 
	"dojo/on", 
	"dijit/registry", 
	"dojo/_base/declare",
	"dojo/domReady!"
	], 
	function(dom,on,registry,declare) {
		
	init = function() {
		
	},
	nodeSelected = function(item, node, evt){
			console.debug(" Node selected event called.........");
			var itemId = sysConfigTreeStore.getValue(item, "itemId");
			console.debug("Item id: "+itemId);
			registry.byId("secConfigContentStack").selectChild(itemId);
	};	

return declare("ecm.NavigationController", null, {
	

	constructor: function() {
		//init();
	},
	
	initializeListener: function(){
	
		var navigationTree = registry.byId('sysConfigTree');
		console.debug("Initializing navigation tree: "+ navigationTree);
		on(navigationTree, "click",nodeSelected);
	},
	hello: function(){
		console.debug("Say Hello..");
	}
});

});