window.onload = function () {
    let app = new Vue({
        el: '#app',
        data: {
            balance: '',
            goods: [],
            orderGoods: [],
        },
        async mounted() {
            await this.getGoodsList();
            await this.getOrderGoodsList();
            await this.getBalance();
        },
        methods: {
            async getBalance() {
                let res = await axios.get('/balance');
                if (!res) return;
                this.balance = res.data;
            },
            async getGoodsList() {
                let res = await axios.get('/goods');
                if (!res) return;
                this.goods = res.data;
            },
            async getOrderGoodsList() {
                let res = await axios.get('/order_goods');
                if (!res) return;
                this.orderGoods = res.data;
            },
            async addGood(good) {
                await axios.post('/list/add', good);
                await this.getGoodsList();
                await this.getOrderGoodsList();
            },
            async equals(listGood, good) {
                return await axios.get('/equals', good, listGood);
            },
        }
    });
};