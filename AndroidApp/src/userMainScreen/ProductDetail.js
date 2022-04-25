import React, {useEffect, useState} from 'react';
import {Text, Center, Button, Column, ScrollView, View, Box, Row} from 'native-base';
import storeApi from '../../api/storeApi';
import {useDispatch, useSelector} from 'react-redux';
import {Alert, Dimensions, FlatList, Image, StyleSheet} from 'react-native';
const {width, height} = Dimensions.get('screen');
import CommonFunction from '../../utils/CommonFunction';
import {Icon} from 'react-native-elements';
import Counter from '../../utils/component/Counter';
import cartApi from '../../api/cartApi';
import {setCartAmount} from '../../redux/actions';
const ProductDetail = ({route}) => {
    const {itemDetail} = route.params;
    const [productAmount, setProductAmount] = useState(1);
    const userToken = useSelector(state => state.reducer.token);
    const dispatch = useDispatch();

    const checkCartAmount = async () => {
        try {
            let result = await cartApi.getCart(userToken);
            if (result.data.status == 200) {
                dispatch(setCartAmount(result.data.payload.productList.length));
            } else {
                dispatch(setCartAmount(0))
            }
        } catch (error) {
            Alert.alert('Error', error.toString(), [{text: 'OK'}]);
            console.log(error);
        }
    }

    const addToCart = async () => {
        if (await CommonFunction.addToCart(itemDetail.id, productAmount, userToken)) {
            await checkCartAmount();
        }
    }

    const getValueFromCounter = (count) => {
        setProductAmount(count);
    }


    return (
        <Column flex={1} backgroundColor={'white'}>
            <ScrollView nestedScrollEnabled={true}>
                <Center>
                    <Column marginTop={5}>
                        <FlatList style={{flex: 1}}
                                  horizontal
                                  pagingEnabled
                                  showsHorizontalScrollIndicator={false}
                                  data={itemDetail.image}
                                  renderItem={({item}) => {
                                      return (
                                          <View style={styles.item}>
                                              <Image style={styles.image} source={{uri: 'data:image/jpeg;base64,' + item.image}}/>
                                          </View>)
                                  }}
                        />
                        <Column space={5} margin={10}>
                            <Text fontSize={30}>{itemDetail.name}</Text>
                            <Text fontSize={30} color={'#ff0000'}>{CommonFunction.numberWithCommas(itemDetail.price)} Đ</Text>
                            <Text>Brand: {itemDetail.brand}</Text>
                            <Text>Category: {itemDetail.category}</Text>
                            <Text>Unit in stock: {itemDetail.unitInStock}</Text>
                            <Column height={itemDetail.decription == null ? 60 : 200} space={2}>
                                <Text>Decription:</Text>
                                <ScrollView showsVerticalScrollIndicator={false}
                                            height={200}
                                            nestedScrollEnabled={true}
                                            borderRadius={5}
                                            backgroundColor={'#e5e5e5'}
                                            paddingLeft={2}
                                            paddingRight={2}
                                >
                                    {
                                        itemDetail.decription == null ?
                                            <Text>No decription</Text> :
                                            <Text>{itemDetail.decription}</Text>
                                    }
                                </ScrollView>
                            </Column>
                        </Column>
                    </Column>
                </Center>
            </ScrollView>
            <Center>
                <Row space={2} padding={2}>
                    <Counter getValue={getValueFromCounter} maxValue={itemDetail.unitInStock}/>
                    <Button flexDirection={'row'} width={150}
                            onPress={addToCart}
                            leftIcon={<Icon color={'#ffffff'} name={'add-shopping-cart'}></Icon>}
                            backgroundColor={'#000000'}
                            _pressed={{
                                backgroundColor: '#1fd000'
                            }}
                            borderRadius={15}
                    >Add to cart</Button>
                </Row>
            </Center>
        </Column>
    )
}

const styles = StyleSheet.create({
    item: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        width
    },
    image: {
        height: width * 1,
        width: width * 0.8,
        resizeMode: 'cover'
    },
});

export default ProductDetail;
