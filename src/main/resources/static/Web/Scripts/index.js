let { createApp } = Vue;

createApp({
  data() {
    return {
      authenticated: false,
    };
  },

  created() {
    this.checkAuthentication();
  },

  methods: {
    checkAuthentication() {
      axios.get('/api/auth/check')
        .then(() => {
          this.authenticated = true;
        })
        .catch(err => {
          console.log(err);
          this.authenticated = false;
          localStorage.removeItem('authenticated');
        });
    },
 
    logout() {
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
              localStorage.removeItem('authenticated');
              location.pathname = "/web/index.html"
            })
        }
      });
    },

    showLoginAlert() {
      Swal.fire({
        title: "Access Denied",
        text: "You need to log in or register to access this page.",
        icon: "error",
        confirmButtonText: "Ok"
      });
    },

    checkAccess(page) {
      if (this.authenticated) {
        location.pathname = `./pages/${page}.html`;
      } else {
        this.showLoginAlert();
      }
    },

    preventDefault(event) {
      event.preventDefault();
    }

  },
}).mount("#app");