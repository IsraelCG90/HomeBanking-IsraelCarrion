let { createApp } = Vue;

createApp({
  data() {
    return {
        accounts: [],
        name:""
    };
  },

  created(){
    this.loadData();
  },

  methods:{
    loadData(){
        axios.get('/api/clients/1')
        .then( ({data}) => {
            this.accounts = data.accounts;
            this.name = data.firstName;
        })
        .catch(err => console.log(err))
    },
    // addClient() {
    //   if (this.firstName == "" || this.lastName == "" || this.email == "") {
    //       alert("Debe completar los campos");
    //   } else {
    //       this.postClient();
    //   }
    // },
    // postClient(){
    //     axios.post('http://localhost:8080/clients', {
    //         firstName: this.firstName,
    //         lastName: this.lastName,
    //         email: this.email
    //     })
    //     .catch(err => console.log(err))
    //     this.loadData();
    // }
  },
}).mount("#app");