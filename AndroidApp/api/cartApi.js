import axios from 'axios';
import {CART_URL} from '../utils/Constant';

const cartApi = {
    async addProductToCart (id, amount, token) {
        return await axios.post(CART_URL + '/createCartAndAddProductToCart', null, {
            timeout: 7000,
            headers: {
                Authorization: 'Bearer ' + token
            },
            params: {
                productId: id,
                productAmount: amount
            }
        });
    },
    async getCart (token) {
        return await axios.get(CART_URL + '/getCartById', {
            timeout: 7000,
            headers: {
                Authorization: 'Bearer ' + token
            },
        });
    },
}
export default cartApi;
