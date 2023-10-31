let { createApp } = Vue;

createApp({
  data() {
    return {
    };
  },

  created() {
    
  },

  methods: {
    logout() {
      axios.post('/api/logout')
        .then( () => {
          location.pathname = "/web/index.html"
        })
        .catch(err => console.log(err))
    }
  },
  computed:{
  }

}).mount("#app");