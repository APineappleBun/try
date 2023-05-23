import {requestUtil} from '../../../utils/requestUtil'

const app = getApp();

Component({

  data: {
    currentQuest: {},
    quest: {},
    reward: {},
    bestTime: {},
    questList:[],
    baseUrl: '',
  },

  methods: {
    // 获取任务列表
    async getQuestList() {
      const result = await requestUtil({
        url: "/quest/findAll",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          questList: result.data.questList,
        })
        resolve(this.data.questList)
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

    toDetail(e) {
      var id = e.currentTarget.dataset.id;
      wx.navigateTo({
        url: "/pages/quest/newQuest/newQuest?id=" + id,
      })
    },
    
    toCurrent(e) {
      var id = e.currentTarget.dataset.id;
      console.log(id)
      wx.navigateTo({
        url: '/pages/quest/current/current?id=' + id,
      })
    },

    async onShow() {
      await this.getTabBar().init();
      await this.getQuestList();
      // 注意执行顺序可能被异步调用影响
      await this.getCurrentQuest();
    },

    onLoad() {
      this.setData({
        baseUrl: app.globalData.baseUrl,
      })
    }
  }
})
