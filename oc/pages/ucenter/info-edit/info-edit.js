import {requestUtil} from '../../../utils/requestUtil.js'

const app = getApp();

Component({
  data: {
    baseUrl: '',
    
    userInfo: '',
    genderText: null,
    genderValue: ['女'],
    genders: [
      { label: '男', value: '男' },
      { label: '女', value: '女' },
    ],
    defaultGender: "请选择性别",
    defaultAvatarUrl: "localhost:8088/image/quest_map_1.png",
    defaultNickname: "请输入昵称",
    defaultPhoneNumber: "请授权绑定手机号",
    gender: '',
    avatarUrl: '',
    nickname: '',
    phoneNumber: '',
  },
  methods: {
    // 获取最新userInfo
    async getUserInfo() {
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: '/user/my/getUserInfo',
        data: {userId},
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

    async onChooseAvatar(e) {
      const { avatarUrl } = e.detail 
      this.setData({
        avatarUrl,
      })
      await this.uploadAvatar();
    },

    // 修改头像
    async uploadAvatar() {
      wx.uploadFile({
        url: this.data.baseUrl + "/user/my/setAvatar",
        filePath: this.data.avatarUrl,
        name: "file",
        header: {
          "Content-Type": "multipart/form-data",
          "token": app.getStorageSync('token'),
        },
        formData: {
          "userId": app.globalData.userId,
        },
        success: function (res) {
          var data = res.data
          console.log(data)
        }
      })
    },

    // 修改昵称
    async setNickname() {
      var nickname = this.data.nickname;
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: "/usermy/my/setNickname",
        data: {nickname, userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        wx.showToast({
          title: '修改昵称成功',
          icon: 'success',
        })
        this.getUserInfo();
        resolve(result.data);
      })  
    },

    // 修改性别
    async setGender() {
      var gender = this.data.gender == "男" ? 0 : 1;
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: "/user/my/setGender",
        data: {gender, userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        wx.showToast({
          title: '修改性别成功',
          icon: 'success',
        })
        this.getUserInfo();
        resolve(result.data);
      })  
    },

    // 修改手机号
    async bindPhoneNumber (e) {
      var detail = e.detail;
      console.log(detail);
      if (detail.errMsg === "getPhoneNumber:ok") {
        console.log('用户同意授权');
        var code = detail.code;
        var userId = app.globalData.userId;
        const result = await requestUtil({
          url: "/user/my/setPhoneNumber",
          data: {code, userId},
          method: "POST",
        });
        return new Promise((resolve, reject) => {
          console.log(result.data)
          wx.showToast({
            title: '绑定手机号成功',
            icon: 'success',
          })
          this.getUserInfo();
          resolve(result.data);
        })  
      } else {
        console.log('用户拒绝授权');
      }
    },

    // picker
    onColumnChange(e) {
      console.log('picker pick:', e);
    },
    onPickerChange(e) {
      const { key } = e.currentTarget.dataset;
      const { value } = e.detail;

      console.log('picker change:', e.detail);
      this.setData({
        [`${key}Visible`]: false,
        [`${key}Value`]: value,
        [`${key}Text`]: value.join(' '),
      });
    },
    onPickerCancel(e) {
      const { key } = e.currentTarget.dataset;
      console.log(e, '取消');
      console.log('picker1 cancel:');
      this.setData({
        [`${key}Visible`]: false,
      });
    },
    onGenderPicker() {
      this.setData({ genderVisible: true });
    },

    clearContent() {
      this.setData({
        nameValue: '',
      });
    },

    async onLoad() {
      this.setData({
        baseUrl: app.globalData.baseUrl,
        userInfo: wx.getStorageSync('userInfo'),
      })
    },
  }
});
