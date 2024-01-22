<template>
  <div class="hello">
    <v-btn class="btn" v-for="(but,index) in buttons" :key="index" @click="handleclick(index)" :class="{ 'clicked': clickedButton === index }" :disabled=disablebutton[index] >
      <v-icon v-if="icons[index]!==''" :color="iconcolors[index]" size="25">{{ icons[index] }}</v-icon>
      {{ but }}</v-btn>

  </div>
</template>

<script>

export default {
  
  name: 'taskBar',
  data:()=>({
    
    buttons:["play","Stop","Stop input","Replay","New Simulation", "Add Machine","Add Queue",'connect items'],
    icons:["mdi-play","mdi-stop",'mdi-pause','mdi-replay','','mdi-factory','mdi-queue-first-in-last-out','mdi-arrow-right-bold'],
    iconcolors:["green",'red','red','black','black','black','black'],
    disablebutton:Array(8).fill(false),
    clickedButton:null,
    customcolors:[],
    isActive:false,

  }),
  methods:{
    open(){
      this.isActive=true;
    },
    handleclick(index){
      if(index!==2&&index!==4){
      this.clickedButton=index;
      }
      switch(index){
        case 0:
          this.disablebutton[5]=true;
          this.disablebutton[6]=true;
          this.disablebutton[7]=true;
          this.play();
          break;
        case 1:
          this.disablebutton[5]=true;
          this.disablebutton[6]=true;
          this.disablebutton[7]=true;
          this.stop();
          break
        case 2:
        this.stopInput();
          break;
        case 3:
          this.disablebutton[5]=true;
          this.disablebutton[6]=true;
          this.disablebutton[7]=true;
          this.clickedButton=0
          this.replay();
          break;
        case 4:
        this.clickedButton=null;
          this.disablebutton[5]=false;
          this.disablebutton[6]=false;
          this.disablebutton[7]=false;
          this.newSim();
          break;
        case 5:
          this.addMachine();
          break; 
        case 6:
          this.addQueue();
          break;
        case 7:
          this.addArrow();
          break;
      }

    },
    play(){

      this.$emit('startSimulation')
    },
    stop(){
      console.log("stop");
    },
    addMachine(){
      this.$emit('addMachine');
    },
    addQueue(){
      this.$emit('addQueue');
    },
    stopInput(){
      console.log("stop input");
    },
    replay(){
      console.log("replay");
    },
    newSim(){
      this.$emit('newSimulation');
    },
    addArrow(){
      this.$emit('addArrow');
    },
    addColor(){
      this.customcolors.push()
    }

  },
 
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello{
  display: flex;
  flex-wrap: wrap; 
  justify-content: center;  
  height: 9vh;
  width: auto;
  /* background-color: grey; */
  background-color: #365486;
  gap: 7px;
  align-items: center;
  border-radius: 10px;
}
.btn:hover{
  background-color: rgb(150, 202, 255);
  background-color:#7FC7D9;
}
.btn.clicked{
  background-color: dodgerblue;
  color: black;
}
input::placeholder{
  text-align: center;
}
.input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
