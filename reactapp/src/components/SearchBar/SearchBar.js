import React, { useState, useContext } from 'react';
import CarMakes from '../CarMakes/CarMakes';
import CarModels from '../CarModels/CarModels';
import Engines from '../Engines/Engines';
import Product from '../Product/Product';
import { CarContext } from '../../contexts/CarContext';
import { ProductsContext } from '../../contexts/ProductsContext';
import './SearchBar.css';

export default function SearchBar(props) {
    const { products } = useContext(ProductsContext);
    const { carMakes, carModels, engines } = useContext(CarContext);

    const [selectedCarMakeId, setSelectedCarMakeId] = useState(null);
    const [selectedCarModelId, setSelectedCarModelId] = useState(null);
    const [selectedEngineId, setSelectedEngineId] = useState(null);
    const [showProducts, setShowProducts] = useState(false);

    const handleCarMakeSelect = (evtKey, _) => {
        setSelectedCarMakeId(parseInt(evtKey));
        setSelectedCarModelId(null);
        setSelectedEngineId(null);
        setShowProducts(false);
    };

    const handleCarModelSelect = (evtKey, _) => {
        setSelectedCarModelId(parseInt(evtKey));
        setSelectedEngineId(null);
        setShowProducts(false);
    };

    const handleEngineSelect = (evtKey, _) => {
        setSelectedEngineId(parseInt(evtKey));
        setShowProducts(false);
    };

    return (
        <div className='search-bar'>
            <div className='select-car-header'>Wybierz samochód</div>
            <div className='select-car'>
                <CarMakes handleSelect={handleCarMakeSelect} carMakes={carMakes}
                    selectedValue={selectedCarMakeId == null ? null : carMakes.filter(carMake => carMake.id === selectedCarMakeId)[0].name} />
                <CarModels handleSelect={handleCarModelSelect} carModels={carModels.filter(carModel => carModel.carMakeId === selectedCarMakeId)} 
                    selectedValue={selectedCarModelId == null ? null : carModels.filter(carModel => carModel.id === selectedCarModelId)[0].name} />
                <Engines handleSelect={handleEngineSelect} engines={engines.filter(engine => engine.carModelId === selectedCarModelId)} 
                    selectedValue={selectedEngineId == null ? null : engines.filter(engine => engine.id === selectedEngineId)[0].engine} />
                <button className='search-btn' onClick={() => setShowProducts(true)}>Szukaj części</button>
                <div className='products-list'>
                    { 
                        products.length === 0 || !showProducts ? 
                            <></> : 
                            products.filter(product => product.carModelId === selectedCarModelId).map(product => <Product key={product.id} data={product} />) 
                    }
                </div>
            </div>
        </div>
    );
}
