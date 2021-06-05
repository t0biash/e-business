import React, { useState, useEffect, useContext } from 'react';
import Select from 'react-select';
import Menu from '../Menu/Menu';
import Product from '../Product/Product';
import { ProductsContext } from '../../contexts/ProductsContext';
import './Products.css';

export default function Products() {
    const { products, categories } = useContext(ProductsContext);
   
    const [selectorCategories, setSelectorCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);

    useEffect(() => {
        const options = [];
        for (const key of Object.keys(categories))
            options.push({ label: categories[key].name, value: categories[key].id });
        setSelectorCategories(options);
    }, [categories]);

    const handleChange = selectedOption => selectedOption == null ? setSelectedCategory(null) : setSelectedCategory(selectedOption.value);

    if (products.length !== 0 && categories.length !== 0)
        return (
            <>
                <Menu />
                <Select onChange={handleChange} options={selectorCategories} placeholder='Kategoria' isClearable='true' className='category-select' />
                <div className='products-list'>
                    { selectedCategory == null ? products.map(product => <Product key={product.id} data={product} />) 
                        : products.filter(product => product.categoryId === selectedCategory).map(product => <Product key={product.id} data={product} />)  }
                </div>
            </>
        );
    else
        return (<><Menu /></>);
}