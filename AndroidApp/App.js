import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {Provider, useSelector} from 'react-redux';
import {Store} from './redux/store';
import {NativeBaseProvider} from 'native-base/src/core/NativeBaseProvider';
import {LogBox} from 'react-native';
import RootComponent from './src/navigator/RootComponent';
function App() {

    LogBox.ignoreLogs([
        "[react-native-gesture-handler] Seems like you\'re using an old API with gesture components, check out new Gestures system!",
    ]);
    return (
        <NativeBaseProvider>
            <Provider store={Store}>
                <RootComponent/>
            </Provider>
        </NativeBaseProvider>
    );
}

export default App;
