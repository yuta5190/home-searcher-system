//地図の初期表示を行うVue

const setMap = Vue.createApp({}); 
setMap.use(store);

setMap.component('map-create-vue', { 

   mounted() { 
     this.$nextTick(() => { 
     //位置情報取得
     this.getPlaceData();
     
     //地図作成
     this.initializeMap(); 
     
     //地図倍率作成
     store.state.ratio =3;
      }); 
   }, 
   
   methods: { 
   //位置情報をhtmlより取得
     getPlaceData() { 
       this.$store.state.longitude =document.getElementById('longitudeInput').value; 
       this.$store.state.firstLng=document.getElementById('longitudeInput').value;
       this.$store.state.latitude =document.getElementById('latitudeInput').value; 
       this.$store.state.firstLat =document.getElementById('latitudeInput').value; 
     }, 
     
     //地図の生成
     initializeMap() { 
       let latitude = parseFloat(this.$store.state.latitude); 
       let longitude =parseFloat(this.$store.state.longitude); 
       console.log(latitude);
       console.log(longitude);
       
       //中心の位置
       this.$store.state.centerPosition = new google.maps.LatLng(this.$store.state.latitude,  this.$store.state.longitude)
     
      //地図のオプション 
       this.$store.state.option= { 
          zoom:  14,
          center: this.$store.state.centerPosition, 
          mapTypeId: 'roadmap',
       }; 
      
       this.$store.state.map = new google.maps.Map(
       document.getElementById('map'),this.$store.state.option); 
	   
	   //中心の位置にヒト型マーカーを設定
	   this.$store.state.currentLocationMarker= new google.maps.Marker({
				position: this.$store.state.centerPosition,
				icon:{url:'/img/currentLocation.png',scaledSize : new google.maps.Size(110, 110)}
			});
	  
	  //作成した地図をset
	   this.$store.state.currentLocationMarker.setMap(this.$store.state.map);
       },
       
   },
   template: `
   <div class="map"></div>`,
    }); 

setMap.mount('#map-style');