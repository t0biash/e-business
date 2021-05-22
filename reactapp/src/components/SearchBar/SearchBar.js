import React from 'react';
import CarMakes from '../CarMakes/CarMakes';
import CarModels from '../CarModels/CarModels';
import Engines from '../Engines/Engines';
import Product from '../Product/Product';
import { fetchCarMakes } from '../../api/carMakes';
import { fetchCarModels } from '../../api/carModels';
import { fetchEngines } from '../../api/engines';
import { fetchProducts } from '../../api/products';
import './SearchBar.css';

export default class SearchBar extends React.Component {
    constructor(props) {
        super(props);

        this.handleCarMakeSelect = this.handleCarMakeSelect.bind(this);
        this.handleCarModelSelect = this.handleCarModelSelect.bind(this);
        this.handleEngineSelect = this.handleEngineSelect.bind(this);
        this.handleProductsSearch = this.handleProductsSearch.bind(this);
        this.state = { carMakes: [], carModels: [], engines: [], selectedCarMakeId: null, selectedCarModelId: null, selectedEngineId: null, products: [] };
    }

    async componentDidMount() {
        const carMakes = await fetchCarMakes();
        const carModels = await fetchCarModels();
        const engines = await fetchEngines(); 

        this.setState({ carMakes: carMakes, carModels: carModels, engines: engines });
    }

    handleCarMakeSelect = (evtKey, _) => this.setState({ selectedCarMakeId: parseInt(evtKey), selectedCarModelId: null, selectedEngineId: null });
    handleCarModelSelect = (evtKey, _) => this.setState({ selectedCarModelId: parseInt(evtKey), selectedEngineId: null }) 
    handleEngineSelect = (evtKey, _) => this.setState({ selectedEngineId: parseInt(evtKey) }) 

    handleProductsSearch = async () => {
        let products = await fetchProducts();
        products = products.filter(product => product.carModelId === this.state.selectedCarModelId);

        this.setState({ products: products });
    };

    render() {
        return (
            <div className='search-bar'>
                <div className='select-car-header'>Wybierz samochód</div>
                <div className='select-car'>
                    <CarMakes handleSelect={this.handleCarMakeSelect} carMakes={this.state.carMakes} 
                        selectedValue={this.state.selectedCarMakeId == null ? null : this.state.carMakes.filter(carMake => carMake.id === this.state.selectedCarMakeId)[0].name} />
                    <CarModels handleSelect={this.handleCarModelSelect} carModels={this.state.carModels.filter(carModel => carModel.carMakeId === this.state.selectedCarMakeId)} 
                        selectedValue={this.state.selectedCarModelId == null ? null : this.state.carModels.filter(carModel => carModel.id === this.state.selectedCarModelId)[0].name} />
                    <Engines handleSelect={this.handleEngineSelect} engines={this.state.engines.filter(engine => engine.carModelId === this.state.selectedCarModelId)} 
                        selectedValue={this.state.selectedEngineId == null ? null : this.state.engines.filter(engine => engine.id === this.state.selectedEngineId)[0].engine} />
                    <button className='search-btn' onClick={this.handleProductsSearch}>Szukaj części</button>
                    <div className='products-list'>
                        { this.state.products.map(product => <Product key={product.id} data={product} />) }
                    </div>
                </div>
            </div>
        );
    }
}