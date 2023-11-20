const { createApp } = Vue;

createApp({
  data() {
    return {
        name: "",
        maxAmount: '',
        interest: '',
        inputPayments: '',
        payments: [],
    };
  },

  created(){
    
  },

  methods:{
    createLoan(){
      Swal.fire({
        title: "Create loan",
        text: "Do you want create a loan?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, create!"
      }).then((result) => {
        if(result.isConfirmed){
          axios.post('/api/loan/create', `name=${this.name}&maxAmount=${this.maxAmount}&interest=${this.interest}&payments=${this.payments}`)
            .then(() => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                iconColor: 'green',
                title: 'The loan was created!',
                showConfirmButton: false,
                timer: 1500
              }), setTimeout(() => { location.pathname = "/web/pages/loan-application.html" }, 1800)
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

    addPayments(){
      if(this.inputPayments > 0 && !this.payments.includes(this.inputPayments)){
        this.payments.push(this.inputPayments);
        this.inputPayments= ''
      }
    },

    deletePayments(){
      this.payments = []
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