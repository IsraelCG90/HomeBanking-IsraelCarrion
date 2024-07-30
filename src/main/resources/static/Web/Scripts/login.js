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
            localStorage.setItem('authenticated', 'true');
            location.pathname="/web/pages/accounts.html";
        })
        .catch(err => {
          console.log(err);
          Swal.fire({
            title: 'Login Failed',
            text: 'Incorrect email or password. Please try again.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        });
    },
  },
}).mount("#app");