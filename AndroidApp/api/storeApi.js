import axios from 'axios';
import {PRODUCT_URL, BRAND_URL, CATEGORY_URL} from '../utils/Constant';
import qs from 'qs'

const storeApi = {
    async createProduct(postData, token) {
        return await axios.post(PRODUCT_URL + '/createProduct', postData, {
            timeout: 7000,
            headers: {
                Authorization: 'Bearer ' + token
            }
        });
    },
    async getAllProduct(pageValue, sizeValue) {
        return await axios.get(PRODUCT_URL + '/getAllProduct', {
            timeout: 7000,
            params: {
                page: pageValue,
                size: sizeValue
            }
        });
    },
    async getProductByKeyword(pageValue, sizeValue, nameValue, brandValue, categoryValue, priceValue) {
        return await axios.get(PRODUCT_URL + '/getProductByKeyword', {
            timeout: 7000,
            params: {
                page: pageValue,
                size: sizeValue,
                name: nameValue,
                brand: brandValue,
                category: categoryValue,
                price: priceValue
            }
        });
    },
    async updateProduct(productId, postData) {
        return await axios.put(PRODUCT_URL + '/updateProduct', postData, {
            timeout: 7000,
            params: {
                id: productId
            }
        });
    },
    async deleteImageOfProduct(productId, imageIdList, token) {
        return await  axios.delete(PRODUCT_URL + '/deleteImageOfProduct', {
            timeout: 7000,
            headers: {
                Authorization: 'Bearer ' + token
            },
            params: {
                productId: productId,
                imageId: imageIdList
            },
            paramsSerializer: params => {
                return qs.stringify(params, { arrayFormat: 'repeat' });
            }
        });
    }
}
export default storeApi;
