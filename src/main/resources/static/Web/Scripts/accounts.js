let { createApp } = Vue;

createApp({
  data() {
    return {
        accounts: [],
        name:"",
        loans:[]
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
            this.name = data.firstName;
            this.loans = data.loans;
            this.accounts.sort((a, b) => b.id - a.id);
        })
        .catch(err => console.log(err))
    },
    createAccount() {
      axios.post('/api/clients/current/accounts')
      .then( response => {
        this.loadData();
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