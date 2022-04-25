import {createStackNavigator} from '@react-navigation/stack';
import React from 'react';
import Base from '../userMainScreen/Base';
import ProductDetail from '../userMainScreen/ProductDetail';
import {IconButton} from 'native-base';
import {Icon} from 'react-native-elements';

const Stack = createStackNavigator();
export default function MainStack (){
    return (
        <Stack.Navigator>
            <Stack.Screen
                name="Base"
                component={Base}
                options={{headerShown: false}}
            />
            <Stack.Screen
                name="ProductDetail"
                component={ProductDetail}
                options={{
                    headerTitle: 'Product Detail',
                    headerStyle: {
                        backgroundColor: '#ff0000'
                    },
                    headerTitleStyle: {
                        color: '#ffffff'
                    },
                    headerLeft: (props) => (
                        <IconButton
                            {...props}
                            _pressed={{backgroundColor: '#ff0000'}}
                            icon={<Icon color={'#ffffff'} name='arrow-back-ios' />}
                            style={{marginLeft: 7}}
                        ></IconButton>
                    ),
                    headerTitleAlign: 'center',
                }}
            />
        </Stack.Navigator>
    )
}
