<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  provide(){
    return{
      reload:this.reload
    }
  },
  data(){
    return{
      isShow:true
    }
  },
  methods:{
    reload(){
      this.isShow=false;
      this.$nextTick(()=>{
        this.isShow=true
      })
    }
  }
}
</script>

<style>
#app {
}
</style>
