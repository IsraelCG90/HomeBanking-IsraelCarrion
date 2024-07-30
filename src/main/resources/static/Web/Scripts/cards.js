let { createApp } = Vue;

createApp({
  data() {
    return {
        credit: [],
        debit: [],
        localDate: '',
    };
  },

  created(){
    this.loadData();
    this.createLocalDate();
  },

  methods:{
    loadData(){
        axios.get('/api/clients/current')
        .then( ({data}) => {
            this.credit = data.cards.filter(card => card.type == "CREDIT").sort((a, b) => b.id - a.id);
            this.debit = data.cards.filter(card => card.type == "DEBIT").sort((a, b) => b.id - a.id);
        })
        .catch(err => console.log(err))
    },

    dateFormat(date) {
        return moment(date).format("MM/YY");
    },

    deleteCard(id){
      Swal.fire({
        title: "Delete card",
        text: "Do you want to delete your card?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete!"
      }).then((result) => {
        if(result.isConfirmed){
          axios.post('/api/clients/current/cards/delete', `id=${id}`)
            .then(() => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                iconColor: 'green',
                title: 'The card was deleted!',
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

    createCard(){
      location.pathname="/web/pages/create-cards.html"
    },

    createLocalDate() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, "0");
      const day = String(today.getDate()).padStart(2, "0");
      this.localDate = `${year}-${month}-${day}`;
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