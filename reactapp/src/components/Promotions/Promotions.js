import React, { useContext } from 'react';
import Menu from '../Menu/Menu';
import { ProductsContext } from '../../contexts/ProductsContext';
import { PromotionsContext } from '../../contexts/PromotionsContext';

export default function Promotions() {
    const { products } = useContext(ProductsContext);
    const promotions = useContext(PromotionsContext);

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