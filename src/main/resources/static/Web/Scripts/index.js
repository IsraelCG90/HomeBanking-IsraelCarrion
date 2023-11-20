let { createApp } = Vue;

createApp({
  data() {
    return {
        accounts: [],
    };
  },

  created(){
    this.loadData();
  },

  methods:{
    loadData(){
        axios.get('/api/clients/current')
        .then( ({data}) => {
            this.accounts = data.accounts;
        })
        .catch(err => console.log(err))
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