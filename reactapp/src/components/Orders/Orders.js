import React, { useState, useEffect, useContext } from 'react';
import UserMenu from '../UserMenu/UserMenu';
import Order from '../Order/Order';
import { fetchUserOrders } from '../../api/orders';
import { UserContext } from '../../contexts/UserContext';

export default function Orders() {
    const { userId } = useContext(UserContext);

    const [orders, setOrders] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetchUserOrders(userId);
            setOrders(response);
        }
        fetchData();
    }, [userId]);

    return (
        <>
            <UserMenu />
            <div className='products-list'>
                { orders.length > 0 ? orders.map(order => <Order key={order.id} data={order} />) : <></> } 
            </div>
        </>
    );
}