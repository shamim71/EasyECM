dojo.provide("ecm.Main");

ecm.Main = {
	store: null,
	preloadDelay: 500,
	query: dojo.config.flickrRequest || {},
	itemTemplate: '<img src="${thumbnail}"/>${title}',
	itemClass: 'item',
	_itemsById: {},
	
	largeImageProperty: "media.l", // path to the large image url in the store item
	thumbnailImageProperty: "media.t", // path to the thumb url in the store item
	
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
		dojo.require("ecm.module");
	
		// register callback for when dependencies have loaded
		dojo.ready(dojo.hitch(this, "startup"));
		
	},
	startup: function() {

		// create the data store
		var flickrStore = this.store = new dojox.data.FlickrRestStore();
		
		// when items are being rendered, also preload the images
		dojo.connect(this, "renderItems", this, "preloadItems");
		
		// wait until UI is complete before taking down the loading overlay
		dojo.connect(this, "initUi", this, "endLoading");

		// build up and initialize the UI
		this.initUi();

		// put up the loading overlay when the 'fetch' method of the store is called
		dojo.connect(
			this.store, 
			"fetch", 
			dojo.hitch(this, "startLoading", dijit.byId("tabs").domNode)
		);
		// take down the loading overlay after onResult (fetch callback) is called
		dojo.connect(this, "onResult", this, "endLoading");
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

		// create a single Lightbox instnace which will get reused
		this.lightbox = new dojox.image.LightboxNano({});

		// set up ENTER keyhandling for the search keyword input field
		dojo.connect(dojo.byId("searchTerms"), "onkeypress", this, function(evt){
			if(evt.keyCode == dojo.keys.ENTER){
				dojo.stopEvent(evt);
				var terms = dojo.byId("searchTerms").value;
				if(terms.match(/\w+/)) {
					this.doSearch(terms);
				}
			}
		});

		// set up click handling for the search button
		dojo.connect(dojo.byId("searchBtn"), "onclick", this, function(evt){
			var terms = dojo.byId("searchTerms").value;
			if(terms.match(/\w+/)) {
				this.doSearch(terms);
			}
		});
	},
	doSearch: function(terms) {
		// summary: 
		// 		inititate a search for the given keywords
		
		var request = {
			query: dojo.delegate(this.query, {
				text: terms
			}),
			count: 10,
			onComplete: dojo.hitch(this, function(items, request){
				this.onResult(terms, items);
			}),
			onError: dojo.hitch(console, "error")
		};
		this.store.fetch(request);
	},
	showImage: function(url, originNode){
		// summary: 
		// 		Show the full-size image indicated by the given url
		this.lightbox.show({ 
			href:url, origin: originNode 
		});
	},
	onResult: function(term, items) {
		// summary: 
		// 		Handle fetch results
		
		// FlickrStore doesn't support the Identity API
		// so we populate our own byId lookup
		var itemsById = this._itemsById;
		dojo.forEach(items, function(item){
			// add entry to id=>item lookup
			itemsById[item.id] = item;
		}, this);
		
		var contr = dijit.byId("tabs"); 
		var listNode = dojo.create("ul", {
			'class': 'demoImageList',
			'id': 'panel'+contr.getChildren().length
		});

		// create the new tab panel for this search
		var panel = new dijit.layout.ContentPane({
			title: term, 
			content: listNode,
			closable: true
		});
		contr.addChild(panel);
		// render the items into the <ul> node
		this.renderItems(items, listNode);
		// make this tab selected
		contr.selectChild(panel);
		
		// connect mouse click events to our event delegation method
		var hdl = dojo.connect(listNode, "onclick", this, "_onListClick");
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
	},
	renderItem: function(item, refNode, posn) {
		// summary: 
		// 		Create HTML string to represent the given item
		var props = dojo.delegate(item, {
			thumbnail: dojo.getObject(this.thumbnailImageProperty, false, item)
		});
		
		return dojo.create("li", {
			'class': this.itemClass,
			innerHTML: dojo.string.substitute(this.itemTemplate, props)
		}, refNode, posn);
	},
	renderItems: function(items, listNode, idPrefix){
		// summary: 
		// 		Put rendering of the items into the given container (list) node
		
		var store = this.store, 
			frag = dojo.doc.createDocumentFragment();

		var lis = dojo.forEach(items, function(item){
			// what no getIdentity? and getValue(item, 'id') returns undefined???
			var node = this.renderItem(item);
			node.id = (idPrefix || listNode.id) + "_" + item.id;
			frag.appendChild( node );
		}, this);
		listNode.appendChild(frag);
	},
	preloadItems: function(items) {
		// summary: 
		// 			preload images so they come from cache when viewed
		
		var delay = this.preloadDelay, 
			largeImageProperty = this.largeImageProperty;
		
		dojo.forEach(items, function(item){
			// start preloading the large images
			// setTimeout ensures they happen in parallel, out of band
			setTimeout(function(){
				(new Image()).src = dojo.getObject(largeImageProperty, false, item);
			}, delay);
		})
	}
};