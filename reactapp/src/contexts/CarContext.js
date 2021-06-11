import React, { useEffect, useState } from 'react';
import { fetchCarMakes } from '../api/carMakes';
import { fetchCarModels } from '../api/carModels';
import { fetchEngines } from '../api/engines';

export const CarContext = React.createContext({ carMakes: [], carModels: [], engines: [] });

export const CarContextProvider = ({ children }) => {
    const [carMakes, setCarMakes] = useState([]);
    const [carModels, setCarModels] = useState([]);
    const [engines, setEngines] = useState([]);

    const providerValue = { carMakes, carModels, engines };

    useEffect(() => {
        const fetchData = async () => {
            const responseCarMakes = await fetchCarMakes();
            const responseCarModels = await fetchCarModels();
            const responseEngines = await fetchEngines();

            setCarMakes(responseCarMakes);
            setCarModels(responseCarModels);
            setEngines(responseEngines);
        };
        fetchData();
    }, []);

    return (<CarContext.Provider value={providerValue}>{children}</CarContext.Provider>);
};

export default CarContextProvider;