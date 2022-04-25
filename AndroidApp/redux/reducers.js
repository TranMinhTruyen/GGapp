import {CART_AMOUNT, SET_PROFILE, SET_TOKEN, USER_LOGOUT} from './actions';

const initState = {
    token: null,
    profile: null,
    cartAmount: 0,
};

function reducer(state = initState, action) {
    switch (action.type) {
        case SET_TOKEN:
            return { ...state, token: action.payload };
        case SET_PROFILE:
            return { ...state, profile: action.payload };
        case USER_LOGOUT:
            return { ...state, token: null, profile: null, cartAmount: 0 };
        case CART_AMOUNT:
            return { ...state, cartAmount: action.payload };
        default:
            return state;
    }
}
export default reducer;
