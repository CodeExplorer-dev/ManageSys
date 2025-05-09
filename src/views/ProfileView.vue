<template>
  <TopBar :title="title"/>
  <div class="main">
    <div class="chart_container">
      <div class="chart_title">员工性别统计</div>
      <div ref="chartRef_1" class="chart"></div>
    </div>

    <div class="chart_container">
      <div class="chart_title">员工职位统计</div>
      <div ref="chartRef_2" class="chart"></div>
    </div>
  </div>

</template>

<script setup>
import TopBar from "@/components/topBar/TopBar.vue";
import {ref, onMounted, onBeforeUnmount, reactive} from 'vue';
import * as echarts from 'echarts'
import request from "@/utils/request.js";

const title = ref("员工信息统计");

const chartRef_1 = ref(null);
const chartRef_2 = ref(null);
let chartInstance_1 = ref(null)
let chartInstance_2 = ref(null)

const gender = reactive({
  male: 0,
  female: 0
})

const load1 = () => {
  request.get('employee/selectAll').then(res => {
    console.log(res.data)
    gender.male = res.data.filter(item => item.gender === 1).length
    gender.female = res.data.filter(item => item.gender === 2).length
    initChart1()
  })
}

const initChart1 = () => {
  if (!chartRef_1.value) return

  // 初始化图表
  chartInstance_1 = echarts.init(chartRef_1.value)

  // 设置图表配置
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '性别',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: gender.male, name: '男性员工' },
          { value: gender.female, name: '女性员工' }
        ]
      }
    ]
  }

  chartInstance_1.setOption(option)

  // 响应式调整
  window.addEventListener('resize', handleResize)
}

const jobCount = reactive({
  teacher: 0,
  teacher_leader: 0,
  research_leader: 0,
  student_leader: 0
})

const load2 = () => {
  request.get('employee/selectAll').then(res => {
    console.log(res.data)
    jobCount.teacher = res.data.filter(item => item.job === 1).length
    jobCount.teacher_leader = res.data.filter(item => item.job === 2).length
    jobCount.research_leader = res.data.filter(item => item.job === 3).length
    jobCount.student_leader = res.data.filter(item => item.job === 4).length
    initChart2()
  })
}

const initChart2 = () => {
  if (!chartRef_2.value) return

  // 初始化图表
  chartInstance_2 = echarts.init(chartRef_2.value)

  // 设置图表配置
  const option = {
    xAxis: {
      type: 'category',
      data: ['班主任', '讲师', '教研主管', '学生主管']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [jobCount.teacher, jobCount.teacher_leader, jobCount.research_leader, jobCount.student_leader],
        type: 'bar'
      }
    ]
  };

  chartInstance_2.setOption(option)

  // 响应式调整
  window.addEventListener('resize', handleResize)
}

const handleResize = () => {
  if (chartInstance_1) {
    chartInstance_1.resize()
  }

  if (chartInstance_2) {
    chartInstance_2.resize()
  }
}

onMounted(() => {
  load1()
  // initChart1()
  load2()
  // initChart2()
})

onBeforeUnmount(() => {
  if (chartInstance_1) {
    chartInstance_1.dispose()
  }
  if (chartInstance_2) {
    chartInstance_2.dispose()
  }
})
</script>

<style scoped>
.main{
  width: 1150px;
  display: flex;
  justify-content: space-between;
  margin-top: 2.5rem;
  margin-left: 2rem;

  .chart_container{
    width: 450px;
    height: 400px;

    .chart_title{
      font-family: "楷体", "KaiTi", "STKaiti", serif;
      font-size: 2rem;
      font-weight: 400;
      margin-bottom: 1rem;
      text-align: center;
    }

    .chart{
      width: 100%;
      height: 100%;
    }
  }
}
</style>