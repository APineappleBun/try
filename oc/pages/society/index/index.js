import { requestUtil } from "../../../utils/requestUtil"

const app = getApp()

Component({

  data: {
    userInfo: '',
    recentQuestId: '',
    chatList: [{},],
    bgStyle: `--societyBG: url(${app.globalData.baseUrl}/image/society_BG_1.png)`
  },

  methods: {
    
    toMyChat() {
      wx.navigateTo({
        url: '/pages/society/mychat/mychat',
      })
    },

    toUcenter() {
      wx.navigateTo({
        url: '/pages/ucenter/index/index',
      })
    },

    async getChatList() {
      // 获取最新chatList
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: '/society/my/getRecommend',
        data: {userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        wx.setStorageSync('userInfo', result.data.userInfo);
      })
    },
    
    async getUserInfo() {
      // 获取最新userInfo
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: '/user/my/getUserInfo',
        data: {userId},
        method: "POST",
      });
      console.log(result);
      return new Promise((resolve, reject) => {
        this.setData({
          userInfo: result.data.userInfo,
        })
        if(this.data.userInfo.avatarUrl.startsWith("/avatar")) {
          var str = this.data.userInfo.avatarUrl;
          this.setData({
            ['userInfo.avatarUrl']: this.data.baseUrl + str,
          })
        }
        app.globalData.userInfo = this.data.userInfo;
        wx.setStorageSync('userInfo', this.data.userInfo);
      })
    },

    toChat(e) {
      var senderId = app.globalData.userId;
      var receiverId = e.currentTarget.dataset.id;
      wx.navigateTo({
        url: '/pages/society/chat/chat?senderId=' + senderId + '&receiverId=' + receiverId,
      })
    },

    async check() {
      return new Promise((resolve, reject) => {
        var token = wx.getStorageSync('token');
        console.log(token)
        if (token) { // 缓存中没有token
          resolve(token);
          return true;
        } else {
          resolve(token);
          return false;
        }
      })
    },

    async onShow() {
      await this.getTabBar().init();
      var isLogin = await this.check();
      console.log(isLogin);
      if (isLogin) { // 已登陆
        await this.getUserInfo();
        await this.getChatList();
      } else { // 未登录
        wx.showModal({
          title: "登录提醒",
          content: "您当前访问的页面需要登录",
          confirmText: "登录",
          cancelText: "拒绝",
          confirmColor: "#000000",
          cancelColor: "#000000",
          success(res){
            if (res.confirm){
              wx.navigateTo({
                url: '/pages/ucenter/login/login',
              })
            }
            else if (res.cancel){
              wx.showToast({
                title: "未登录禁止访问",
                icon: "error",
                mask: true
              })
            }
          }
        })
      }
    },
    
    async onLoad() {
      this.setData({
        baseUrl: app.globalData.baseUrl,
      })
    }
  }
})