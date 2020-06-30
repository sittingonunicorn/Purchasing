Vue.component('good-list', {
    template: '<div>List</div>'
})

var app = new Vue({
    el: '#app',
    template:'<good-list/>',
    data: {
        messages: [
            {id:'1', text:'Wow'}
        ]
    }
});