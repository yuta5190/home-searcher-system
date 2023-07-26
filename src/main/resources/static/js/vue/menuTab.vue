//左側のタブ（近隣地区や近隣駅の再表示に関わるvue）

const tabMenuCreate = Vue.createApp({}); 
tabMenuCreate.use(store);
tabMenuCreate.component('tab-menu-vue', { 
 data() {
    return {
    distanceValue:"",
    //ドロップダウンの表示false：隠す、trueで表示
		showDistanceDropdown: false,
		showCategoryDropdown: false,
		showAddressDropdown: false,
		showStationDropdown: false,
		
		//表示１回目のエラー回避
		firstAction:0,
		
		//距離のドロップダウンタブの表示変更
		distanceValue: "",
		
		//距離の選択
		distanceOptions: [{distance:'500m',value:"0.5"}, {distance:'1km',value:"1"}, {distance:'3km',value:"3"}],
		
		//タブのカテゴリーの種類(googlemapに備わってるカテゴリーなら追加可能)
		categoryList:store.state.categoryList,
		
		//チェックされてるカテゴリー
		selectedCategories: [],
		
		//合計評価
		totalResults:[],
		
		//近隣住所リスト
		addressOptions: [],
		
		//近隣駅リスト
		stationOptions: [],
		
		
		selectDistance: "距離選択▼",
		
		//チェック済みカテゴリーの内容を整える物
		filterdSelectedCategories: [],
		
		//チェック済みの施設の評価が無いものをはじくもの
		filterdResultsList:[],
		
      }
    },
    
mounted() { 
//初期表示で緯度経度から近隣情報を取得
　　setTimeout(() => {
　　　　　　　　axios.post('/map/getOptions', { longitude: store.state.longitude, latitude: store.state.latitude })
		.then(response => {
				const data = response.data;
				this.addressOptions = data.addressOptions;
				this.stationOptions = data.stationOptions;
				})
		 const id =document.getElementById('id').value; 
		const type =document.getElementById('infomationType').value; 
		this.fetchData(id,type);
　　}, 1000);
}, 
methods: {  
  //距離のボタンに応じて地図の倍率を変える
 emitInitializeMap(distance) {
     　store.state.ratio=distance;
   　　　this.fetchData()
    　　　　if( store.state.ratio==="0.5"){
    　　　　　　store.state.map.setZoom(16.5)
    　　 　　};
　　　　  　　if( store.state.ratio==="1"){
  　　　　　　　　　　 store.state.map.setZoom(15.5) 
  　　　　　　};
  　　　　　　if( store.state.ratio==="3"){
   　　　　　　　　store.state.map.setZoom(14.5) 
  　　　　　　};
　　　},

  //施設情報の取得
  　　fetchData(id,type) {
    　　　　axios.post('/map/getprice', { id:id,type:type })
      　　　　　　.then(response => {
        　　　　　　　　　　　　var data = response.data;
        　　　　　　　　　　　　　 store.state.priceList = data.priceList;
      　　　　　　　　　　　　　　});
      axios.post('/map/getcrime', { id:id,type:type })
      　　　　　　.then(response => {
        　　　　　　　　　　　　var data = response.data;
        　　　　　　　　　　　　　 store.state.crimeList = data.crimeList;
      　　　　　　　　　　　　　　});
  　　},
  
  //ドロップダウンの表示設定
　　　　changeDistanceDropdown() {
		　　　this.showDistanceDropdown = !this.showDistanceDropdown;
	},
	changeCategoryDropdown() {
			this.showCategoryDropdown = !this.showCategoryDropdown;
	},
	　changeOtherDropdown() {
			this.showOtherDropdown = !this.showOtherDropdown;
	},
　　　　changeAddressDropdown() {
		　　　this.showAddressDropdown = !this.showAddressDropdown;
	},	
　　　　changeStationDropdown() {
			this.showStationDropdown = !this.showStationDropdown;
	},
	
	//距離を選択するとの数が一番上のタブに表示される
　　　　selectDistanceOption(options) {
			this.distanceValue= options.value;
			this.selectDistance = options.distance + "▼";
			this.emitInitializeMap(options.value);
			this.searchNearBy();
	},
//近隣検索
searchNearBy() {
  // 初期化
  this.factoryReset();
  this.filterdSelectedCategories = this.selectedCategories;
  this.filterCategory();

  store.state.service = new google.maps.places.PlacesService(store.state.map);
  
  const promises = [];
  
  for (let i = 0; i < this.filterdSelectedCategories.length; i++) {
    const promise = new Promise((resolve, reject) => {    
      // 近隣カテゴリー検索
      store.state.service.nearbySearch({
        location: store.state.centerPosition,
        radius: store.state.ratio * 1000,
        type: this.filterdSelectedCategories[i],
      }, (results, status) => {
        if (results.length === 0) {
          resolve();
        } else {
          if (status === google.maps.places.PlacesServiceStatus.OK) {
            store.state.resultsList.push({ name: this.filterdSelectedCategories[i], list: results });
            resolve();
          } else {
            reject("近隣検索中にエラーが発生しました。");
          }
        }
      });
    });
    promises.push(promise);
  }
  
  Promise.all(promises)
    .then(() => {
      this.createMarkers();
    })
    .catch((error) => {
      console.error(error);
    });
},


//マーカー作成メソッド
  createMarkers(){
  //初回以外の処理
     if(this.firstAction>0){   
       for(let j=0;j<store.state.marker.length;j++){
       store.state.marker[j].setVisible(false);
       }
       }else{this.firstAction++;};  
    store.state.marker=[];
    
    //全ての施設数のカウント
    let totalCount=0;
    
    //評価がある物のカウント
    let count =0;
    
    //評価の平均値
    let average=0;
    
    //施設評価ランキングを作成する際のリスト（評価がないものを取り除く）
    const limitResultList=[];
    
    for (let num = 0, totalNum=0; num < store.state.resultsList.length; num++) {
    
    for (let i = 0; i < store.state.resultsList[num].list.length; i++,totalNum++) {
   
   //施設を一つのリスト化する 
    limitResultList.push(store.state.resultsList[num].list[i]);
   
   //評価がある物だけカウント、平均値を変更
    if(store.state.resultsList[num].list[i].rating==null){
    }else{
    count++;
    average=store.state.resultsList[num].list[i].rating+average;
    }
    
    //マーカーの作成
     store.state.marker[totalCount] = new google.maps.Marker({
                        text : store.state.resultsList[num].list[i].name,
						position:store.state.resultsList[num].list[i].geometry.location,
					});
	//マーカーのウインドウを作成			
					store.state.infoWindow[totalCount] = new google.maps.InfoWindow({
						content: '<div> ' +store.state.resultsList[num].list[i].name + '<div><div>'+store.state.resultsList[num].list[i].vicinity
						+'</div>',
					});
	//マーカーのクリックアクションを作成				
 store.state.marker[totalCount].addListener('click', (function (i, num, totalNum) {
      return function () {
        let stateNum = store.state.infoWindow.length;
        for (let closeNum = 0; closeNum < store.state.infoWindow.length; closeNum++) {
          store.state.infoWindow[closeNum].close();
        }
        if (num === 0) {
          store.state.infoWindow[i].open(store.state.map, store.state.marker[i]);
        } else {
          store.state.infoWindow[totalNum].open(store.state.map, store.state.marker[totalNum]);
        }
      };
    })(i, num, totalNum));

//マーカーを地図にセット
	store.state.marker[totalCount].setMap(store.state.map);
	
	//施設数カウント
	totalCount++;
	}}
	
	//評価の処理	
	if(average===0){store.state.ratingAverage=0;}else{
	store.state.ratingAverage = Math.round((average / count) * 100) / 100;}
	//評価順にリストを並び替え
　　　var filtLimitResultList = limitResultList.filter(item => item.hasOwnProperty('rating'));
	filtLimitResultList.sort((a, b) => b.rating - a.rating);
	store.state.topList=[];
	//評価上から５つを表示
	store.state.topList = filtLimitResultList.slice(0, 5);
  },
  
  //全初期化のメソッド
  retake(){
  this.selectedCategories=[];
  store.state.topList=[];
  store.state.ratingAverage=0;
     for(let j=0;j<store.state.marker.length;j++){
       store.state.marker[j].setVisible(false);
       store.state.infoWindow[j].close();
       for(let i=0;i<this.$store.state.categoryList.length;i++){
       this.$store.state.categoryList[i].view=false;
       }
     }
  },
  
  //初期化メソッド
  factoryReset(){
  //カテゴリーのチェックがある物のタブの表示を切り替える
  for(let i=0;i<this.$store.state.categoryList.length;i++){
if(this.selectedCategories.includes(this.$store.state.categoryList[i].name)){
this.$store.state.categoryList[i].view=true;
}else{this.$store.state.categoryList[i].view=false;}
};

//全ての地図上の窓を閉じる
for (closeNum = 0; closeNum < store.state.infoWindow.length; closeNum++) {
store.state.infoWindow[closeNum].close();
						};						
//検索結果の調整
store.state.resultsList = [];
  
 //選択しものがない時、評価タブを初期表示にする
 if( this.selectedCategories.length===0){
  store.state.topList=[];
  store.state.ratingAverage=0;
  }
 },
 
 //カテゴリーのフィルター処理
 filterCategory(){
   if (this.filterdSelectedCategories.includes("itemStore")) {
    this.filterdSelectedCategories = this.filterdSelectedCategories.filter(item => item !== "itemStore");
    this.filterdSelectedCategories = this.filterdSelectedCategories.concat(store.state.itemStore);
  }

  if (this.filterdSelectedCategories.includes("amusument")) {
    this.filterdSelectedCategories = this.filterdSelectedCategories.filter(item => item !== "amusument");
    this.filterdSelectedCategories = this.filterdSelectedCategories.concat(store.state.amusument);
  }
 },
 },
　　template: `<div class="top-tab"><div class="black-filter">絞り込みメニュー</div></div>
<div id="distance-drop"><button class="select-button" v-on:click="changeDistanceDropdown">{{selectDistance}}</button>
　　<transition name="tabslide">
　　　　<ul class="dropdown-menu" v-show="showDistanceDropdown">
<li v-for="distance in distanceOptions" :key="distance" v-on:click="selectDistanceOption(distance)" class="options">{{distance.distance}}</li>
</ul>
  </transition></div>


<div id="category-drop">
  <button class="select-button" v-on:click="changeCategoryDropdown">カテゴリー選択▼</button>
  <transition name="tabslide">
　　<ul class="dropdown-menu" v-show="showCategoryDropdown">
  <li  v-for="category in categoryList" :key="index" class="options">
  <label class="ECM_CheckboxInput"><input name="category" class="ECM_CheckboxInput-Input" type="checkbox" v-model="selectedCategories" :value="category.name" @change="searchNearBy"><span class="ECM_CheckboxInput-DummyInput"></span><span class="ECM_CheckboxInput-LabelText"> {{category.ja}}</span></label>
  </li>
   <li class="options"><button type="button" class="back-button" v-on:click="retake">全てのチェックを外す</button></li>
  </ul>
  </transition>
</div>

<div class="top-tab"><div class="black-filter">再検索</div></div>
    <div id="address-drop" >
      <button class="select-button" v-on:click="changeAddressDropdown">近隣地区選択▼</button>
      <transition name="tabslide5">
      <ul class="dropdown-menu"  v-show="showAddressDropdown">
        <li v-for="address in addressOptions" :key="address"   class="options">
        <form action="/map/" method="post">
          <input type="hidden" v-bind:value=address.id name="id" />
          <input type="hidden" value="address" name="infomationType"/>
          <button  class="select-button" type=submit>{{address.name}} </button>
         </form></li>
      </ul>
        </transition>
    </div>
    
    
        <div id="station-drop" >
      <button class="select-button" v-on:click="changeStationDropdown">近隣駅選択▼</button>
        <transition name="tabslide5">
      <ul class="dropdown-menu"  v-show="showStationDropdown">
        <li v-for="station in stationOptions" :key="station"   class="options">    
        <form action="/map/" method="post">
          <input type="hidden" v-bind:value=station.id name="id" />
           <input type="hidden" value="station" name="infomationType"/>
           <button  class="select-button" type=submit>{{station.name}} </button>
         </form></li>
      </ul>
      </transition>
    </div>`
		 }); 
tabMenuCreate.mount('#menu');