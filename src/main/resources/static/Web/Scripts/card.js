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
        axios.get('/api/clients/1')
        .then( ({data}) => {
            this.credit = data.cards.filter(card => card.type == "CREDIT");
            this.debit = data.cards.filter(card => card.type == "DEBIT");
        })
        .catch(err => console.log(err))
    },
    dateFormat(date) {
        return moment(date).format("MM/YY");
    }
  },
}).mount("#app");