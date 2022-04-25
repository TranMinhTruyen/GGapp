import {NavigationContainer} from '@react-navigation/native';
import React from 'react';
import {useSelector} from 'react-redux';
import AuthStack from './AuthStack';
import MainStack from './MainStack';

export default function RootComponent () {
    const userToken = useSelector((state) => state.reducer.token);
    return (
        <NavigationContainer>
            {
                userToken == null ? <AuthStack/> : <MainStack/>
            }
        </NavigationContainer>
    )
}
