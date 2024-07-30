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
      Swal.fire({
        title: "Make a transfer.",
        text: "Do you want to make a transfer?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, transfer!"
      }).then((result) => {
        if(result.isConfirmed){
          axios.post('/api/clients/current/transactions', `amount=${this.amount}&description=${this.description}&originAccount=${this.mySelectAccount}&destinationAccount=${this.toAccount}`)
            .then(() => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                iconColor: 'green',
                title: 'The transfer has been made!',
                showConfirmButton: false,
                timer: 1500
              }), setTimeout(() => { location.pathname="/web/pages/accounts.html" }, 1800)
            })
            .catch(err => {
              Swal.fire({
                title: "error",
                text: err.response.data,
                icon: "error"
              });
            })
        }
      })
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
    },

  },

  computed:{
    filterAccounts(){
      this.toMyAccounts = this.fromAccounts.filter(e => e !== this.mySelectAccount);
      console.log(this.toMyAccounts)
    }
  }

}).mount("#app");