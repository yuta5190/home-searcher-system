//地図を操作するVue

const createTag = Vue.createApp({}); 
createTag.use(store);
createTag.component('tag-create-vue', { 
 data() {
    return {
      touchOption:"操作モード",
      dbclick:"",
      }
    },

methods: { 
//地図を現在位置に戻すメソッド
 moveCenter(){
  this.$store.state.map.panTo(new google.maps.LatLng(this.$store.state.latitude,this.$store.state.longitude))},

//地図を初期位置に戻すメソッド
returnCenter(){
this.$store.state.longitude=this.$store.state.firstLng;
this.$store.state.latitude=this.$store.state.firstLat;
this.$store.state.centerPosition = new google.maps.LatLng(this.$store.state.latitude,  this.$store.state.longitude);
this.$store.state.map.panTo(new google.maps.LatLng(this.$store.state.latitude,this.$store.state.longitude));


//現在地マーカー移動処理
this.centerMarking();
},

//マーカーモードの時にピンを打つメソッド
dbclickMap(geo,map) {
		//クリックした箇所の緯度経度をstoreに入れる
		this.$store.state.latitude = geo.lat();
		this.$store.state.longitude = geo.lng();
		this.$store.state.centerPosition = new google.maps.LatLng(this.$store.state.latitude, this.$store.state.longitude);
		
		this.centerMarking();
		
	   },
	    handleDoubleClick(e) {
    this.dbclickMap(e.latLng, this.$store.state.map);
  },
  
//操作切り替え
changeDbclickOption() {
  if (this.touchOption === "操作モード") {
    this.touchOption = "ピンモード";
    this.$store.state.map.setOptions({ disableDoubleClickZoom:true});
    this.dbclick=this.$store.state.map.addListener('dblclick', (e) => {
      this.dbclickMap(e.latLng, this.$store.state.map);
    });
  } else {
    this.touchOption = "操作モード";
    this.$store.state.map.setOptions({ disableDoubleClickZoom: false });
   google.maps.event.removeListener(this.dbclick);
  }
},
//現在のcenterPositionから中心地にマーキング
centerMarking(){
this.$store.state.currentLocationMarker.setVisible(false);
this.$store.state.currentLocationMarker= new google.maps.Marker({
				position: this.$store.state.centerPosition,
				icon:{url:'/img/currentLocation.png',scaledSize : new google.maps.Size(110, 110)}
			});

//作成した地図をset
this.$store.state.currentLocationMarker.setMap(this.$store.state.map);
},
},
template: `
  <div id="pin-search">
<h2>操作メニュー</h2>
<div id="controller-button">
<button v-on:click="moveCenter" class="btn btn-malformation">現在位置</button>
<button v-on:click="returnCenter" class="btn btn-malformation">初期位置</button>
<button v-on:click="changeDbclickOption" class="btn btn-malformation">{{touchOption}}</button>
</div>
</div>	` }); 
createTag.mount('#option-style');