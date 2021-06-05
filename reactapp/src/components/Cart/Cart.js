import React, { useContext } from 'react';
import Menu from '../Menu/Menu';
import CartItem from '../CartItem/CartItem';
import { CartContext } from '../../contexts/CartContext';
import { UserContext } from '../../contexts/UserContext';
import Button from 'react-bootstrap/Button';
import { useHistory } from "react-router-dom";
import { createOrder, createOrderProduct } from '../../api/orders';
import './Cart.css';

export default function Cart(props) {
    const { cart } = useContext(CartContext);
    const { authenticated } = useContext(UserContext);
    const history = useHistory();

    const makeOrder = async () => {
        if (!authenticated) {
            history.push('/login');
            return;
        }
       
        const user = JSON.parse(localStorage.getItem('user'));
        const order = { date: new Date().toISOString().split('T')[0], userId: user.id };
        let response = await createOrder(order);

        for (let product of cart) { 
            const orderProduct = { orderId: response.orderId, productId: product.id };
            await createOrderProduct(orderProduct);
            alert('Zamówienie złożone') 
        }
    };

    return (
        <>
            <Menu />
            <div className='products-list'>
                { cart.length !== 0 ? cart.map((product, index) => <CartItem key={index} data={product} />) : <></> }
            </div>
            <div className='cart-summary'>
                { 
                    cart.length === 0 ? 
                        <></> : 
                        <span className='total-amount'>
                            <strong>Suma:</strong> {cart.map(product => product.price).reduce((a, b) => a + b, 0)} zł
                        </span> 
                }
                { 
                    cart.length === 0 ? 
                        <></> : 
                        <Button onClick={makeOrder} variant="success">
                            Zamów
                        </Button>
                }
            </div>
        </>
    );
}