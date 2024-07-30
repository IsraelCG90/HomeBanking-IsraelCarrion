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
          console.log(this.loans)
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
      Swal.fire({
        title: "Apply for a loan",
        text: "Want to apply for a loan?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, request!"
      }).then((result) => {
        if (result.isConfirmed) {
          axios.post('/api/loan', { id: this.loanSelected, amount: this.amount, payments: this.paymentSelected, toAccount: this.accountSelected })
            .then(() => {
              Swal.fire({
                title: "Approved!",
                text: "Your loan was approved.",
                icon: "success"
              }), setTimeout(() => { location.pathname = "/web/pages/accounts.html" }, 1700);
              ;
            })
            .catch(err => {
              Swal.fire({
                title: "error",
                text: err.response.data,
                icon: "error"
              });
            })
        }
      });
    },

    logout(){
      Swal.fire({
        title: "Log off",
        text: "Do you want to close your session?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, log off!"
      }).then((result) => {
        if (result.isConfirmed) {
          axios.post('/api/logout')
          .then(() => {
            location.pathname="/web/index.html"
          })
        }
      });
    },

    isActive(path) {
      return window.location.pathname.endsWith(path);
    }

  },
}).mount("#app");