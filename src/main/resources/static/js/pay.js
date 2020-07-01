window.onload = function () {
    let app = new Vue({
        el: '#app',
        data: {
            balance: '',
            orderItems: [],
            sum: '',
            amount: '',
            goodId: ''
        },
        async mounted() {
            await this.getItemsList();
            await this.getSum();
            await this.getBalance();
        },
        methods: {
            async getBalance() {
                let res = await axios.get('/balance');
                if (!res) return;
                this.balance = res.data;
            },
            async getItemsList() {
                let res = await axios.get('/items_list');
                if (!res) return;
                this.orderItems = res.data;
            },
            async getSum() {
                let res = await axios.get('/sum');
                if (!res) return;
                this.sum = res.data;
            },
            async setAmount(good) {
                this.amount = prompt('Input amount', 1);
                await axios.post('/amount/' + this.amount, good);
                await this.getItemsList();
                await this.getSum();
                location.reload();
            },
            async pay() {
                await axios.post('/paid')
                    .then((response) => {
                        alert(response.data.message);
                    }).catch((error) => {
                        alert(error.response.data.error);
                    });
                await this.getItemsList();
                await this.getSum();
                location.reload();
            },
            async deleteItem(good) {
                await axios.post('/delete_item', good);
                await this.getItemsList();
                await this.getSum();
                location.reload();
            },
        }
    });
}