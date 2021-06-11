import React from 'react';

export const CartContext = React.createContext({ 
    cart: [], 
    addProduct: (product) => {
	// This is intentional
    }, 
    removeProduct: (id) => {
	// This is intentional
    } 
});
