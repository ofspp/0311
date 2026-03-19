<template>
  <div>
{{message}}
<button v-on:click="changemessage"></button>
  </div>
</template>

<script setup>
import {ref} from 'vue'

const message = ref('hello Vue')
const changemessage=()=>{
  message.value = 'Hello World!'
}
</script>

<style lang="scss" scoped>

</style>