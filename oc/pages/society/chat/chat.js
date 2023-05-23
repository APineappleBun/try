import {requestUtil} from '../../../utils/requestUtil'
const app = getApp();
var socket = null;
Component({
  data: {
    messageList: [],
    input: '', // 输入框
    senderId: '',
    receiverId: 2,
    sender: {},
    receiver: {},
  },
  methods: {
    onChange(event) {
      this.setData({
        input: event.detail
      })
    },
    bindChange:function(res){
      this.setData({
        input: res.detail.value
      })
    },

    back:function(){
      wx.closeSocket();
      console.log('连接断开');
    },

    onLoad: function (options) {
      var that = this;

      this.setData({ // 页面传入的参数
        senderId: options.senderId,
        // receiverId: options.receiverId,
        sender: app.globalData.userInfo,
      });

      this.getUserInfo();

      var senderId = that.data.senderId;
      // 建立连接
      socket = wx.connectSocket({
        // url: 'ws://localhost:8088/websocket/' + that.data.senderId + "/" + that.data.receiverId,
        url: 'ws://localhost:8088/websocket/' + senderId,
        success: res => {
          console.info('建立连接成功');
        }
      });

      // 事件监听
      socket.onOpen(function () {
        console.info('连接打开成功');
      });

      socket.onClose(function () {
        console.info('连接关闭成功');
      });

      socket.onError(function () {
        console.info('连接报错');
      });

      // 服务器发送监听
      socket.onMessage(function (e) {
        console.info(e.data);
        var messageList = JSON.parse(e.data);
        that.setData({ 
          messageList: messageList,
        });
      });

      this.getMessage();
    },

    async getMessage() {
      var senderId = this.data.senderId;
      var receiverId = this.data.receiverId;
      const result = await requestUtil({
        url: "/chat/getMessage",
        data: {senderId, receiverId},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          messageList: result.data.messageList,
        })
        wx.pageScrollTo({
          scrollTop: 9999
        })
        resolve(result.data.messageList);
      })

    },
    // InputFocus(e) {
    //   this.setData({
    //     InputBottom: e.detail.height
    //   })
    // },

    // formMsg(e) {
    //   this.setData({
    //     content: e.detail.value.trim()
    //   })
    // },
    async getUserInfo() {
      var userId = this.data.receiverId;
      const result = await requestUtil({
        url: "/society/getUserInfo",
        data: {userId},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data);
        this.setData({
          receiver: result.data.userInfo,
        })
        resolve(result.data.userInfo);
      })
    },

    //发送消息
    async send(e) {
      if (this.data.input != null) {
        var message = {
          content: this.data.input,
          senderId: this.data.senderId,
          receiverId: this.data.receiverId,
        };
        // var userId = app.globalData.userId;
        console.log(message);
        const result = await requestUtil({
          url: "/chat/pushMessage",
          // data: {message},
          data: message,
          method: "POST",
        });
        return new Promise((resolve, reject) => {
          console.log(result.data);
          // var newList = [];
          // newList = this.data.messageList;
          // newList.push(message);
          this.setData({
            // messageList: newList,
            input: null,
          })
          wx.pageScrollTo({
            scrollTop: 9999
          })
          resolve(message);
        })
      } else {
        wx.showToast({
          title: '消息不能为空',
          icon: 'error'
        })
      }
    },

    // InputBlur(e) {
    //   this.setData({
    //     InputBottom: 0
    //   })
    // }
  }
})

