dojo.provide("ecm.widgets.UserRestStore");

dojo.declare("ecm.widgets.UserRestStore", dojox.data.JsonRestStore, {

	"-chains-": {
		 constructor: "manual"
	},

	constructor : function(options) { 
		dojo.safeMixin(this,options);
		this.service = dojox.rpc.JsonRest.services[options.target] || dojox.rpc.Rest(options.target, true, null, options.getRequest);

		this.inherited(arguments);
	}
});
