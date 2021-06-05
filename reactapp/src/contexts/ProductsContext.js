import React, { useEffect, useState } from 'react';
import { fetchCategories } from '../api/catagories';
import { fetchPartsManufacturers } from '../api/partsManufacturers';
import { fetchProducts } from '../api/products';

export const ProductsContext = React.createContext({ categories: [], partsManufacturers: [], products: [] });

export const ProductsContextProvider = ({ children }) => {
    const [categories, setCategories] = useState([]);
    const [partsManufacturers, setPartsManufacturers] = useState([]);
    const [products, setProducts] = useState([]);

    const providerValue = { categories, partsManufacturers, products };

    useEffect(() => {
        const fetchData = async () => {
            const categories = await fetchCategories();
            const partsManufacturers = await fetchPartsManufacturers();
            const products = await fetchProducts();

            setCategories(categories);
            setPartsManufacturers(partsManufacturers);
            setProducts(products);
        };
        fetchData();
    }, []);

    return (<ProductsContext.Provider value={providerValue}>{children}</ProductsContext.Provider>);
};

export default ProductsContextProvider;