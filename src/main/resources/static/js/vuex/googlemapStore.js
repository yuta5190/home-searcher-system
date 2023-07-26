const store = new Vuex.Store({
	state: {
//初期の緯度経度
	firstLat:"",
	firstLng:"",
	
//現在の緯度経度
		latitude: "0",
		longitude: "0",
		//現在の地図の中心地
		centerPosition:null,
		//現在位置のマーカー
		currentLocationMarker:"",
        //周辺検索の検索リスト
		priceList: [],
		crimeList: [],
		
		
		addressOptions:[],
		stationOptions:[],
		
		//カテゴリーのリストgooglemapに備わってるカテゴリーなら追加可能
		categoryList:[{ name: "atm", ja: "ATM",view:false },
        { name: "school", ja: "学校", view:false},
        { name: "grocery_or_supermarket", ja: "スーパー",view:false },
        { name: "hospital", ja: "医療機関",view:false },
        { name: "restaurant", ja: "飲食店",view:false },
        { name: "park", ja: "公園",view:false },
        { name: "post_office", ja: "郵便局",view:false },
        { name: "convenience_store", ja: "コンビニ",view:false },
        { name: "itemStore", ja: "販売店",view:false },
        { name: "amusument", ja: "アミューズメント",view:false }],
		itemStore:["book_store","clothing_store","department_store","electronics_store","florist","furniture_store","home_goods_store","jewelry_store","shoe_store","shopping_mall"],
		amusument:["amusement_park","aquarium","art_gallery","bowling_alley","movie_theater","museum","spa","zoo"],
		resultsList:[],
		
		//タブカテゴリー検索の平均評価
		ratingAverage:0,
		
		
		topList:[],
		//地図		
		map: null,
		//地図のピン
		marker: [],
		option:null,
		//地図の倍率
		ratio:"",
		//地図のマーカーウィンドウ
		infoWindow: [],
		//地図の周辺施設検索するメソッド
		service:"",
		//チェックされたカテゴリー
		selectedCategories: [],
	},
	mutations: {
		updateLatitude(state, latitude) {
			state.latitude = latitude;
		},
		updateLongitude(state, longitude) {
			state.longitude = longitude;
		},
		updateMap(state, map) {
			state.map = map
		},
		updateInstitution(state, institutionList) {
			state.institutionList = institutionList
		},
		updateMarker(state, marker) {
			state.marker = marker
		},
		updateInfoWindow(state, infoWindow) {
			state.infoWindow = infoWindow
		},
		updateItemStores(state, itemStores) {
			state.itemStores = itemStores
		},
	},  
});

