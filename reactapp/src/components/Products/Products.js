import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import Menu from '../Menu/Menu';
import Product from '../Product/Product';
import { fetchProducts } from '../../api/products';
import { fetchCategories } from '../../api/catagories';
import './Products.css';

export default function Products() {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const products = await fetchProducts();
            setProducts(products);

            const categories = await fetchCategories();
            const options = [];
            for (const key of Object.keys(categories))
                options.push({ label: categories[key].name, value: categories[key].id });
            setCategories(options);
        }
        fetchData();
    }, []);

    const handleChange = selectedOption => {
        selectedOption == null ? setSelectedCategory(null) : setSelectedCategory(selectedOption.value);
    };

    if (products.length !== 0 && categories.length !== 0)
        return (
            <>
                <Menu />
                <Select onChange={handleChange} options={categories} placeholder='Kategoria' isClearable='true' className='category-select' />
                <div className='products-list'>
                    { selectedCategory == null ? products.map(product => <Product key={product.id} data={product} />) 
                        : products.filter(product => product.categoryId === selectedCategory).map(product => <Product key={product.id} data={product} />)  }
                </div>
            </>
        );
    else
        return (<><Menu /></>);
}