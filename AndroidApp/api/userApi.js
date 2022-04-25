import axios from 'axios';
import {USER_URL} from '../utils/Constant';

const userApi = {
    async login(username, pass) {
        const postData = {
            account: username,
            password: pass
        }
        let response = await axios.post(USER_URL + '/login', postData);
        return response.data;
    },

    async createUser (postData, key) {
        let response = await axios.post(USER_URL + '/createUser', postData, {
            params: {
                confirmKey: key
            }
        });
        return response.data;
    },

    async getProfile (token) {
        let response = await axios.post(USER_URL + '/getProfileUser', null,{
            headers: {
                Authorization: 'Bearer '+ token
            }}
        );
        return response.data;
    },

    async sendConfirmKey (userEmail) {
        var checkEmailRequest = {
            email: userEmail,
            confirmKey: ''
        }
        let response = await axios.post(USER_URL + '/sendConfirmKey', checkEmailRequest, {timeout: 7000});
        return response.data;
    },

    async checkConfirmKey (userEmail, key) {
        var checkEmailRequest = {
            email: userEmail,
            conFirmKey: key
        }
        let response = await axios.post(USER_URL + '/checkConfirmKey', checkEmailRequest, {timeout: 7000});
        return response.data;
    },

    async checkLoginStatus (token) {
        let response = await axios.post(USER_URL + '/checkLoginStatus', null,{
            headers: {
                Authorization: 'Bearer ' + token
            }}
        );
        return response.data;
    },

    async getAllUser (pageValue, sizeValue) {
        let response = await axios.get(USER_URL + '/getAllUser', {
            timeout: 7000,
            params: {
                page: pageValue,
                size: sizeValue
            }
        });
        return response.data;
    },
}
export default userApi;
