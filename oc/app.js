// app.js
import {getBaseUrl} from "./utils/requestUtil";

App({
  onLaunch() {
    wx.cloud.init({
      env: 'oc-7gmkp1iw7501906b',
      traceUser: true,
    })

    // wx.cloud.callFunction({
    //   name: 'get_openid',
    //   success: (res) => {
    //     //获取用户openid
    //     this.globalData.user_openid = res.result.openid;
    //     console.log(this.globalData.user_openid);
    //   }
    // })

    // this.globalData.user_id = wx.getStorageSync('user_id');

    // // 展示本地存储能力
    // const logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)

    // 设置baseUrl（服务器访问地址）
    this.globalData.baseUrl = getBaseUrl();
    this.globalData.userId = wx.getStorageSync('userId');
    this.globalData.token = wx.getStorageSync('token');
  },
  globalData: {
    openid: '',
    userId: '',
    userInfo: null,
    hasUserInfo: false,
    baseUrl: '',
    token: '',
  }
})
