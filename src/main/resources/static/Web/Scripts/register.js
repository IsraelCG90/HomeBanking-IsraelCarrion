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
                            location.pathname = "/web/pages/accounts.html"
                        })
                        .catch(err => console.log(err))
                    axios.post('/api/clients/current/accounts')
                })
                .catch((error) => {
                    console.error("Error registering user:", error);
                });
        },
    }
}).mount("#app");