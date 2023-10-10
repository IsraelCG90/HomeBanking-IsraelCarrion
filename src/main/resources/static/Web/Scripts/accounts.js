let { createApp } = Vue;

createApp({
  data() {
    return {
        accounts: [],
        name:"",
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
        })
        .catch(err => console.log(err))
    },
  },
}).mount("#app");