let { createApp } = Vue;

createApp({
  data() {
    return {
        credit: [],
        debit: []
    };
  },

  created(){
    this.loadData();
  },

  methods:{
    loadData(){
        axios.get('/api/clients/currents')
        .then( ({data}) => {
            this.credit = data.cards.filter(card => card.type == "CREDIT");
            this.debit = data.cards.filter(card => card.type == "DEBIT");
        })
        .catch(err => console.log(err))
    },
    dateFormat(date) {
        return moment(date).format("MM/YY");
    },
    logout(){
      axios.post('/api/logout')
      .then( response => {
        location.pathname="/web/index.html"
      })
    }
  },
}).mount("#app");