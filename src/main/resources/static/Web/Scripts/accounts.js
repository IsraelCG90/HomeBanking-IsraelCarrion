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
        axios.get('/api/clients/1')
        .then( ({data}) => {
            this.accounts = data.accounts;
            this.name = data.firstName;
            this.loans = data.loans;
        })
        .catch(err => console.log(err))
    },
  },
}).mount("#app");