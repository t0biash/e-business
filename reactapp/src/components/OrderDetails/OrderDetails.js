import React, { useState, useEffect, useContext } from 'react';
import UserMenu from '../UserMenu/UserMenu';
import { useParams } from 'react-router-dom';
import { fetchOrderProducts } from '../../api/orders';
import { ProductsContext } from '../../contexts/ProductsContext';

export default function OrderDetails(props) {
    const { categories, products, partsManufacturers } = useContext(ProductsContext);

    const { orderId } = useParams();

    const [orderProducts, setOrderProducts] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetchOrderProducts(orderId);
            setOrderProducts(response);
        };
        fetchData();
    }, [orderId]);
    
    const orderDetailsElement = (index, productId) => {
        const product = products.filter(p => p.id === productId)[0];
        const category = categories.filter(c => c.id === product.categoryId)[0];
        const partsManufacturer = partsManufacturers.filter(pm => pm.id === product.partsManufacturerId)[0];

        return (
            <div key={index} className='list-element'>
                <h3>{product.name}</h3>
                <strong>Opis: </strong>{product.description}<br />
                <strong>Cena: </strong>{product.price} zł<br />
                <strong>Kategoria: </strong>{category.name}<br /> 
                <strong>Producent: </strong>{partsManufacturer.name}<br /><br />
                <hr />
            </div>
        );
    };

    if (orderProducts.length === 0 || products.length === 0 || categories.length === 0 || partsManufacturers.length === 0)
        return (
            <>
                <UserMenu />
            </>
        );
    else {
        const totalPrice = orderProducts.map(orderProduct => products.filter(product => product.id === orderProduct.productId)[0].price).reduce((a, b) => a + b, 0);
        return (
                <>
                    <UserMenu />
                    {
                        orderProducts.length > 0 ? 
                            orderProducts.map((orderProduct, index) => orderDetailsElement(index, orderProduct.productId)) : 
                            <></>
                    }   
                    <div className='cart-summary'>
                        <span className='total-amount'><strong>Całkowita kwota zamówienia: </strong> {totalPrice} zł</span>
                    </div>
                </>
            );
    }
}