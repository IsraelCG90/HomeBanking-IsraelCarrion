let { createApp } = Vue;

createApp({
  data() {
    return {
        typeCard: "",
        colorCard: ""
    };
  },

  created(){
  },

  methods:{
    createCard(){
        axios.post('/api/clients/current/cards', `cardType=${this.typeCard}&cardColor=${this.colorCard}`)
        .then( () => {
            location.pathname="/web/pages/cards.html"
        })
        .catch(err => console.log(err))
    },
    logout(){
      axios.post('/api/logout')
      .then( response => {
        location.pathname="/web/index.html"
      })
    }
  },
}).mount("#app");