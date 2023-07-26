//タブに情報を表示させるvue

const createTab = Vue.createApp({}); 

createTab.use(store);

createTab.component('tab-view-vue', { 

data(){return{
//カテゴリーリスト
categoryList:this.$store.state.categoryList,
}
},

methods: {
//地図の中央を移動
moveCenter(location){
 store.state.map.panTo(location)
},

//カテゴリの表示を管理(false:非表示、true:表示)
 changeStatus(num) {
    return this.$store.state.categoryList[num].view;
    },
    
   //カテゴリー検索結果取得
    returnResultsList() {
    return this.$store.state.resultsList;
    },

//地下情報取得
    returnPriceList(){
    if(this.$store.state.priceList.length===0){
   return 0;}else{
    return this.$store.state.priceList[0].currentPrice;}
    },

//犯罪情報取得
returnCrimeListNum() {
  if (!this.$store.state.crimeList || this.$store.state.crimeList.length === 0) {
    return 0;
  } else {
    return this.$store.state.crimeList.length;
  }
}
,
    
    returnCrimeList(){
    return this.$store.state.crimeList;
    },

//画像評価の設定
    returnEvalution(){
    if(this.$store.state.ratingAverage>0.1 && this.$store.state.ratingAverage<1.5){
    return 1;
    }else if(this.$store.state.ratingAverage >= 1.5 && this.$store.state.ratingAverage<2.5){
    return 2;
    }else if(this.$store.state.ratingAverage >= 2.5 && this.$store.state.ratingAverage<3.5){
    return 3;
    }else if(this.$store.state.ratingAverage >= 3.5 && this.$store.state.ratingAverage<4.5){
    return 4;
    }else if(this.$store.state.ratingAverage >= 4.5){
    return 5;
    }else{
    return 0;
    }
},  
//評価取得
returnRating(){
    return this.$store.state.ratingAverage;
},
//ランキング取得
returnTopList(){
    return this.$store.state.topList;
},
}, 
template: `
<input id="tab1" type="radio" name="tab_btn">
	<input id="tab2" type="radio" name="tab_btn">
	<input id="tab3" type="radio" name="tab_btn">
	<input id="tab4" type="radio" name="tab_btn">
	<input id="tab5" type="radio" name="tab_btn">
	<input id="tab6" type="radio" name="tab_btn">
	<input id="tab7" type="radio" name="tab_btn">
	<input id="tab8" type="radio" name="tab_btn">
	<input id="tab9" type="radio" name="tab_btn">
	<input id="tab10" type="radio" name="tab_btn">
	<input id="tab11" type="radio" name="tab_btn">
	<input id="tab12" type="radio" name="tab_btn">

	<div class="tab_area">
		<label class="tab1_label" for="tab1" >周辺情報</label>
		<label class="tab2_label" for="tab2" v-show="changeStatus(0)" >{{categoryList[0].ja}}</label>
		<label class="tab3_label" for="tab3" v-show="changeStatus(1)" >{{categoryList[1].ja}}</label>
		<label class="tab4_label" for="tab4" v-show="changeStatus(2)" >{{categoryList[2].ja}}</label>
		<label class="tab5_label" for="tab5" v-show="changeStatus(3)" >{{categoryList[3].ja}}</label>
		<label class="tab6_label" for="tab6" v-show="changeStatus(4)" >{{categoryList[4].ja}}</label>
		<label class="tab7_label" for="tab7" v-show="changeStatus(5)" >{{categoryList[5].ja}}</label>
		<label class="tab8_label" for="tab8" v-show="changeStatus(6)" >{{categoryList[6].ja}}</label>
		<label class="tab9_label" for="tab9" v-show="changeStatus(7)" >{{categoryList[7].ja}}</label>
		<label class="tab10_label" for="tab10" v-show="changeStatus(8)" >{{categoryList[8].ja}}</label>
		<label class="tab11_label" for="tab11" v-show="changeStatus(9)" >{{categoryList[9].ja}}</label>
		<label class="tab12_label" for="tab12" v-show="returnCrimeListNum()!==0 || returnPriceList()!==0">土地価格・犯罪数</label>
	
	</div>
	<div class="panel_area">
		<div id="panel1" class="tab_panel">
		<span class="rating">評価 ：　{{returnRating()}}
		 <img class="eval-img" src="/img/evalution1.png" v-show="returnRating()>0.1 && returnRating()<1.5">
  <img class="eval-img" src="/img/evalution2.png" v-show="returnRating()>=1.5 && returnRating()<2.5">
  <img class="eval-img" src="/img/evalution3.png" v-show="returnRating()>=2.5 && returnRating()<3.5">
  <img class="eval-img" src="/img/evalution4.png" v-show="returnRating()>=3.5 && returnRating()<4.5" >
  <img class="eval-img" src="/img/evalution5.png" v-show="returnRating()>=4.5 " >
		</span>
<div class="panel_text">
  <div class="rating-error" v-show="returnRating()===0">カテゴリーを選択してください</div>
  <table  v-show="returnRating()!==0">
  <br>
  <tr id="rating_data_top">
  <th class="data1">Rank</th>
  <th  class="data2">Name</th>
  <th class="data3" >Rating</th>
</tr>			
   <tr id="rating_data" v-for="(list, index) in returnTopList()">
  <td class="data1">{{index + 1}}位</td>
  <td  class="data2" v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
  <td class="data3" >{{ list.rating }}</td>
</tr>	
</table>	
</div>
</div>

   <div id="panel2" class="tab_panel" v-show="changeStatus(0)" >
	<table>
    <tr class="seach-panel-top">
      <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[0].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td class="data1" v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td class="data2">{{ list.vicinity }}</td>
          <td class="data3">{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		<div id="panel3" class="tab_panel" v-show="changeStatus(1)" >
	<table>
    <tr class="seach-panel-top">
   <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[1].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td class="data1" v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td class="data2">{{ list.vicinity }}</td>
          <td class="data3">{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>		</div>
  
		<div id="panel4" class="tab_panel"  v-show="changeStatus(2)">
		<table>
    <tr class="seach-panel-top">
        <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[2].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
  </div>
		<div id="panel5" class="tab_panel" v-show="changeStatus(3)"  >
		<table>
    <tr class="seach-panel-top">
       <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[3].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		<div id="panel6" class="tab_panel" v-show="changeStatus(4)" >
		<table>
    <tr class="seach-panel-top">
      <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[4].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		<div id="panel7" class="tab_panel" v-show="changeStatus(5)" >
		<table>
    <tr class="seach-panel-top">
      <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[5].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		<div id="panel8" class="tab_panel"  v-show="changeStatus(6)">
		<table>
    <tr class="seach-panel-top">
       <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[6].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		<div id="panel9" class="tab_panel"  v-show="changeStatus(7)">
		<table>
    <tr class="seach-panel-top">
      <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[7].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		<div id="panel10" class="tab_panel"  v-show="changeStatus(8)">
		<table>
    <tr class="seach-panel-top">
      <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[8].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>
		
		<div id="panel11" class="tab_panel"  v-show="changeStatus(9)">
		<table>
    <tr class="seach-panel-top">
       <th>Name</th>
      <th>Address</th>
      <th>Rating</th>
    </tr>
     <template v-for="result in returnResultsList()" :key="result.name">
      <template v-if="result.name === this.categoryList[9].name">
        <tr v-for="list in result.list" :key="list.name" class="seach-panel">
          <td v-on:click="moveCenter(list.geometry.location)">{{ list.name }}</td>
          <td>{{ list.vicinity }}</td>
          <td>{{list.rating}}</td>
        </tr>
      </template>
    </template>
  </table>
		</div>

		<div id="panel12" class="tab_panel" v-show="returnCrimeListNum()!==0 || returnPriceList()!==0">
		<div class="original_panel" >
		<div   v-show="returnPriceList()!==0">土地価格：{{returnPriceList()}}　円</div>
		<div   v-show="returnPriceList()===0">土地価格：登録がありません</div>
		<br>
		<div   v-show="returnCrimeListNum()>1">   周辺犯罪数　：　{{returnCrimeListNum()}}件</div>
		<div  v-show="returnCrimeListNum()===0">   周辺犯罪数　：　近くで犯罪はありません</div>
		</div>
		</div>
	</div>


	` }); 
createTab.mount('.tab_wrap');