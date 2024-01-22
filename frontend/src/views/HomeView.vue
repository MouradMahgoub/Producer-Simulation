<template>
  <div class="home">
    <simulationStage/>
  </div>
</template>

<script>
// @ is an alias to /src
import taskBar from '@/components/taskBar.vue'
import simulationStage from '@/components/simulationStage.vue'
export default {
  name: 'HomeView',
  components: {
    taskBar,
    simulationStage
  }
}
</script>
