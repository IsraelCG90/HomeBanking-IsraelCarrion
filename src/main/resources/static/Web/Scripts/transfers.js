let { createApp } = Vue;

createApp({
  data() {
    return {
      toOrFrom: "",
      fromAccounts: [],
      mySelectAccount: "",
      toMyAccounts: [],
      toAccount: "",
      amount: 0,
      description: "",
    };
  },

  created() {
    this.getToAccounts();
  },

  methods: {
    getToAccounts() {
      axios.get('/api/clients/current/accounts')
        .then(({data}) => {
          this.fromAccounts = data.map(e => e.number);
        })
        .catch(err => console.log(err))
    },
    transfer() {
      axios.post('/api/clients/current/transactions', `amount=${this.amount}&description=${this.description}&originAccount=${this.mySelectAccount}&destinationAccount=${this.toAccount}`)
      .then( () => {
        location.pathname="/web/pages/accounts.html"
      })
      .catch(err => console.log(err))
    },
    logout() {
      axios.post('/api/logout')
        .then( () => {
          location.pathname = "/web/index.html"
        })
        .catch(err => console.log(err))
    }
  },
  computed:{
    filterAccounts(){
      this.toMyAccounts = this.fromAccounts.filter(e => e !== this.mySelectAccount);
      console.log(this.toMyAccounts)
    }
  }

}).mount("#app");