import {requestUtil} from '../../../utils/requestUtil.js'

const app = getApp()

Component({
  data: {
    themeId: '',
    theme: {},
    puzzleList: [{},],
    currentAmount: '',
    totalAmount: '',
  },
  methods: {

    async getTheme() {
      var themeId = this.data.themeId;
      const result = await requestUtil({
        url: "/theme/detail",
        data: {themeId},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          theme: result.data.theme,
        })
        resolve(this.data.theme);
      })
    },

    async getPuzzleList() {
      var userId = app.globalData.userId;
      var themeId = this.data.themeId;
      const result = await requestUtil({
        url: "/theme/my/progressDetail",
        data: {userId, themeId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          puzzleList: result.data.puzzleList,
        })
        resolve(this.data.puzzleList);
      })
    },

    async getCollectProgress() {
      var themeId = this.data.themeId;
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: "/progress/my/themeProgress",
        data: {userId, themeId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          currentAmount: result.data.currentAmount,
          totalAmount: result.data.totalAmount,
        })
        resolve(this.data);
      })
    },

    toDetail(e) {
      var puzzleId = e.currentTarget.dataset.id;
      wx.navigateTo({
        url: "/pages/achievement/puzzlePart/puzzlePart?puzzleId=" + puzzleId
      })
    },

    async onShow() {
      await this.getCollectProgress();
      await this.getTheme();
      await this.getPuzzleList();
    },

    onLoad(options) { // 根据questId拿到quest相关数据
      var themeId = options.themeId;
      console.log(themeId)
      this.setData({
        themeId: new Number(themeId),
        baseUrl: app.globalData.baseUrl,
      })
    },
  }
})