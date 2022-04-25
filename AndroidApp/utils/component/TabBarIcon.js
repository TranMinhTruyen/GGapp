import React from 'react';
import {Icon} from 'react-native-elements';
import {Text} from 'react-native';
import {Center} from 'native-base';

const TabBarIcon = (props) => {
    return (
        <Center>
            <Icon name={props.iconName} type={props.iconType} color={props.focusIcon ? '#ff0000' : '#000000'}/>
            {
                props.focusIcon ? <Text style={{fontSize: 10, color: '#ff0000'}}>{props.name}</Text> : null
            }
        </Center>
    )
}
export default TabBarIcon;
