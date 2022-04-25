import React, {useCallback, useEffect, useState} from 'react';
import {Alert, Image, StyleSheet, Text, View} from 'react-native';
import {useDispatch, useSelector} from 'react-redux';
import userApi from '../../api/userApi';
import CustomButton from '../../utils/component/CustomButton';
import {ScrollView, Skeleton, Center, Column, Row, Input, FormControl, WarningOutlineIcon, Button} from 'native-base';
import {Icon} from 'react-native-elements';
import reducer from '../../redux/reducers';
import {setProfile, userLogout} from '../../redux/actions';
import {NavigationContainer, useNavigation} from '@react-navigation/native';
import {StackActions} from '@react-navigation/native';
import GlobalStyles from '../../utils/styles/GlobalStyles';

function Profile ({navigation}) {
    const token = useSelector((state) => state.reducer.token);
    const dispatch = useDispatch();
    const [userData, setUserData] = useState(null);
    const [spinner, setAction] = useState(false);
    const navigate = useNavigation();

    useEffect( () => {
        navigation.addListener('focus', () => {
            getProfile();
        });
    }, [navigation]);

    async function getProfile() {
        setAction(true);
        try {
            let result = await userApi.getProfile(token);
            if (result.statusCode == 200) {
                setUserData(result.payload);
                setAction(false);
            } else {
                Alert.alert('Error', 'Something is wrong!', [{text: 'OK'}]);
                setAction(false);
            }
        } catch (error) {
            Alert.alert('Error', error.toString(), [{text: 'OK'}]);
            console.log(error);
        }
    }

    const logOutOnPress = () => {
        dispatch(userLogout());
    }

    return (
        <ScrollView style={{backgroundColor: 'white'}}>
            {
                userData != null && !spinner ?
                    <UserDetail userData={userData} spinner={spinner}></UserDetail> : null
            }
            <Center>
                <CustomButton
                    buttonStyle={GlobalStyles.button}
                    onPressHandle={logOutOnPress}
                    placeHolderName={'Logout'}>
                </CustomButton>
            </Center>
        </ScrollView>
    );
}

const UserDetail = ({userData, spinner}) => {
    return (
        <Column style={styles.body} space={'3'}>
            <Row space={'15'}>
                <Skeleton style={styles.imageStyle} rounded={'full'} isLoaded={!spinner}>
                    {
                        userData.image != null ?
                            <Image source={{uri: 'data:image/jpeg;base64,' + userData.image}} style={styles.imageStyle}/>
                            : <Image source={require('../../images/GGexamplelogo.jpg')} style={styles.imageStyle}/>
                    }
                </Skeleton>
                <Column style={styles.detail}>
                    <Text>{userData.firstName} {userData.lastName}</Text>
                </Column>
            </Row>
        </Column>
    );
}

const styles = StyleSheet.create({
    body: {
        flex: 1,
        justifyContent: 'center',
        width: '100%',
        padding: 20
    },
    detail: {
        flex: 3,
        justifyContent: 'center',
    },
    imageStyle: {
        width: 100,
        height: 100,
        margin: 10,
        borderColor: '#000000',
        borderRadius: 100,
        borderWidth: 1
    },
});
export default Profile;
