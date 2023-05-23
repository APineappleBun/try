import {requestUtil} from '../../../utils/requestUtil.js'

const db = wx.cloud.database()
const app = getApp();

const addPreceedingZero = (t) => {
  return ('0' + Math.floor(t)).slice(-2);
}

Component({
  data: {
    baseUrl: '',

    bgStyle: `--timerBG: url(${app.globalData.baseUrl}/image/timer_BG.png)`,

    // 传入的参数
    user_quest_id: '',

    // reward
    isShow: false,

    // quest
    currentQuest: {},
    quest: {},
    reward: {},
    bestTime: {},
    questList:[],
    attraction: {},

    // timer
    timer: {
      start: '',
      isTiming: false,
      current: 0,
      hours: '00',
      minutes: '00',
      seconds: '00',
      times: [],
    },
    bestTime: '',

    // progress
    percentage: '',
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
  
    // 计时器timer
    initTimer() { // 根据任务开始时间计算已用时
      return new Promise((resolve, reject) => {
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
  
    pauseTimer: function() {
      const {
        timer,
      } = this.data.timer;
      clearInterval(timer);
      this.setData({
        ['timer.isTiming']: false,
      });
    },
  
    resetTimer: function() {
      const {
        start,
        timer,
      } = this.data.timer;
      const end = +new Date();
      if (timer) {
        clearInterval(timer);
      }
      this.setData({
        ['timer.isTiming']: false,
        ['timer.current']: 0,
        ['timer.hours']: '00',
        ['timer.minutes']: '00',
        ['timer.seconds']: '00',
        // ['timer.millseconds']: '00',
        ['timer.start']: start,
        ['timer.end']: end,
        ['timer.times']: [],
      });
    },

    distance(lat1, lng1, lat2, lng2) {
      console.log(lat1, lng1, lat2, lng2)
      var radLat1 = lat1 * Math.PI / 180.0;
      var radLat2 = lat2 * Math.PI / 180.0;
      var a = radLat1 - radLat2;
      var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
      var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
      s = s * 6378.137;
      s = Math.round(s * 10000) / 10000;
      console.log(s)
      return s;
    },

    async complete() {
      // var userId = app.globalData.userId;
      var userQuestId = this.data.userQuestId;
      var endTimeStr = new Date(new Date().getTime()+ 60*60*1000*8).toISOString();
      console.log(endTimeStr)
      const result = await requestUtil({
        url: "/userQuest/my/complete",
        data: {userQuestId, endTimeStr},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        var current = this.data.timer.current;
        console.log(result.data)
        wx.showToast({
          icon: 'success',
          title: '任务完成',
          content: '成功完成当前任务，总用时: ' + addPreceedingZero(current / 1000 / 3600 % 24) + ':' + addPreceedingZero(current / 1000 / 60 % 60) + ':' + addPreceedingZero(current / 1000 % 60),
        })
        // this.resetTimer();
        // wx.switchTab({
        //   url: '/pages/quest/index/index',
        // })
        // resolve(this.data.timer);
      })
    },

    async tapStart() {
      var userQuestId = this.data.userQuestId;
      // var startTime = new Date(new Date(new Date().getTime()+ 60*60*1000*8).toISOString());
      var startTimeStr = new Date(new Date().getTime()+ 60*60*1000*8).toISOString();
      console.log(startTimeStr);
      // var startTime = new Date(new Date().getTime()+ 60*60*1000*8).toISOString();

      const result = await requestUtil({
        url: "/userQuest/my/start",
        data: {userQuestId, startTimeStr},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        wx.showToast({
          icon: 'success',
          title: '任务开始',
        })
        this.resetTimer();
        this.startTimer();
        this.setData({
          ['currentQuest.questState']: 1,
        })
        resolve(this.data.timer);
      })
    },

    async tapComplete() {
      this.pauseTimer();
      var lat1 = this.data.quest.destination.lat;
      var lng1 = this.data.quest.destination.lng;
      console.log(lat1);
      console.log(lng1);
      var that = this;
      wx.getLocation({
        type: 'wgs84',
        success (res) {
          console.log(res)
          var lat2 = res.latitude;
          var lng2 = res.longitude;
          if (Number(that.distance(lat1, lng1, lat2, lng2))>2){
            wx.showModal({
              title: '失败',
              content: '温馨提示：与终点的距离不能超过50米',
              success: function (res) {
                if (res.confirm) {
                  console.log('用户点击确定')
                } else if (res.cancel) {
                  console.log('用户点击取消')
                }
              }
            });
            that.startTimer();
          } else {
            that.complete();
          }
        },
      })
    },

    async tapGiveUp() {
      var userQuestId = this.data.userQuestId;
      var endTimeStr = new Date(new Date().getTime()+ 60*60*1000*8).toISOString();
      const result = await requestUtil({
        url: "/userQuest/my/giveUp",
        data: {userQuestId, endTimeStr},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result);
        wx.showToast({
          icon: 'error',
          title: '任务已放弃',
        })
        wx.switchTab({
          url: '/pages/quest/index/index',
        })
        resolve(this.data.currentQuest.questState);
      })
    },

    async getAttraction() {
      var attractionId = this.data.quest.attractionId;
      const result = await requestUtil({
        url: "/attraction/detail",
        data: {attractionId},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          attraction: result.data.attraction,
        })
        resolve(this.data.attraction)
      })
    },

    // 获取总进度
    async getProgress() {
      var userId = app.globalData.userId
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
      await this.getCurrentQuest()
      await this.getAttraction();      
      await this.getProgress();
      await this.initTimer();
      await this.startTimer();
    },
    onLoad: function (options) {
      var id = options.id
      console.log(id)
      this.setData({
        userQuestId: new Number(id),
        baseUrl: app.globalData.baseUrl,
      })
    },
  }
  
})