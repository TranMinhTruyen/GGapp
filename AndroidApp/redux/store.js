import { createStore, combineReducers, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import reducer from './reducers';
import {USER_LOGOUT} from './actions';

const rootReducer = combineReducers({ reducer });
export const Store = createStore(rootReducer, applyMiddleware(thunk));
