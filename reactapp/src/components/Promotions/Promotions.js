import React, { useState, useEffect } from 'react';
import Menu from '../Menu/Menu';
import { fetchProducts } from '../../api/products';
import { fetchPromotions } from '../../api/promotions';

export default function Promotions() {
    const [promotions, setPromotions] = useState([]);
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            let response = await fetchProducts(); 
            setProducts(response);

            response = await fetchPromotions(); 
            setPromotions(response.filter(promotion => new Date(promotion.toDate) >= new Date()));
        }
        fetchData()
    }, []);

    return (
        <div>
            <Menu />
            { promotions.length > 0 && products.length > 0 ? promotions.map(promotion => 
                <div className='list-element' key={promotion.id}>
                    <h3>{products.filter(product => product.id === promotion.productId)[0].name}</h3>
                    {promotion.percentage}% zniżki<br/>
                    Od {promotion.fromDate} do {promotion.toDate}
                    <hr />
                </div>) : <></>
            }
        </div>
    );
}