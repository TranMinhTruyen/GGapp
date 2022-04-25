import {createNativeStackNavigator} from '@react-navigation/native-stack';
import Splash from '../authScreen/Splash';
import Login from '../authScreen/Login';
import Register from '../authScreen/Register';
import ForgotPassword from '../authScreen/ForgotPassword';
import React from 'react';

const Stack = createNativeStackNavigator()
export default function AuthStack () {
    return (
        <Stack.Navigator>
            <Stack.Screen
                name="Splash"
                component={Splash}
                options={{headerShown: false}}
            />
            <Stack.Screen
                name="Login"
                component={Login}
                options={{headerShown: false}}
            />
            <Stack.Screen
                name="Register"
                component={Register}
                options={{headerShown: false}}
            />
            <Stack.Screen
                name="ForgotPassword"
                component={ForgotPassword}
                options={{headerShown: false}}
            />
        </Stack.Navigator>
    )
}
