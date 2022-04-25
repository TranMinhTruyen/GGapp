import cartApi from '../api/cartApi';
import {Alert} from 'react-native';
import {Toast} from 'native-base';
import userApi from '../api/userApi';
import {setCartAmount, setProfile} from '../redux/actions';

const CommonFunction = {
    numberWithCommas (x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    },

    async addToCart (id, amount, userToken) {
        try {
            let result = await cartApi.addProductToCart(id, amount, userToken);
            if (result.status == 200) {
                Toast.show({
                    title: 'Suscess',
                    status: 'success',
                    description: 'Product is add to cart'
                });
                return true;
            } else {
                Alert.alert('Error', 'Something is wrong!', [{text: 'OK'}]);
                return false;
            }
        } catch (error) {
            Alert.alert('Error', error.toString(), [{text: 'OK'}]);
            console.log(error);
            return false;
        }
    },

    async checkLogin (userToken) {
        try {
            let result = await userApi.checkLoginStatus(userToken);
            if (result.statusCode == 200) {
                return true;
            }
        } catch (error) {
            console.log(error);
            return false;
        }
    }
}
export default CommonFunction;
