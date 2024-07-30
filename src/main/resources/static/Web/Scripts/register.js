let { createApp } = Vue;

createApp({
    data() {
        return {
            firstName: "",
            lastName: "",
            email: "",
            password: ""
        };
    },

    created() {

    },

    methods: {
        register() {
            axios.post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
                .then((response) => {
                    console.log("User registered");
                    axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                        .then(response => {
                            console.log(response)
                            localStorage.setItem('authenticated', 'true');
                            location.pathname = "/web/pages/accounts.html"
                        })
                        .catch(err => {
                            console.log(err);
                            Swal.fire({
                                icon: 'error',
                                title: 'Failed to login.',
                                text: err.response.data || 'An error occurred while signing in.'
                            });
                        });
                })
                .catch((error) => {
                    console.error("Error registering user:", error.response.data);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error registering.',
                        text: error.response.data || 'An error occurred while registering.'
                    })
                });
        },
    }
}).mount("#app");