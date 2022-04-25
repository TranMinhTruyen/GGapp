import React, {useEffect, useState} from 'react';
import {
    StyleSheet,
    Text,
    View,
    TouchableOpacity,
    Image,
    TextInput,
    Alert,
    ScrollView,
    DatePickerAndroid, Keyboard,
} from 'react-native';
import {launchImageLibrary} from 'react-native-image-picker';
import GlobalStyles from '../../utils/styles/GlobalStyles';
import userApi from '../../api/userApi';
import {setToken} from '../../redux/actions';
import DateTimePicker from '@react-native-community/datetimepicker';
import moment from 'moment';
import {
    Center,
    FormControl,
    Input,
    Row,
    WarningOutlineIcon,
    Button,
    Container,
    Column,
    Stack, IconButton, Toast,
} from 'native-base';
import {Icon} from 'react-native-elements';
import {placeholderName} from 'react-native/template.config';
import {register} from 'react-native/Libraries/Blob/BlobRegistry';
import CustomButton from '../../utils/component/CustomButton';

function Register({navigation}) {
    const [imageData, setImage] = useState(null);
    const [datePickerShow, setDatePickerShow] = useState(false);
    const [datePickerPlaceHolder, setDatePickerPlaceHolder] = useState('Birthday');
    const [showPassword, setShowPassword] = useState(false);
    const [spinner, setAction] = useState(false);
    const [checkUsernameInvalid, setUserNameInvalid] = useState(false);
    const [checkPasswordInvalid, setPasswordInvalid] = useState(false);
    const [checkEmailInvalid, setEmailInvalid] = useState(false);
    const [checkAddressInvalid, setAddressInvalid] = useState(false);
    const [checkDistrictInvalid, setDistrictInvalid] = useState(false);
    const [checkCityInvalid, setCityInvalid] = useState(false);
    const [checkPostCodeInvalid, setPostCodeInvalid] = useState(false);
    const [checkCitizenIDInvalid, setCitizenIDInvalid] = useState(false);
    const [countDown, setCountDown] = useState(0);
    const [runTimer, setRunTimer] = useState(false);
    const [sendConfirmKey, setSendConfirmKey] = useState(false);
    const [checkConfirmKey, setCheckConfirmKey] = useState(false);
    const [confirmKey, setConfirmKey] = useState(null);
    const [showRegisterButton, setShowRegisterButton] = useState(true);
    const [postData, setPostData] = useState({
        account: '',
        password: '',
        email: '',
        role: 'ADMIN',
        active: true
    });

    const chooseFile = async (type) => {
        let options = {
            maxWidth: 500,
            maxHeight: 500,
            mediaType: type,
            includeBase64: true,
            quality: 1,
            selectionLimit: 0
        };
        await launchImageLibrary(options, (response) => {
            if (response.didCancel) {
                return;
            } else if (response.errorCode == 'camera_unavailable') {
                alert('Camera not available on device');
                return;
            } else if (response.errorCode == 'permission') {
                alert('Permission not satisfied');
                return;
            } else if (response.errorCode == 'others') {
                alert(response.errorMessage);
                return;
            }
            console.log(response.assets.length)
            setImage(response.assets[0].base64);
            setPostData(postData => ({
                ...postData,
                image: response.assets[0].base64
            }));
        });
    };

    const createUser = async () => {
        setAction(true);
        try {
            var createUser = await userApi.createUser(postData, confirmKey);
            if (createUser.status == 200) {
                setAction(false);
                Toast.show({
                    title: 'Create user success',
                    status: 'success',
                    description: 'You will be navigate to login page in 5s'
                });
                setTimeout(() => {
                    navigation.goBack();
                }, 5000);
            } else {
                setAction(false);
                Toast.show({
                    title: 'Error',
                    status: 'error',
                    description: 'Something wrong!'
                });
            }
        } catch (error) {
            Toast.show({
                title: 'Error',
                status: 'error',
                description: error.toString()
            });
        }
    }


    const registerOnPressHandler = async () => {
        setPostData(postData => ({
            ...postData,
            role: 'ADMIN',
            active: true
        }));
        if (postData.account !== '' && postData.password !== '' && postData.email !== '') {
            setAction(true);
            try {
                var result = await userApi.sendConfirmKey(postData.email);
                if (result.statusCode == 200) {
                    setSendConfirmKey(true);
                    setCheckConfirmKey(true);
                    setShowRegisterButton(false);
                    setAction(false);
                    Toast.show({
                        title: 'Success',
                        status: 'success',
                        description: 'Email has been send!'
                    });
                } else {
                    setAction(false);
                    Toast.show({
                        title: 'Error',
                        status: 'error',
                        description: 'Something wrong!'
                    });
                }
            } catch (error) {
                Toast.show({
                    title: 'Error',
                    status: 'error',
                    description: error.toString()
                });
                console.log(error);
            }
        }
        else {
            Toast.show({
                title: 'Warning',
                status: 'warning',
                description: 'You must fill all required infomation!'
            });
        }
    }

    const showDatePicker = () => {
        setDatePickerShow(true);
    };

    const onHandleShowPassword = () => setShowPassword(!showPassword);

    useEffect(() => {
        let timerId;
        if (runTimer) {
            setCountDown(45);
            timerId = setInterval(() => {
                setCountDown((countDown) => countDown - 1);
            }, 1000);
        } else {
            clearInterval(timerId);
        }
        return () => clearInterval(timerId);
    }, [runTimer]);

    useEffect(() => {
        if (countDown < 0 && runTimer) {
            setCountDown(0);
            setRunTimer(false);
        }
    }, [countDown, runTimer]);

    const togglerTimer = async () => {
        setRunTimer((t) => !t);
        registerOnPressHandler();
    }

    return (
        <ScrollView style={{backgroundColor: 'white'}}>
            <View style={styles.body}>
                <Text style={{color: '#ff0000', fontSize: 30, fontWeight: 'bold'}}>REGISTER</Text>

                <View style={{alignItems: 'center'}}>
                    <TouchableOpacity
                        activeOpacity={0.5}
                        onPress={() => chooseFile('photo')}
                    >
                        {
                            imageData != null ? <Image
                                source={{uri: 'data:image/jpeg;base64,' + imageData}}
                                style={styles.imageStyle}
                            /> : <Image
                                source={require('../../images/GGexamplelogo.jpg')}
                                style={styles.imageStyle}
                            />
                        }
                    </TouchableOpacity>
                </View>

                <Column style={{flex: 1}} justifyContent={'center'} direction={'column'} width={'100%'} alignItems={'center'} space={'3'}>
                    <FormControl isInvalid={checkUsernameInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'account-circle'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'Username'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setUserNameInvalid(false);
                                   }
                                   else {
                                       setUserNameInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       account: text
                                   }));
                               }}
                        >
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
                               type={!showPassword ? 'password' : 'text'}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'Password'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setPasswordInvalid(false);
                                   }
                                   else {
                                       setPasswordInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       password: text
                                   }));
                               }}
                        >
                        </Input>
                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>


                    <FormControl isInvalid={checkEmailInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'email'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'Email'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setEmailInvalid(false);
                                   }
                                   else {
                                       setEmailInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       email: text
                                   }));
                               }}
                        >
                        </Input>

                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>

                    <Input InputLeftElement={<Icon color={'#ff0000'} name={'person'} style={{marginLeft: 10}}/>}
                           variant={'rounded'}
                           borderWidth={2}
                           placeholder={'Firstname'}
                           fontWeight={'bold'}
                           onChangeText={(text) => {
                               setPostData(postData=> ({
                                   ...postData,
                                   firstName: text
                               }));
                           }}
                    >
                    </Input>

                    <Input InputLeftElement={<Icon color={'#ff0000'} name={'person'} style={{marginLeft: 10}}/>}
                           variant={'rounded'}
                           borderWidth={2}
                           placeholder={'Lastname'}
                           fontWeight={'bold'}
                           onChangeText={(text) => {
                               setPostData(postData=> ({
                                   ...postData,
                                   lastName: text
                               }));
                           }}
                    >
                    </Input>

                    <Input InputLeftElement={<Icon color={'#ff0000'} name={'calendar-today'} style={{marginLeft: 10}}/>}
                           InputRightElement={
                               <IconButton icon={<Icon color={'#ff0000'} name={'edit'} style={{marginRight: 10}}></Icon>}
                                   onPress={showDatePicker}
                                   backgroundColor={'#ffffff'}>
                               </IconButton>
                           }
                           variant={'rounded'}
                           onPressIn={showDatePicker}
                           borderWidth={2}
                           showSoftInputOnFocus={false}
                           placeholder={datePickerPlaceHolder}
                           fontWeight={'bold'}
                    >
                        {datePickerShow ? (
                            <DateTimePicker
                                value={new Date()}
                                is24Hour={true}
                                display="spinner"
                                onChange={(event, value) => {
                                    if (event.type === 'set'){
                                        setPostData(postData=> ({
                                            ...postData,
                                            birthDay: value
                                        }));
                                        setDatePickerPlaceHolder(moment(value).format('DD-MM-YYYY').toString());
                                        setDatePickerShow(false);
                                    }
                                }}
                            />
                        ) : null}
                    </Input>

                    <FormControl isInvalid={checkCitizenIDInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'assignment-ind'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'Citizen ID'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setCitizenIDInvalid(false);
                                   }
                                   else {
                                       setCitizenIDInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       citizenID: text
                                   }));
                               }}
                        >
                        </Input>

                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>

                    <FormControl isInvalid={checkAddressInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'home'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'Address'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setAddressInvalid(false);
                                   }
                                   else {
                                       setAddressInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       address: text
                                   }));
                               }}
                        >
                        </Input>

                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>

                    <FormControl isInvalid={checkDistrictInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'domain'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'District'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setDistrictInvalid(false);
                                   }
                                   else {
                                       setDistrictInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       district: text
                                   }));
                               }}
                        >
                        </Input>

                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>

                    <FormControl isInvalid={checkCityInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'location-city'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'City'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setCityInvalid(false);
                                   }
                                   else {
                                       setCityInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       city: text
                                   }));
                               }}
                        >
                        </Input>

                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>

                    <FormControl isInvalid={checkPostCodeInvalid}>
                        <Input InputLeftElement={<Icon color={'#ff0000'} name={'pin-drop'} style={{marginLeft: 10}}/>}
                               variant={'rounded'}
                               borderWidth={2}
                               placeholder={'Postcode'}
                               fontWeight={'bold'}
                               onEndEditing={(e) => {
                                   if (e.nativeEvent.text !== ''){
                                       setPostCodeInvalid(false);
                                   }
                                   else {
                                       setPostCodeInvalid(true);
                                   }
                               }}
                               onChangeText={(text) => {
                                   setPostData(postData=> ({
                                       ...postData,
                                       postCode: text
                                   }));
                               }}
                        >
                        </Input>

                        <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                            This is required
                        </FormControl.ErrorMessage>
                    </FormControl>

                    {
                        sendConfirmKey ?
                                <FormControl isInvalid={checkCityInvalid}>
                                    <Input InputLeftElement={<Icon color={'#ff0000'}
                                                                   type={'material-community'}
                                                                   name={'key'}
                                                                   style={{marginLeft: 10}}/>}
                                           InputRightElement={
                                            <TouchableOpacity style={{
                                                                        backgroundColor: '#ff0000',
                                                                        height: 44,
                                                                        width: 70,
                                                                        justifyContent: 'center',
                                                                        alignItems: 'center'
                                                                    }}
                                                              onPress={togglerTimer}>
                                                {
                                                    countDown <= 0 ? <Text style={{color: '#ffffff', fontWeight: 'bold'}}>Resend</Text>:
                                                    <Text style={{color: '#ffffff', fontWeight: 'bold'}}>{countDown}</Text>
                                                }
                                            </TouchableOpacity>}
                                           variant={'rounded'}
                                           borderWidth={2}
                                           placeholder={'Confirm key'}
                                           fontWeight={'bold'}
                                           borderColor={'#ff0000'}
                                           onEndEditing={(e) => {
                                               if (e.nativeEvent.text !== ''){
                                                   setConfirmKey(e.nativeEvent.text);
                                               }
                                               else {
                                                   Alert.alert('Error', 'Confirm key invalid!', [{text: 'OK'}]);
                                               }
                                           }}
                                    >
                                    </Input>

                                    <FormControl.ErrorMessage leftIcon={<WarningOutlineIcon size="xs" />}>
                                        This is required
                                    </FormControl.ErrorMessage>
                                </FormControl>
                        : null
                    }
                </Column>
                {
                    showRegisterButton ?
                        <CustomButton
                            buttonStyle={GlobalStyles.button}
                            spinner={spinner}
                            onPressHandle={registerOnPressHandler}
                            placeHolderName={'Register'}
                        /> :
                        <CustomButton
                            buttonStyle={GlobalStyles.button}
                            spinner={spinner}
                            onPressHandle={createUser}
                            placeHolderName={'Confirm'}
                        />
                }
            </View>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    textButton: {
        color: '#ffffff',
        fontSize: 20,
        fontWeight: '400',
    },
    imageStyle: {
        width: 150,
        height: 150,
        margin: 25,
        borderColor: '#ff0000',
        borderRadius: 100,
        borderWidth: 1,
    },
    body: {
        margin: 25,
        alignItems: 'center',
        flex: 1,
    },
});

export default Register;
