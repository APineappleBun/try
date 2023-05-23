import {requestUtil} from '../../../utils/requestUtil.js'

const app = getApp()

Component({

  data: {
    puzzleId: '',
    baseUrl: '',
    puzzle: {},
    best: {},
    currentAmount: '',
    historyList: {},
    questList: {},
    bestUserQuest: '',
  },
  methods: {


    async getQuest() {
      var puzzleId = this.data.puzzleId;
      const result = await requestUtil({
        url: "/puzzle/findAllQuest",
        data: {puzzleId},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          questList: result.data.questList,
        })
        resolve(this.data.questList);
      })
    },

    async getHistory() {
      var userId = app.globalData.userId;
      var puzzleId = this.data.puzzleId;
      const result = await requestUtil({
        url: "/puzzle/my/getHistory",
        data: {userId, puzzleId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          historyList: result.data.userQuestList,
          bestUserQuest: result.data.bestUserQuest,
          currentAmount: result.data.currentAmount,
        })
        resolve(this.data.historyList);
      })
    },

    async getPuzzle() {
      var puzzleId = this.data.puzzleId;
      const result = await requestUtil({
        url: "/puzzle/detail",
        data: {puzzleId},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          puzzle: result.data.puzzle,
        })
        resolve(this.data.puzzle);
      })
    },

    toDetail(e) {
      var id = e.currentTarget.dataset.id;
      wx.navigateTo({
        url: "/pages/quest/newQuest/newQuest?id=" + id,
      })
    },

    async onShow() {
      await this.getPuzzle();
      await this.getHistory();
      await this.getQuest();
    },

    onLoad(options) {
      this.setData({
        puzzleId: new Number(options.puzzleId),
        baseUrl: app.globalData.baseUrl,
      })
    },
  }

})