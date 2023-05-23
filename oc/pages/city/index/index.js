import {requestUtil} from '../../../utils/requestUtil.js'

const db = wx.cloud.database();
const app = getApp();

Component({
  data: {
    totalAmount: '',
    currentAmount: '',
    percentage: '',
    attraction: '',
    attractionAmount: '',
    themeList: [
      {
      },
    ],
    cityText: '北京市',
    cityValue: ['北京市'],
    citys: [
      { label: '北京市', value: '北京市' },
      { label: '上海市', value: '上海市' },
      { label: '广州市', value: '广州市' },
      { label: '深圳市', value: '深圳市' },
    ],

    bgStyle: `--cityBG: url(${app.globalData.baseUrl}/image/city_BG_1.png)`,

    percentage: '',
  },
  
  methods: {
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
    onCityPicker() {
      this.setData({ cityVisible: true });
    },

    async getProgress() {
      var city = this.data.cityText;
      var userId = app.globalData.userId;
      const result = await requestUtil({
        url: "/progress/my/cityProgress",
        data: {userId, city},
        method: "POST",
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          percentage: result.data.progress,
        })
        resolve(this.data.percentage)
      })
    },

    async getAttraction() {
      var city = this.data.cityText;
      const result = await requestUtil({
        url: "/attraction/findRandomOne",
        data: {city},
      });
      return new Promise((resolve, reject) => {
        console.log(result.data)
        this.setData({
          attraction: result.data.attraction,
        })
        resolve(this.data.attraction)
      })
    },

    // async toAttraction(e) {
    //   wx.navigateTo({
    //     url: "/pages/city/attraction/attraction?attractionId=" + id
    //   })
    // },
    async onShow() {
      await this.getTabBar().init();
      await this.getAttraction();
      await this.getProgress();
    },
    onLoad() {
      this.setData({
        baseUrl: app.globalData.baseUrl
      })
      
    }
  }
})