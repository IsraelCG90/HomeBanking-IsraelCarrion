let { createApp } = Vue;

createApp({
  data() {
    return {
        typeCard: "",
        colorCard: ""
    };
  },

  created(){
  },

  methods:{
    createCard(){
      Swal.fire({
        title: "Create card",
        text: "Want to apply for a card?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, apply!"
      }).then((result) => {
        if(result.isConfirmed){
          axios.post('/api/clients/current/cards', `cardType=${this.typeCard}&cardColor=${this.colorCard}`)
            .then(() => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                iconColor: 'green',
                title: 'The card was created.',
                showConfirmButton: false,
                timer: 1500
              }), setTimeout(() => { location.pathname="/web/pages/cards.html" }, 1800)
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