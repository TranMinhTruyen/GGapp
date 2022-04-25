import React, {useEffect} from 'react';
import {Image, StyleSheet, View} from 'react-native';
import {Spinner} from 'native-base';

export default function Splash({navigation}) {
    useEffect(() => {
        setTimeout(() => {
            navigation.replace('Login');
        }, 2000);
    });

    return (
        <View style={styles.body}>
            <Image style={styles.image} source={require('../../images/GGexamplelogo.jpg')} resizeMode={'stretch'}></Image>
            <Spinner size="lg" color={'#ff0000'}/>
        </View>
    );
}

const styles = StyleSheet.create({
    body: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: 'white',
    },
    image: {
        height: '50%',
        width: '90%',
    },
});
