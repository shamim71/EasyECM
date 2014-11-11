/**
 * 
 */
dojo.provide("ecm.NavigationController");

dojo.declare("ecm.NavigationController", [], {

	treeObj: null,
	navigationTree: null,
	
	constructor: function(args) {
		this.navigationTree = args;
		console.debug("Initializing navigation tree: "+ this.navigationTree.id);
		
		this.initializeListener();
	},
	
	initializeListener: function() {
		console.debug(" Initializing Navigation controller...."+ this.navigationTree);
		dojo.connect(this.navigationTree, "onClick", this, "nodeSelected");
		
	},
	
	nodeSelected: function(item, node, evt){
		
		console.debug(" Node selected event called.........");
		var itemId = sysConfigTreeStore.getValue(item, "itemId");
		console.debug("Item id: "+itemId);
		 dijit.byId("secConfigContentStack").selectChild(itemId);
		 
		//var itemId = secConfigTreeStore.getValue(item, "itemId");
	}

});