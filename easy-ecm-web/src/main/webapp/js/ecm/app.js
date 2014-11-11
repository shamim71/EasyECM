define([
	"dojo/dom", 
	"dojo/dom-style", 
	"dojo/dom-class", 
	"dojo/dom-construct", 
	"dojo/dom-geometry", 
	"dojo/string", 
	"dojo/on", 
	"dojo/aspect", 
	"dojo/keys", 
	"dojo/_base/lang", 
	"dojo/_base/fx", 
	"dijit/registry", 
	"dojo/parser",
	"dijit/layout/ContentPane", 
	"dojo/_base/window",
	"ecm/module"
	], 
	function(dom, domStyle, domClass, domConstruct, domGeometry, string, on, aspect, keys, lang, baseFx, registry, parser, ContentPane, win) {

	var store = null,
		preloadDelay = 500,
		
		startup = function() {


		// build up and initialize the UI
		initUi();
		var nav = new ecm.NavigationController();
		nav.initializeListener();
		
		console.debug("Initializing navigation xxxxxxx" +nav);
		var uc = new ecm.sec.UserController();
		uc.initializeListener();
			// put up the loading overlay when the 'fetch' method of the store is called
//			aspect.before(store, "fetch", function() {
//					//startLoading(registry.byId("tabs").domNode);
//				});
		},

		endLoading = function() {
			// summary: 
			// 		Indicate not-loading state in the UI

			baseFx.fadeOut({
				node: dom.byId("loadingOverlay"),
				onEnd: function(node){
					domStyle.set(node, "display", "none");
				}
			}).play();
		},

		startLoading = function(targetNode) {
			// summary: 
			// 		Indicate a loading state in the UI

			var overlayNode = dom.byId("loadingOverlay");
			if("none" == domStyle.get(overlayNode, "display")) {
				var coords = domGeometry.getMarginBox(targetNode || win.body());
				domGeometry.setMarginBox(overlayNode, coords);

				// N.B. this implementation doesn't account for complexities
				// of positioning the overlay when the target node is inside a 
				// position:absolute container
				domStyle.set(dom.byId("loadingOverlay"), {
					display: "block",
					opacity: 1
				});
			}
		},
		initUi = function() {
			// summary: 
			// 		create and setup the UI with layout and widgets

			// set up ENTER keyhandling for the search keyword input field
//			on(dom.byId("searchTerms"), "keydown", function(evt){
//				if(evt.keyCode == keys.ENTER){
//					evt.preventDefault();
//					//doSearch();
//				}
//			});

			// set up click handling for the search button
			//on(dom.byId("searchBtn"), "click", doSearch);
			
			//console.debug("Initializing navigation tree: "+ obj.id);

			endLoading();
		},
		doSearch = function() {

		},
		showImage = function (url, originNode){

		},
		createTab = function (term, items) {

		},

		showItemById = function (id, originNode) {

		},
		onListClick = function (evt) {

		},
		renderItem = function(item, refNode, posn) {

		};
	
	return {
		init: function() {
			startLoading();

			// register callback for when dependencies have loaded
			startup();

		}
	};

});