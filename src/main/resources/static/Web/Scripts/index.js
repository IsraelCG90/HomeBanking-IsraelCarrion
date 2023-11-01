let { createApp } = Vue;

createApp({
  data() {
    return {
        accounts: [],
    };
  },

  created(){
    this.loadData();
  },

  methods:{
    loadData(){
        axios.get('/api/clients/current')
        .then( ({data}) => {
            this.accounts = data.accounts;
        })
        .catch(err => console.log(err))
    },
    logout(){
      axios.post('/api/logout')
      .then( response => {
        location.pathname="/web/index.html"
      })
    }
  },
}).mount("#app");