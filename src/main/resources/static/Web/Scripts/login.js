let { createApp } = Vue;

createApp({
  data() {
    return {
        email: "",
        password: ""
    };
  },

  created(){
    
  },

  methods:{
    login(){
        axios.post('/api/login', `email=${this.email}&password=${this.password}`)
        .then( response => {
            console.log(response)
            location.pathname="/web/pages/accounts.html"
        })
        .catch(err => console.log(err))
    },
  },
}).mount("#app");