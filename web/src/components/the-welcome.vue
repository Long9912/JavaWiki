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
                <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
              </template>
            </a-statistic>
          </a-col>
        </a-row>
      </a-card>
    </a-col>
  </a-row>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";

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

    onMounted(() => {
      getStatistic();
    });

    return {
      statistic
    }
  }
})
</script>

<style>

</style>
