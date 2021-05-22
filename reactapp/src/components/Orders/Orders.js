import React, { useState, useEffect } from 'react';
import UserMenu from '../UserMenu/UserMenu';
import Order from '../Order/Order';
import { fetchUserOrders } from '../../api/orders';

export default function Orders() {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const orders = await fetchUserOrders(localStorage.getItem('userId'));
            setOrders(orders);
        }
        fetchData();
    }, []);

    return (
        <>
            <UserMenu />
            <div className='products-list'>
                { orders.length > 0 ? orders.map(order => <Order key={order.id} data={order} />) : <></> } 
            </div>
        </>
    );
}