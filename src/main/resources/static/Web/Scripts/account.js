const { createApp } = Vue;

createApp({
  data() {
    return {
      id:"",
      account: [],
    };
  },
  created(){
    this.id =  new URLSearchParams(location.search).get("id");
    this.loadAccount(this.id);
  },
  methods:{
    loadAccount(id){
        axios.get(`/api/accounts/${id}`)
        .then(({data}) => {
            this.account = data;
        })
        .catch(err => console.log(err))
    },
    dateFormat(date) {
      return moment(date).format('lll');
    },
    logout(){
      axios.post('/api/logout')
      .then( response => {
        location.pathname="/web/index.html"
      })
    }
  }
}).mount("#app");