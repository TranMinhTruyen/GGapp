import React, {useEffect, useState} from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import Home from './Home';
import Profile from './Profile';
import Setting from './Setting';
import Cart from './Cart';
import {Icon} from 'react-native-elements';
import {Alert, Text, TextInput, View} from 'react-native';
import {Center} from 'native-base';
import TabBarIcon from '../../utils/component/TabBarIcon';
import userApi from '../../api/userApi';
import {setCartAmount, setProfile, userLogout} from '../../redux/actions';
import {useDispatch, useSelector} from 'react-redux';
import cartApi from '../../api/cartApi';
import {useFocusEffect} from '@react-navigation/native';
import CommonFunction from '../../utils/CommonFunction';
const Tab = createBottomTabNavigator();

function Base({navigation}) {
    const userToken = useSelector(state => state.reducer.token);
    const count = useSelector(state => state.reducer.cartAmount);
    const dispatch = useDispatch();

    useEffect( () => {
        navigation.addListener('focus', async () => {
            await checkCartAmount();
            await getProfile();
        })
    }, [navigation]);

    const checkCartAmount = async () => {
        try {
            let result = await cartApi.getCart(userToken);
            if (result.data.status == 200) {
                dispatch(setCartAmount(result.data.payload.productList.length));
            } else {
                dispatch(setCartAmount(0))
            }
        } catch (error) {
            Alert.alert('Error', error.toString(), [{text: 'OK'}]);
            console.log(error);
        }
    }

    const getProfile = async () => {
        try {
            let result = await userApi.getProfile(userToken);
            if (result.statusCode == 200) {
                dispatch(setProfile(result.payload));
            } else {
                Alert.alert('Error', 'Something is wrong!', [{text: 'OK'}]);
            }
        } catch (error) {
            Alert.alert('Error', error.toString(), [{text: 'OK'}]);
            console.log(error);
        }
    }

    useEffect(() => {
        let apiInterval = setInterval(async () => {
            console.log('apiInterval')
            let loginStatus = await CommonFunction.checkLogin(userToken);
            if (!loginStatus) {
                dispatch(userLogout());
                Alert.alert('Login expire', 'Login expire please login again', [{text: 'OK'}]);
            } else {
                await checkCartAmount();
                await getProfile();
            }
        }, 2000);
        return () => {
            clearInterval(apiInterval);
        }
    }, [navigation]);

    return (
        <Tab.Navigator
            screenOptions={{
                tabBarShowLabel: false,
                tabBarHideOnKeyboard: true,
                tabBarStyle: {
                    height: 60,
                },
                headerTitleAlign: 'center',
                headerStyle: {
                    height: 50,
                    backgroundColor: '#ff0000'
                },
                headerTitleStyle: {
                    color: '#ffffff'
                }
            }} backBehavior={'none'}>
            <Tab.Screen name="Home" component={Home} options={{
                tabBarIcon: ({focused}) => (<TabBarIcon iconName={'home'} name={'Home'} focusIcon={focused}/>),
                headerShown: false
            }}/>
            <Tab.Screen name="News" component={Setting} options={{
                tabBarIcon: ({focused}) => (<TabBarIcon iconName={'speaker-notes'} name={'News'} focusIcon={focused}/>),
            }}/>
            <Tab.Screen name="Cart" component={Cart} options={{
                tabBarIcon: ({focused}) => (<TabBarIcon iconName={'shopping-cart'} name={'Cart'} focusIcon={focused}/>),
                tabBarBadge: count == 0 ? null : count,
            }}/>
            <Tab.Screen name="Notification" component={Setting} options={{
                tabBarIcon: ({focused}) => (<TabBarIcon iconName={'notifications'} name={'Notification'} focusIcon={focused}/>),
            }}/>
            <Tab.Screen name="Profile" component={Profile} options={{
                tabBarIcon: ({focused}) => (<TabBarIcon iconName={'account-circle'} name={'Profile'} focusIcon={focused}/>),
            }}/>
        </Tab.Navigator>
    );
}

export default Base;
