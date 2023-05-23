import {requestUtil, getLogin, getUserProfile} from "../../../utils/requestUtil.js";

const app = getApp();

Component({
  data: {
    baseUrl: '',
    bgStyle: `--ucenterBG: url(${app.globalData.baseUrl}/image/ucenter_BG.png)`
  },
  methods: {

    async wxlogin(loginParam) {
      // 发送请求 获取用户的token
      const result = await requestUtil({
        url:"/user/wxlogin",
        data: loginParam,
        method:"post"
      }); // 向后端发送post请求（登录）
      console.log(result);
      return new Promise((resolve, reject) => {
        wx.showToast({
          title: '登录成功',
          icon: 'none'
        })
        if(result.statusCode == 200) { // 成功
          wx.setStorageSync('token', result.data.token);
          app.globalData.token = result.data.token;
          wx.setStorageSync('userId', result.data.userId);
          app.globalData.userId = result.data.userId;
        }
        wx.switchTab({
          url: '/pages/society/index/index',
        })
        resolve(result.data);
      })
    },

    async toLogin() {
      Promise.all([getLogin(), getUserProfile()]).then((res)=>{
        console.log(res);
        let loginParam = {
          code: res[0].code, // code
          nickname: res[1].userInfo.nickName, // 用户昵称
          avatarUrl: res[1].userInfo.avatarUrl, // 用户头像
        };
        console.log(loginParam); // 用于登录的参数
        wx.setStorageSync('userInfo', res[1].userInfo); // 更新缓存
        app.globalData.userInfo = res[1].userInfo; // 更新全局数据
        this.wxlogin(loginParam); // 向后端发起登录请求
      })
    },
  }
});
