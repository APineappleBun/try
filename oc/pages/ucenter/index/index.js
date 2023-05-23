import menuData from './data';
const app = getApp()

Component({
  data: {
    userInfo: {},
    menuData: menuData,
  },
  methods: {
    onClickCell({ currentTarget }) {
      const { type } = currentTarget.dataset;
      switch (type) {
        case 'edit': {
          wx.navigateTo({ url: '/pages/ucenter/info-edit/info-edit' });
          break;
        }
        case 'mine': {
          wx.navigateTo({ url: '/pages/society/mine/mine' });
        }
        case 'logout': {
          wx.showModal({
            title: '提示',
            content: '你确定要退出登录吗？',
            complete: (res) => {
              if (res.cancel) {
                wx.showToast({
                  title: '取消退出登录',
                  icon: 'none'
                })
              }
              if (res.confirm) {
                wx.clearStorageSync();
                wx.showToast({
                  title: '退出登录成功',
                  icon: 'none'
                })
                wx.redirectTo({
                  url: '/pages/society/index/index',
                })
              }
            }
          })
          break;
        }
      }
    },

    onLoad() {
      this.setData({
        userInfo: wx.getStorageSync('userInfo')
      })
    },
    
  }
})

