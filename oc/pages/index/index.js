import {requestUtil} from '../../utils/requestUtil.js'

// pages/index/index.js
wx.cloud.init()
const db = wx.cloud.database()
const app = getApp();

const addPreceedingZero = (t) => {
  return ('0' + Math.floor(t)).slice(-2);
}


Component({

  data: {
    bgStyle: `--timerBG: url(${app.globalData.baseUrl}/image/timer_BG.png)`,

    // reward
    isShow: false,
    baseUrl: '',

    // swiper
    current: 1,
    autoplay: true,
    duration: 500,
    interval: 5000,
    // swiperImageList: [],
    // swiperIdList: [],
    swiperList: [],


    // progress
    percentage: '',

    // quest
    currentQuest: {},
    quest: {},
    reward: {},

    // time
    bestTime: "暂无记录",
    timer: {
      start: '',
      isTiming: false,
      // current: 3570000,
      current: 0,
      hours: '00',
      minutes: '00',
      seconds: '00',
      // millseconds: '00',
      times: [],
    },
  },


  methods: {
    // reward
    showReward() {
      this.setData({
        isShow: true,
      })
    },
    
    hideReward() {
      this.setData({
        isShow: false,
      })
    },
  
    toCurrent(e) {
      var id = e.currentTarget.dataset.id;
      console.log(id)
      wx.navigateTo({
        url: '/pages/quest/current/current?id=' + id,
      })
    },

    // 计时器timer
    initTimer() { // 根据任务开始时间计算已用时
      return new Promise((resolve, reject) => {
        console.log(this.data.currentQuest)
        this.setData({
          ['timer.current']: new Date(new Date(new Date().getTime()+ 60*60*1000*8).toISOString()).getTime() - new Date(this.data.currentQuest.startTime).getTime(),
        })
        resolve(this.data.timer.current)
      })
    },

    startTimer: function() { // 启动计时器
      return new Promise((resolve, reject) => {
        const start = +new Date(); // 启动计时器的时间
        const previous = this.data.timer.current; // 之前已用时
        const timer = this.data.timer.timer; // 计时器
        if (timer) {
          clearInterval(timer);
        }
        const t = setInterval(() => {
          const current = +new Date() - this.data.timer.start + previous; // 现在已用时（实时变化）
          this.setData({
            ['timer.current']: current,
            // ['timer.days']: addPreceedingZero(current / 1000 / 3600 / 24),
            ['timer.hours']: addPreceedingZero(current / 1000 / 3600 % 24), // 换算小时
            ['timer.minutes']: addPreceedingZero(current / 1000 / 60 % 60), // 换算分钟
            ['timer.seconds']: addPreceedingZero(current / 1000 % 60), // 换算秒
            // ['timer.millseconds']: addPreceedingZero(current % 1000 / 10),
          });
        }, 130);
        this.setData({
          ['timer.start']: start,
          ['timer.timer']: t,
          ['timer.isTiming']: true,
        });
        resolve(this.data.timer)
      })
    },

    // 获取轮播图数据
    async getSwiperList() {
      
      const result = await requestUtil({
        url: "/quest/findSwiper",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        // var list = new Array();
        // for (var i = 0; i < result.data.swiperIdList.length; i++) {
        //   var image = {};
        //   image.url = result.data.swiperImageList[i],
        //   image.id = result.data.swiperIdList[i],
        //   list.push(image);
        // }
        this.setData({
          // swiperImageList: result.data.swiperImageList,
          // swiperIdList: result.data.swiperIdList,
          // swiperList: list,
          swiperList: result.data.swiperList,
        })
        // console.log(list)
        resolve(this.data.swiperList)
      })
    },

    onTap(e) { // 点击轮播图
      var id = e.currentTarget.dataset.id;
      wx.navigateTo({
        url: 'pages/quest/newQuest/newQuest?id=' + id,
      })
    },

    // 获取总进度
    async getProgress() {
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: "/progress/my/totalProgress",
        data: {userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          percentage: result.data.progress,
        })
        resolve(this.data.percentage)
      })
    },

    // 获取当前任务以及任务详情
    async getCurrentQuest() {
      var userId = app.globalData.userId;
      console.log(userId);
      const result = await requestUtil({
        url: "/userQuest/my/findCurrentQuest",
        data: {userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          currentQuest: result.data.userQuest,
          quest: result.data.quest,
          reward: result.data.reward,
          bestTime: result.data.bestTime,
        })
        resolve(this.data.bestTime)
      })
    },

    async onShow() {
      await this.getTabBar().init();
      await this.getCurrentQuest();
      await this.getProgress();
      await this.getSwiperList();
      await this.initTimer();
      await this.startTimer();
    },

    onLoad() {
      this.setData({
        baseUrl: app.globalData.baseUrl,
      })
    }
  },
});
