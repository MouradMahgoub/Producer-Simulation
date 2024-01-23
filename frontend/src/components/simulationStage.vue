<template>
    
    <div class="page">
     <taskBar @addMachine="addMachine" @addQueue="addQueue" @addArrow="addArrow" @startSimulation="startSimulation" @newSimulation="newSimulation" @pauseSimulation="pauseSimulation" @replaySimulation="replaySimulation"/>

     <div class="konva-holder"></div>
    </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import Konva from 'konva';
import taskBar from './taskBar.vue'

export default {
    components: {
     taskBar,
    },
   data() {
     return {
      allMachines:[],  
      allQueues:[],  
      allArrows:[],  
      allShapes:[],
      layer:null,
      newQueueId:0,
      newMachineId:0,
      arrowSrc:null,
      arrowDest:null,

      isStarting:false,
      isPausing:false,
      isDrawingQueue:false,
      isDrawingMachine:false,
      isDrawingArrow:false,
      isStartedArrow:false,
      
     }
   },

   mounted() {
    this.createKonvaStage();
    this.openChannel();
   },

   methods:{

    addMachine(){
     this.clear();
     this.isDrawingMachine=true;
    }, 

    addQueue(){
     this.clear();
     this.isDrawingQueue=true;
    },

    addArrow(){
     this.clear();
     this.isDrawingArrow=true;
    },

    clear(){
      this.isDrawingQueue=false;
      this.isDrawingMachine=false;
      this.isDrawingArrow=false;
      this.isStartedArrow=false;
      this.isStarting=false;
      this.isPausing=false;
    },

    async createKonvaStage() {//creating stage with events (if i click a shape then i clicked the screen it will create a shape)create line in this(special case ) 
     const viewportWidth = Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0);
     const viewportHeight = Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0);

     const stageWidth = viewportWidth * (98.8 / 100); // Replace percentageWidth with your desired percentage
     const stageHeight = viewportHeight * (91 /100); // Replace percentageHeight with your desired percentage

     this.stage = new Konva.Stage({
        container: '.konva-holder',
        width: stageWidth,
        height: stageHeight,
     });

     this.layer = new Konva.Layer();
     this.stage.add(this.layer);

     let newShape={};

     this.stage.on('click', (e) => {
 
        if(this.isDrawingQueue||this.isDrawingMachine){
            newShape.x = this.stage.getPointerPosition().x;
            newShape.y = this.stage.getPointerPosition().y;
        
          if(this.isDrawingQueue){
         
            newShape.type='queue'
            newShape.fill='#A1EEBD'
            newShape.text='Q'+this.newQueueId.toString();
            this.newQueueId++;
            this.sendQueueToBack(newShape);

          }
          else if(this.isDrawingMachine){
            newShape.type='machine'
            newShape.fill='#FF6868'
            newShape.text='M'+this.newMachineId.toString();
            this.newMachineId++;
            this.sendMachineToBack(newShape);
          }
          
        }
        
       
     });
      
    },

    async  clearAndDraw() {//clear the layer and draw the array elements again using this.drawShape 
        this.layer.destroyChildren();
        this.allArrows.forEach((shape) => {
            this.drawKonvaShape(shape,'arrow');
        });
        this.allQueues.forEach((shape) => {

            this.drawKonvaShape(shape,'queue');
        });
        this.allMachines.forEach((shape) => {
            this.drawKonvaShape(shape,'machine');
        });
    },

    drawKonvaShape(shape,type) {// (print the array ) take a shape object and convert it to element of Konva and have the events of every shape(methods)
       let newShape=null;
       let textX=10;
       let textY=10;
       let ParsedShape=this.parseToKonvaBack(shape,type);
      //  if(type === 'machine'){
      //   textD=17;
      //  }
       if(type === 'machine'&&ParsedShape.fill=="#FF6868"){
        textX=18;
        textY=12;
        }
        if (type=== 'arrow') {
        newShape =  new Konva.Arrow({
                points: shape.points, // Define the start and end points of the arrow
                pointerLength: 7.5,  // Length of the pointer head
                pointerWidth: 7.5,   // Width of the pointer head
                fill: 'black',      // Fill color
                stroke: 'black',    // Stroke color
                strokeWidth: 2      // Stroke width
            });
        } 
        else if (type === 'machine') {
           
            newShape = new Konva.Circle(ParsedShape);
            newShape.attrs.stroke='black';
            newShape.attrs.strokeWidth=1;
            newShape.attrs.radius=40;
            
        }
        else if (type === 'queue') {
          newShape=new Konva.Rect(ParsedShape);
          newShape.attrs.stroke='black';
          newShape.attrs.width=80;
          newShape.attrs.height=50;
          newShape.attrs.offsetX=40, // Half of the width
          newShape.attrs.offsetY=25  // Half of the height
          newShape.attrs.strokeWidth=1;

        }
        
        let text = new Konva.Text({
            x: newShape.attrs.x-textX,
            y: newShape.attrs.y-textY,
            text: ParsedShape.text,
            fontSize: 15,
            fontFamily: 'Calibri',
            fill: 'black',
            align: 'center',
            verticalAlign: 'middle'
        });  
        
        let group = new Konva.Group();
        group.on('click', () => {
            if(this.isDrawingArrow && !this.isStartedArrow){
                this.isStartedArrow = true;
                this.arrowSrc = newShape;
            }
            else if(this.isStartedArrow){
                if((newShape.attrs.x != this.arrowSrc.attrs.x || newShape.attrs.y != this.arrowSrc.attrs.y) && newShape.className != this.arrowSrc.className){
                        
                    this.arrowDest = newShape;
                    let arrow = {type:'arrow',points: this.adjustPoints(this.arrowSrc,this.arrowDest)} // Define the start and end points of the arrow
                    this.conectShapes(arrow,this.arrowSrc,this.arrowDest);

                
                }
                
                this.isStartedArrow=false;
            }
      });

        group.add(newShape);
        group.add(text);

        this.layer.add(group);

    },

    parseToKonvaBack(shape,type){
      if(type=='arrow')
       return shape;
      let x={id:shape.id , x:parseFloat(shape.x), y:parseFloat(shape.y)}
      if(type=='queue'){
       x.text="Q"+shape.id.toString()+"\n"+shape.products.length.toString();
       x.fill="#A1EEBD";
      }
      else {
        // console.log(JSON.stringify(shape, null, 2));

        x.text="M"+shape.id.toString();
        if(shape.currentColor!=shape.defaultColor){
          x.text+="\n"+(shape.remainingTime/1000).toString();
        }
        else{
          x.text+="\n"+"Ready";
        }
        x.fill=shape.currentColor;
      }
      return x;
    },

    conectShapes(arrow,srcShape,destShape){
        const newArrow = {...arrow}; // Create a new objec(Copy)
        this.allArrows.push(newArrow);
        this.clearAndDraw();
        let source='machine';
        let destination='queue';
        let machineId=srcShape.attrs.id;
        let queueId=destShape.attrs.id;
        if(srcShape.className=='Rect'){
            let temp=source
            source=destination
            destination=temp;

            queueId=srcShape.attrs.id;
            machineId=destShape.attrs.id;
        }
        // console.log(source)
        // console.log(queueId)
        fetch('http://localhost:8080/'+source+'/connect'
            , {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                  data:{
                        queueId: queueId,
                        machineId: machineId,
                    }
                })
            }
            )
            
            .catch(error => console.error('Error changing list:', error));
    },

    sendQueueToBack(newQueue){
      
        fetch('http://localhost:8080/queue/add'
            , {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                  data:{
                        x: newQueue.x.toString(),
                        y: newQueue.y.toString(),
                        // defaultColor: newQueue.fill,
                    }
                })
            }
            )
            .then(response => response.json())
            .then(data => {
                this.allQueues = data;
                // console.log(JSON.stringify(this.allQueues, null, 2));
                this.clearAndDraw();
            })
            .catch(error => console.error('Error changing list:', error));
    },

    sendMachineToBack(newMachine){
         fetch('http://localhost:8080/machine/add'
            , {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                  data:{
                        x: newMachine.x.toString(),
                        y: newMachine.y.toString(),
                        defaultColor: newMachine.fill,
                    }
                })
            }
            )
            .then(response => response.json())
            .then(data => {
                this.allMachines = data;
                // console.log(JSON.stringify(this.allMachines, null, 2));

                this.clearAndDraw();
            })
            .catch(error => console.error('Error changing list:', error));
    },

    adjustPoints(arrowSrc,arrowDest){
        let points = [arrowSrc.attrs.x , arrowSrc.attrs.y , arrowDest.attrs.x , arrowDest.attrs.y];
        let hDiff = arrowSrc.attrs.x - arrowDest.attrs.x;
        let vDiff = arrowSrc.attrs.y - arrowDest.attrs.y;
        let dir=null;
       if(Math.abs(hDiff) > Math.abs(vDiff)){
         if(hDiff > 0)
          dir = 'left';
         else
          dir = 'right';
       } 
       else{
         if(vDiff > 0)
          dir = 'up';
         else
          dir = 'down';
       }
       switch(dir){
        case 'left':
          points[0] -= this.getDimentions(arrowSrc,dir);
          points[2] += this.getDimentions(arrowDest,dir);
        break;

        case 'right':
          points[0] += this.getDimentions(arrowSrc,dir);
          points[2] -= this.getDimentions(arrowDest,dir);
        break;
        case 'up':
          points[1] -= this.getDimentions(arrowSrc,dir);
          points[3] += this.getDimentions(arrowDest,dir);
        break;

        case 'down':
          points[1] += this.getDimentions(arrowSrc,dir);
          points[3] -= this.getDimentions(arrowDest,dir);
        break;
       }
       return points;
    },

    getDimentions(shape,dir){
        if(shape.attrs.radius == null){

          if(dir == 'right' || dir == 'left')
             return shape.attrs.width/2;
          else 
             return shape.attrs.height/2;
        }
     return shape.attrs.radius;
    },

    newSimulation(){
      this.clear();
      this.newQueueId=0;
      this.newMachineId=0;
      this.allArrows=[];
      this.allMachines=[];
      this.allQueues=[];
      this.clearAndDraw();
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send("/app/newSimulation", {}, JSON.stringify({ message: "new Simulation" }));
      }
      else {
        console.error("Not connected to WebSocket");
      }
    },
    startSimulation() {
      // this.clear()
      // this.isStarting=true;
      console.log("startSimulation");
      if(this.isPausing){
        this.resumeSimulation();
      }
      else{
        // Send a message to the '/app/start' endpoint
        if (this.stompClient && this.stompClient.connected) {
          
          this.clear()
          this.isStarting=true;
          console.log("start")
          this.stompClient.send("/app/start", {}, JSON.stringify({ message: "Start the simulation" }));
        }
        else {
          console.error("Not connected to WebSocket");
        }
      }  
    },
    
    pauseSimulation() {
      this.clear();
      console.log("pause");
      this.isPausing=true;
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send("/app/pause", {}, JSON.stringify({ message: "pause the simulation !!!!" }));
      }
      else {
        console.error("Not connected to WebSocket");
      }
    },

    resumeSimulation() {
      this.clear();
      console.log("resume");
      this.isStarting=true;
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send("/app/resume", {}, JSON.stringify({ message: "resume the simulation !!!!" }));
      }
      else {
        console.error("Not connected to WebSocket");
      }
    },
    replaySimulation(){
      this.clear();
      this.isStarting=true;
      console.log("replay");
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send("/app/replay", {}, JSON.stringify({ message: "replay the simulation !!!!" }));
      }
      else {
        console.error("Not connected to WebSocket");
      }
    },
    
    openChannel() {
      this.clear();

      const socket = new SockJS('http://localhost:8080/ws');
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, frame => {

        // Subscription to a topic
        this.stompClient.subscribe('/topic/updates', message => {
          // handle message
          const messageBody = JSON.parse(message.body);
          const machines = messageBody.machines; // Access the "machines" data
          const queues = messageBody.queues; // Access the "machines" data
          this.allMachines=machines;
          this.allQueues  =queues;
          // console.log(JSON.stringify(this.allQueues, null, 2));
          // console.log(JSON.stringify(this.allMachines, null, 2));
          if(this.isStarting){
           this.clearAndDraw();
          }
        });
      }, error => {
        console.error('Error connecting:', error);
      });
      
    }
    
   }
}
</script>

<style>
.konva-holder {
  height: 91vh;
  width:100%;
  /* border: 1px solid black; */
  background-color: #DCF2F1;
  /* padding-right:-1px  ; */
}
.page {
  border: 1px solid black;
  border-top-left-radius:10px ;
  border-top-right-radius:10px ;
}
</style>