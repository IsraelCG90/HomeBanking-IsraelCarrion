let { createApp } = Vue;

createApp({
  data() {
    return {
      accounts:[],
      idAccount: "",
      idLoan: "",
      loan:[],
      quotas: "",
      quotasPending:"",
      arrayQuotas: [],
      paymentSelected:"",
      paymentToBe:"",
      nameLoan: "",
    };
  },

  created() {
    this.idLoan =  new URLSearchParams(location.search).get("id");
    this.getData();
  },

  methods: {
    getData(){
      axios.get('/api/clients/current')
      .then( ({data}) => {
        this.accounts = data.accounts;
        this.loan = data.loans.find(loan => loan.id == this.idLoan)
        this.quotas = (this.loan.amount / this.loan.payments)
        this.quotasPending = (this.loan.payments - this.loan.paymentsMade)
        for (let i = 0; i < this.quotasPending; i++) {
          this.arrayQuotas.push(i+1);
        }
        this.nameLoan = this.loan.name;
      })
      .catch(err => console.log(err))
    },

    postPayment(){
      axios.post('/api/loan/payment', `paymentMade=${this.paymentToBe}&paymentSelected=${this.paymentSelected}&idAccount=${this.idAccount}&idLoan=${this.idLoan}&nameLoan=${this.nameLoan}`)
      .then(() => {
        location.pathname="/web/pages/accounts.html"
      })
      .catch(err => console.log(err))
    },

    logout() {
      axios.post('/api/logout')
        .then(() => {
          location.pathname = "/web/index.html"
        })
        .catch(err => console.log(err))
    }
  },
  
  computed:{
    paymentForMade(){
      this.paymentToBe = (this.paymentSelected * this.quotas).toFixed(2)
    }
  }

}).mount("#app");