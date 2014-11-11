dojo.provide("ecm.Container");

ecm.Container = {
	store: null,
	preloadDelay: 500,
	query: dojo.config.flickrRequest || {},
	itemTemplate: '<img src="${thumbnail}"/>${title}',
	itemClass: 'item',
	_itemsById: {},
	
	largeImageProperty: "media.l", // path to the large image url in the store item
	thumbnailImageProperty: "media.t", // path to the thumb url in the store item
	//securityController: null,
	navigationController: null,
	secDataController: null,
	securityController: null,
	
	parseOnLoad: (function(config){
		// set dojo.config.parseOnLoad to false
		// and store the original value in our own parseOnLoad property
		var parseOnLoad = config.parseOnLoad;
		config.parseOnLoad = false;
		return parseOnLoad;
	})(dojo.config),
	
	init: function() {
		this.startLoading();
		
		// phase 2: load further dependencies
		dojo.require("ecm.header");
	
		// register callback for when dependencies have loaded
		dojo.ready(dojo.hitch(this, "startup"));
		
	},
	startup: function() {

		// create the data store
		var flickrStore = this.store = new dojox.data.FlickrRestStore();
		
		//this.securityController = new ecm.SecurityController();
		//dojo.hitch(this.securityController,"initializeListener");
		
		//this.navigationController = new ecm.NavigationController(dojo.byId('sysConfigTree'));
		//dojo.hitch(this.navigationController,"initializeListener");
		// when items are being rendered, also preload the images
		//dojo.connect(dojo.byId('sysConfigTree'), "onClick", function(item, node, evt){  console.debug(".......................");} );
		// set up click handling for the search button


		
		// wait until UI is complete before taking down the loading overlay
		dojo.connect(this, "initUi", this, "endLoading");

		// build up and initialize the UI
		this.initUi();

		// put up the loading overlay when the 'fetch' method of the store is called
		//dojo.connect(
		//	this.store, 
		//	"fetch", 
		//	dojo.hitch(this, "startLoading", dijit.byId("tabs").domNode)
		//);
		// take down the loading overlay after onResult (fetch callback) is called
		//dojo.connect(this, "onResult", this, "endLoading");
	},
	sayHello: function(){
	
	console.debug("say hello........");
	},
	
	
	endLoading: function() {
		// summary: 
		// 		Indicate not-loading state in the UI
		
		dojo.fadeOut({
			node: dojo.byId('loadingOverlay'),
			onEnd: function(node){
				dojo.style(node, 'display', 'none');
			}
		}).play()
	},

	startLoading: function(targetNode) {
		// summary: 
		// 		Indicate a loading state in the UI
		
		var overlayNode = dojo.byId('loadingOverlay');
		if("none" == dojo.style(overlayNode, "display")) {
			var coords = dojo.coords(targetNode || dojo.body());
			dojo.marginBox(overlayNode, coords);

			// N.B. this implementation doesn't account for complexities
			// of positioning the overlay when the target node is inside a 
			// position:absolute container
			dojo.style( dojo.byId('loadingOverlay'), {
				display: 'block',
				opacity: 1
			});
		}
	},
	initUi: function() {
		// summary: 
		// 		create and setup the UI with layout and widgets

		if(this.parseOnLoad) {
			// we've declared the layout in markup, 
			// parse that now to instantiate those widgets
			dojo.parser.parse();
		}
		
//		dojo.connect(treeObj, "onClick", function(item, node, evt){
//			var terms = dojo.byId("searchTerms").value;
//			if(terms.match(/\w+/)) {
//				this.doSearch(terms);
//			}
//			console.debug(".................................");
//		});

				
		this.navigationController = new ecm.NavigationController(dijit.byId('sysConfigTree'));
		
		this.secDataController = new ecm.sec.SecDataController();
		
		var userPage = dijit.byId('scUserMgmt');
		dojo.connect(userPage, "onLoad", this.secDataController, "initializeListener");
		
		//this.securityController = new ecm.SecurityController();
		
		//dojo.hitch(this.secDataController,"initializeListener");

	},

	showImage: function(url, originNode){
		// summary: 
		// 		Show the full-size image indicated by the given url
		this.lightbox.show({ 
			href:url, origin: originNode 
		});
	},
	
	showItemById: function(id, originNode) {
		var item = this._itemsById[id];
		if(item) {
			this.showImage( dojo.getObject(this.largeImageProperty, false, item), originNode);
		}
	},
	_onListClick: function(evt) {
		var node = evt.target, 
			containerNode = dijit.byId("tabs").containerNode;
		
		for(var node = evt.target; (node && node !==containerNode); node = node.parentNode){
			if(dojo.hasClass(node, this.itemClass)) {
				this.showItemById(node.id.substring(node.id.indexOf("_") +1), node);
				break;
			}
		}
	}
};