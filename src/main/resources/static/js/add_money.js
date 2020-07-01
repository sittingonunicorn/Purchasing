window.onload = function () {
    let app = new Vue({
        el: '#app',
        data: {
            balance: '',
            sum: '',
            userName:''
        },
        async mounted() {
            await this.getBalance();
            await this.getUser();
        },
        methods: {
            async getBalance() {
                let res = await axios.get('/balance');
                if (!res) return;
                this.balance = res.data;
            },
            async getUser() {
                let res = await axios.get('/get_user');
                if (!res) return;
                this.userName = res.data;
            },
            async replenishBalance() {
                await axios.post('/replenish_balance', this.sum);
                await this.getBalance();
                location.reload();
            },
        }
    });
}