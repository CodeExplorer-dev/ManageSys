<template>
  <div>
    <span>{{ title }}</span>
  </div>
</template>

<script setup>
const props = defineProps({
  title: {
    type: String,
    required: true
  }
})

</script>

<style scoped>
div{
  width: 58rem;
  height: 2rem;
  margin: 10px;
  border-left: solid 7px #02A7F0;
  display: flex;
  align-items: center;

  span{
    font-size: 1.15rem;
    color: #02A7F0;
    margin-left: 10px;
  }
}
</style>