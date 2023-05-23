const app = getApp()

Component({
  data: {
    active: 4,
    list: [
      {
        url: 'pages/city/index/index',
        icon: `${app.globalData.baseUrl}/image/city_icon.png`,
        text: '城市'
      },{
        url: 'pages/quest/index/index',
        icon: `${app.globalData.baseUrl}/image/quest_icon.png`,
        text: '任务'
      }, {
        url: 'pages/index/index',
        icon: `${app.globalData.baseUrl}/image/index_icon.png`,
        text: '首页'
      }, {
        url: 'pages/achievement/index/index',
        icon: `${app.globalData.baseUrl}/image/achievement_icon.png`,
        text: '成就'
      }, {
        url: 'pages/society/index/index',
        icon: `${app.globalData.baseUrl}/image/society_icon.png`,
        text: '我的'
      }
    ]
  },

  methods: {


    onChange(event) {
      this.setData({ active: event.detail.value });
      wx.switchTab({
        url: this.data.list[event.detail.value].url.startsWith('/')
          ? this.data.list[event.detail.value].url
          : `/${this.data.list[event.detail.value].url}`,
      });
    },

    init() {
      const page = getCurrentPages().pop();
      const route = page ? page.route.split('?')[0] : '';
      const active = this.data.list.findIndex(
        (item) =>
          (item.url.startsWith('/') ? item.url.substr(1) : item.url) ===
          `${route}`,
      );
      this.setData({ active });
    },
  },
});
