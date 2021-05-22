import React, { useState, useEffect } from 'react';
import Menu from '../Menu/Menu';
import { fetchPartsManufacturers } from '../../api/partsManufacturers';
import './PartsManufacturers.css';

export default function PartsManufacturers() {
    const [partsManufacturers, setPartsManufacturers] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetchPartsManufacturers();
            setPartsManufacturers(response);
        }
        fetchData();
    }, []);

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