window.onload = function () {
    let app = new Vue({
        el: '#app',
        data: {
            balance: '',
            sum: '',
        },
        async mounted() {
            await this.getBalance();
        },
        methods: {
            async getBalance() {
                let res = await axios.get('/balance');
                if (!res) return;
                this.balance = res.data;
            },
            async replenishBalance() {
                await axios.post('/replenish_balance', this.sum);
                await this.getBalance();
                location.reload();
            },
        }
    });
}