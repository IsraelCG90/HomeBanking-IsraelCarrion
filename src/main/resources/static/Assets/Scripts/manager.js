const { createApp } = Vue;

createApp({
  data() {
    return {
        clients: [],
        clientsText: "",
        firtName:"",
        lastName:"",
        email:""
        
    };
  },

  created(){
    this.loadData();
  },

  methods:{
    loadData(){
        axios.get('http://localhost:8080/clients')
        .then(({data}) => {
            this.clients = data._embedded.clients;
            this.clientsText = data;
        })
        .catch(err => console.log(err))
    },
    addClient() {
      if (this.firstName == "" || this.lastName == "" || this.email == "") {
          alert("Debe completar los campos");
      } else {
          this.postClient();
      }
    },
    postClient(){
        axios.post('http://localhost:8080/clients', {
            firstName: this.firstName,
            lastName: this.lastName,
            email: this.email
        })
        .catch(err => console.log(err))
        this.loadData();
    }
  },
}).mount("#app");