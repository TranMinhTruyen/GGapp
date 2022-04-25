import React, {useState} from 'react';
import {Button, Input, Row, Toast, useToast} from 'native-base';
import {StyleSheet} from 'react-native';

const Counter = ({getValue, maxValue}) => {
    const [value, setValue] = useState(1);
    const warningMaxValueToast = useToast();

    const increasePress = () => {
        if (value < maxValue) {
            let newValue = value + 1
            setValue(newValue);
            getValue(newValue);
        } else {
            if (!warningMaxValueToast.isActive('max-value')){
                warningMaxValueToast.show({
                    id: 'max-value',
                    title: 'Warning',
                    status: 'warning',
                    duration: 2000,
                    description: 'Maximum is ' + maxValue + ' unit'
                });
            }
        }
    }

    const decreasePress = () => {
        if (value != 0) {
            let newValue = value - 1
            setValue(value - 1);
            getValue(newValue);
        }
    }

    return(
        <Row>
            <Input
                width={175}
                textAlign={'center'}
                fontSize={16}
                borderRadius={15}
                borderColor={'#000000'}
                InputRightElement={<Button
                    onPress={decreasePress}
                    size={'lg'}
                    _pressed={{backgroundColor: '#1fd000'}}
                    backgroundColor={'#000000'}
                    rounded="none"
                    height={'full'}
                    width={50}>-</Button>}
                InputLeftElement={<Button
                    onPress={increasePress}
                    size={'lg'}
                    _pressed={{backgroundColor: '#1fd000'}}
                    backgroundColor={'#000000'}
                    rounded="none"
                    height={'full'}
                    width={50}>+</Button>}
            >
                {value}
            </Input>
        </Row>
    )
}

const styles = StyleSheet.create({
})
export default Counter;
