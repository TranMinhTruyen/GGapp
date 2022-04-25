import React, {useEffect, useState} from 'react';
import {
    ActivityIndicator,
    Alert,
    Image,
    StyleSheet,
    KeyboardAvoidingView,
    Text,
    TextInput,
    View,
} from 'react-native';
import {useDispatch, useSelector} from 'react-redux';
import {Link} from '@react-navigation/native';
import ForgotPassword from './ForgotPassword';
import userApi from '../../api/userApi';
import {setProfile, setToken} from '../../redux/actions';
import GlobalStyles from '../../utils/styles/GlobalStyles';
import {
    Center,
    FormControl,
    Input,
    Row,
    WarningOutlineIcon,
    Button,
    Container,
    Column,
    Stack, IconButton, Toast, ScrollView,
} from 'native-base';
import {Icon} from 'react-native-elements';
import CustomButton from '../../utils/component/CustomButton';

export default function Login({navigation}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [checkUsernameInvalid, setUserNameInvalid] = useState(false);
    const [checkPasswordInvalid, setPasswordInvalid] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const dispatch = useDispatch();
    const [spinner, setAction] = useState(false);

    useEffect(() =>{
        navigation.addListener('beforeRemove', (e) => {
            e.preventDefault();
        });
    }, [navigation])

    async function loginOnPressHandler() {
        if (username !== '' && password !== '') {
            setAction(true);
            try {
                let result = await userApi.login(username, password);
                if (result.statusCode === 200 && result.payload !== null && result.payload != '') {
                    setAction(false);
                    dispatch(setToken(result.payload.accessToken));
                    navigation.navigate('Base');
                } else {
                    setAction(false);
                    Toast.show({
                        title: 'Error',
                        status: 'error',
                        description: 'Username or Password is wrong!'
                    });
                }
            } catch (error) {
                setAction(false);
                Toast.show({
                    title: 'Error',
                    status: 'error',
                    description: error.toString()
                });
                console.log(error);
            }
        } else {
            Toast.show({
                title: 'Warning',
                status: 'warning',
                description: 'You must input username and password!'
            });
        }
    }

    function registerOnPressHandler() {
        navigation.navigate('Register');
    }

    const onHandleShowPassword = () => setShowPassword(!showPassword);

    return (
            <KeyboardAvoidingView style={styles.body} behavior={'position'} keyboardVerticalOffset={-150}>
                <Center>
                    <Image style={styles.image} source={require('../../images/GGexamplelogo.jpg')} resizeMode={'center'}></Image>
                    <Column style={{width:'80%', alignItems: 'center'}} space={5}>
                        <Text style={{color: '#ff0000', fontSize: 30, fontWeight: 'bold',}}>LOGIN</Text>
                        <FormControl isInvalid={checkUsernameInvalid}>
                            <Input InputLeftElement={<Icon color={'#ff0000'} name={'account-circle'} style={{marginLeft: 10}}/>}
                                   borderColor={'#000000'}
                                   variant={'rounded'}
                                   borderWidth={2}
                                   placeholder={'Username'}
                                   fontWeight={'bold'}
                                   onChangeText={(value) => setUsername(value)}
                                   onEndEditing={(e) => {
                                       if (e.nativeEvent.text !== ''){
                                           setUserNameInvalid(false);
                                       }
                                       else {
                                           setUserNameInvalid(true);
                                       }
                                   }}>
                            </Input>

                            <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                                This is required
                            </FormControl.ErrorMessage>
                        </FormControl>

                        <FormControl isInvalid={checkPasswordInvalid}>
                            <Input InputLeftElement={<Icon color={'#ff0000'} name={'lock'} style={{marginLeft: 10}}/>}
                                   InputRightElement={
                                       <IconButton
                                           icon={showPassword ?
                                               <Icon color={'#ff0000'} name={'visibility'} style={{marginRight: 10}}></Icon> :
                                               <Icon color={'#ff0000'} name={'visibility-off'} style={{marginRight: 10}}></Icon>}
                                           onPress={onHandleShowPassword}
                                           backgroundColor={'#ffffff'}></IconButton>
                                   }
                                   borderColor={'#000000'}
                                   type={showPassword ? 'text' : 'password'}
                                   variant={'rounded'}
                                   borderWidth={2}
                                   placeholder={'Password'}
                                   fontWeight={'bold'}
                                   onChangeText={(value) => setPassword(value)}
                                   onEndEditing={(e) => {
                                       if (e.nativeEvent.text !== ''){
                                           setPasswordInvalid(false);
                                       }
                                       else {
                                           setPasswordInvalid(true);
                                       }
                                   }}
                            >
                            </Input>
                            <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                                This is required
                            </FormControl.ErrorMessage>
                        </FormControl>
                        <Center>
                            <Row>
                                <CustomButton
                                    buttonStyle={GlobalStyles.button}
                                    onPressHandle={registerOnPressHandler}
                                    placeHolderName={'Register'}
                                />
                                <CustomButton
                                    buttonStyle={GlobalStyles.button}
                                    spinner={spinner}
                                    onPressHandle={loginOnPressHandler}
                                    placeHolderName={'Login'}
                                />
                            </Row>

                            <Row space={1}>
                                <Text style={{color: 'black'}}>Forgot password ?</Text>
                                <Link
                                    style={{color: 'blue', textDecorationLine: 'underline'}}
                                    to={{screen: 'ForgotPassword'}}>Click here
                                </Link>
                            </Row>
                        </Center>
                    </Column>
                </Center>
            </KeyboardAvoidingView>
    );
}

const styles = StyleSheet.create({
    body: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: 'white',
    },
    image: {
        height: 300,
        width: 300,
        marginBottom: -50
    },
});
