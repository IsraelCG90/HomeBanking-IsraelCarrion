let { createApp } = Vue;

createApp({
  data() {
    return {
      loans: [],
      loanSelected: 0,
      paymentsLoans: [],
      paymentSelected: 0,
      yourAccounts: [],
      accountSelected: "",
      amount: 0,
    };
  },

  created() {
    this.getLoans();
    this.loadData();
  },

  watch: {
    loanSelected(idLoan) {
      this.paymentsLoans = this.loans.find((loan) => loan.id == idLoan);
    },
  },

  methods: {
    getLoans() {
      axios.get('/api/loan')
        .then(({ data }) => {
          this.loans = data.sort((a, b) => b.id - a.id);
        })
        .catch(err => console.log(err))
    },
    loadData() {
      axios.get('/api/clients/current')
        .then(({ data }) => {
          this.yourAccounts = data.accounts.map(e => e.number);
        })
        .catch(err => console.log(err))
    },
    requestLoan() {
      axios.post('/api/loan', { id: this.loanSelected, amount: this.amount, payments: this.paymentSelected, toAccount: this.accountSelected })
        .then(() => {
          location.pathname = "/web/pages/accounts.html"
        })
        .catch(err => console.log(err))
    },
    logout() {
      axios.post('/api/logout')
        .then(() => {
          location.pathname = "/web/index.html"
        })
    }
  },

  // computed: {
  //   updatePayments() {
  //     this.paymentsLoans = this.loans.find(l => l.id == this.loanSelected);
  //     console.log(this.paymentsLoans)
  //   }
  // },

}).mount("#app");