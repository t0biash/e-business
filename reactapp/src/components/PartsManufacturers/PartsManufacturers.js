import React, { useContext } from 'react';
import Menu from '../Menu/Menu';
import { ProductsContext } from '../../contexts/ProductsContext';
import './PartsManufacturers.css';

export default function PartsManufacturers() {
    const { partsManufacturers } = useContext(ProductsContext);

    return (
        <div>
            <Menu />
            { partsManufacturers.length > 0 ? partsManufacturers.map(partsManufacturer => 
                <div key={partsManufacturer.id} className='list-element'>
                    <h3>{partsManufacturer.name}</h3>
                    {partsManufacturer.description}
                    <hr />
                </div>) : <></>
            }
        </div>
    );
}