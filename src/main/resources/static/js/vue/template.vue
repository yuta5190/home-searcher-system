//ヘッダー表示
const pageTop = Vue.createApp({}); 
pageTop.component('page-top-vue', { 
template: `
 <div class="text-flex">
	<div class="title">
      <h1>Home Build Guide</h1>
      </div>
      <div class="header-right">
        <a href="/">Home</a>
</div>
</div>
` }); 
pageTop.mount('#pagetop');

//フッター表示
const pageEnd = Vue.createApp({}); 
pageEnd.component('page-end-vue', { 
template: `
 <div>
    <nav>
	<a href="/">Home</a> 
	</nav>
	<p>&copy; 2023 HomeBuildGuide</p>
    </div>
` }); 
pageEnd.mount('#pageend');
