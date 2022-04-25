export const SET_TOKEN = 'SET_TOKEN';
export const SET_PROFILE = 'SET_PROFILE';
export const USER_LOGOUT = 'USER_LOGOUT';
export const CART_AMOUNT = 'CART_AMOUNT';

export const setToken = (token) => {
    return {
        type: SET_TOKEN,
        payload: token
    }
};

export const userLogout = () => {
    return {
        type: USER_LOGOUT
    };
};

export const setProfile = (profile) => {
    return {
        type: SET_PROFILE,
        payload: profile
    };
};

export const setCartAmount = (cartAmount) => {
    return {
        type: CART_AMOUNT,
        payload: cartAmount
    };
};
