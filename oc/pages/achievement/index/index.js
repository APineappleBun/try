import {requestUtil} from '../../../utils/requestUtil.js'

const app = getApp();


Component({
  data: {
    baseUrl: '',
    achievement: {},
    bgStyle: `--achievementBG: url(${app.globalData.baseUrl}/image/achievement_BG.png)`,
    themeList: [
      {},
    ],
  },

  methods: {
    async getAchievement() {
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: "/achievement/my/detail",
        data: {userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          achievement: result.data.achievement,
        });
        resolve(this.data.achievement);
      })
    },

    async getThemeList() {
      const result = await requestUtil({
        url: "/theme/findAll",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          themeList: result.data.themeList,
        })
        resolve(this.data.themeList)
      })
    },

    toDetail(e) {
      var themeId = e.currentTarget.dataset.id;
      wx.navigateTo({
        url: "/pages/achievement/puzzle/puzzle?themeId=" + themeId
      })
    },

    async onShow() {
      await this.getTabBar().init();
      await this.getAchievement();
      await this.getThemeList();
    },

    onLoad() {
      this.setData({
        baseUrl: app.globalData.baseUrl,
      })
    },
  }
})