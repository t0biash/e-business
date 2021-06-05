import React from 'react';

export const CartContext = React.createContext({ 
    cart: [], 
    addProduct: (product) => {}, 
    removeProduct: (id) => {} 
});
