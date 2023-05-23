import { requestUtil } from "../../../utils/requestUtil"

const app = getApp()

Component({

  data: {
    userId: '',
    friendList: [],
  },
  
  methods: {
    async getFriendList() {
      var userId = this.data.userId;
      const result = await requestUtil({
        url: "/society/my/getFriendList",
        data: {userId},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          friendList: result.data.friendList,
        })
        resolve(result.data.friendList);
      })
    },

    onShow() {
      this.getFriendList();
    },

    onLoad() {
      this.setData({
        userId: app.globalData.userId,
      })
    },
  }

})