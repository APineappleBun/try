import { requestUtil } from "../../../utils/requestUtil";
const app = getApp();

Component({
  data: {
    baseUrl: '',

    // 传入的参数
    quest_id: '',

    // reward
    isShow: true,

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

    async acceptQuest() {
      var userId = app.globalData.userId;
      var questId = this.data.quest_id;
      const result = await requestUtil({
        url: "/userQuest/my/accept",
        data: {userId, questId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        if (result.data.message == "当前已有已接受的任务") {
          wx.showToast({
            title: '失败：一次只能接受一个任务',
            icon: 'error',
          })
        } else {
          wx.showToast({
            title: '已接受任务',
            icon: 'success',
          })
        }
        console.log(result.data.userQuestId);
        resolve(result.data);
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
          percentage: result.data.message,
        })
        resolve(this.data.percentage)
      })
    },

    // 获取当前任务以及任务详情
    async getQuest() {
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
          quest: result.data.quest,
          reward: result.data.reward,
          puzzle: result.data.puzzle,
          attraction: result.data.attraction,
        })
        resolve(this.data.bestTime)
      })
    },

    async onShow() {
      await this.getQuest()  
      await this.getProgress();
    },
    onLoad: function (options) {
      var id = options.id
      console.log(id)
      this.setData({
        quest_id: new Number(id),
        baseUrl: app.globalData.baseUrl,
      })
    },
  }
  
})