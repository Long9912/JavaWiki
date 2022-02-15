<template>
  <a-row>
    <a-col :span="24">
      <a-card>
        <a-row>
          <a-col :span="8">
            <a-statistic title="总阅读数" :value="statistic.viewCount">
              <template #suffix>
                <UserOutlined/>
              </template>
            </a-statistic>
          </a-col>
          <a-col :span="8">
            <a-statistic title="总点赞数" :value="statistic.voteCount">
              <template #suffix>
                <like-outlined/>
              </template>
            </a-statistic>
          </a-col>
          <a-col :span="8">
            <a-statistic
                title="点赞率" :value="statistic.voteCount / statistic.viewCount * 100"
                suffix="%"
                :precision="2"
                :value-style="{color:'#cf1322'}"
            >
              <template #suffix>
                <like-outlined/>
              </template>
            </a-statistic>
          </a-col>
        </a-row>
      </a-card>
    </a-col>
  </a-row>
  <br>
  <a-row :gutter="16">
    <a-col :span="12">
      <a-card>
        <a-row>
          <a-col :span="12">
            <a-statistic title="今日阅读数" :value="statistic.todayViewCount">
              <template #suffix>
                <UserOutlined/>
              </template>
            </a-statistic>
          </a-col>
          <a-col :span="12">
            <a-statistic title="今日点赞数" :value="statistic.todayVoteCount">
              <template #suffix>
                <like-outlined/>
              </template>
            </a-statistic>
          </a-col>
        </a-row>
      </a-card>
    </a-col>
    <a-col :span="12">
      <a-card>
        <a-row>
          <a-col :span="12">
            <a-statistic
                title="预计今日阅读数"
                :value="statistic.todayViewIncrease"
                :value-style="{color:'#0000ff'}"
            >
              <template #suffix>
                <UserOutlined/>
              </template>
            </a-statistic>
          </a-col>
          <a-col :span="12">
            <a-statistic
                title="预计今日阅读增长"
                :value="statistic.todayViewIncreaseRateAbs"
                suffix="%"
                :precision="2"
                :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
            >
              <template #prefix>
                <ArrowDownOutlined v-if="statistic.todayViewIncreaseRate < 0"/>
                <ArrowUpOutlined v-if="statistic.todayViewIncreaseRate >= 0"/>
              </template>
            </a-statistic>
          </a-col>
        </a-row>
      </a-card>
    </a-col>
  </a-row>
  <br>
  <a-row>
    <a-col :span="24" id="main-col">
      <div id="main" style="width:100%; height: 300px"></div>
    </a-col>
  </a-row>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";

declare let echarts:any;

export default defineComponent({
  name: "the-welcome",
  setup() {
    const statistic = ref();
    statistic.value = {};
    const getStatistic = () => {
      axios.get("/ebookSnapshot/getStatistic").then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          const statisticResp = data.content;
          statistic.value.viewCount = statisticResp[1].viewCount;
          statistic.value.voteCount = statisticResp[1].voteCount;
          statistic.value.todayViewCount = statisticResp[1].viewIncrease;
          statistic.value.todayVoteCount = statisticResp[1].voteIncrease;

          //按分钟计算时间点:已过去一天的百分比
          const now = new Date();
          const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
          //通过时间百分比与今日阅读数预计今日阅读量
          statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate));
          //获得昨天阅读数
          let yesterdayViewIncrease = statisticResp[0].viewIncrease;
          //((今天预计阅读数)减去(昨天阅读数)/昨天阅读数)得到增长或减少百分比
          //根据正负值显示红色或绿色
          statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease
                                      - yesterdayViewIncrease) / yesterdayViewIncrease * 100;
          //显示的值取绝对值
          statistic.value.todayViewIncreaseRateAbs=Math.abs(statistic.value.todayViewIncreaseRate);
        } else {
          message.error(data.message);
        }
      })
    }

    const get30DayStatistic = () => {
      axios.get("/ebookSnapshot/get30Statistic").then((response)=>{
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          const statisticList = data.content;
          init30DayStatistic(statisticList);
        }
      })
    }

    const init30DayStatistic = (list:any) => {
      // 发布生产后出现问题：切到别的页面，再切回首页，报表显示不出来
      // 解决方法：把原来的id=main的区域清空，重新初始化
      const mainDom = document.getElementById('main-col');
      if (mainDom) {
        mainDom.innerHTML = '<div id="main" style="width: 100%;height:300px;"></div>';
      }
      // 基于准备好的dom，初始化echarts实例
      const myChart = echarts.init(document.getElementById('main'));

      const xAxis = [] as string[];
      const seriesView = [] as number[];
      const seriesVote = [] as number[];
      for (let i = 0; i < list.length; i++) {
        const record = list[i];
        xAxis.push(record.date);
        seriesView.push(record.viewIncrease);
        seriesVote.push(record.voteIncrease);
      }

      myChart.setOption({
            title: {
              text: '30天趋势图'
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['每日阅读数','每日点赞数']
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            toolbox: {
              feature: {
                saveAsImage: {}
              }
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: xAxis
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '每日阅读数',
                type: 'line',
                data: seriesView,
                smooth: true
              },
              {
                name: '每日点赞数',
                type: 'line',
                data: seriesVote,
                smooth: true
              }
            ]
          }
      );
    }

    onMounted(() => {
      getStatistic();
      get30DayStatistic();
    });

    return {
      statistic
    }
  }
})
</script>

<style>

</style>
