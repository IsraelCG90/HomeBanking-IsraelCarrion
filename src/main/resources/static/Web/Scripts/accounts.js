let { createApp } = Vue;

createApp({
  data() {
    return {
      accounts: [],
      name: "",
      loans: [],
      accountType: ""
    };
  },

  created() {
    this.loadData();
  },

  methods: {
    loadData() {
      axios.get('/api/clients/current')
        .then(({ data }) => {
          this.accounts = data.accounts.sort((a, b) => b.id - a.id);
          this.name = data.firstName;
          this.loans = data.loans.sort((a, b) => b.id - a.id);
          console.log(this.loans)
        })
        .catch(err => console.log(err))
    },

    createAccount() {
      axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
        .then(() => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            iconColor: 'green',
            title: 'The account was created!',
            showConfirmButton: false,
            timer: 1500
          }), setTimeout(() => { this.loadData() }, 1800)
        })
        .catch(err => console.log(err))
    },

    deleteAccount(id) {
      Swal.fire({
        title: "Delete account",
        text: "Do you want to delete your account?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete!"
      }).then((result) => {
        if(result.isConfirmed){
          axios.post('/api/clients/current/accounts/delete', `id=${id}`)
            .then(() => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                iconColor: 'green',
                title: 'The account was deleted!',
                showConfirmButton: false,
                timer: 1500
              }), setTimeout(() => { this.loadData() }, 1800)
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
    }
    
  },
}).mount("#app");