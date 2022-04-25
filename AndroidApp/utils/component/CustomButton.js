import React from 'react';
import GlobalStyles from '../styles/GlobalStyles';
import {Button, Spinner, Row, Center} from 'native-base';
import {Text} from 'react-native';

const CustomButton = (props) => {
    return (
        <Button
            style={props.buttonStyle}
            backgroundColor={!props.spinner ? '#ff0000' : '#8dff80'}
            _pressed={{
                backgroundColor: '#1fd000'
            }}
            onPress={props.onPressHandle}>
            {
                !props.spinner ? props.placeHolderName :
                    <Row space={1}>
                        <Spinner color='#000000'/>
                        <Text style={{color: '#000000'}}>{props.loadingText}</Text>
                    </Row>
            }
        </Button>
    )
}
export default CustomButton;
