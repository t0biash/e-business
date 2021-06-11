import React, { useEffect, useState } from 'react';
import { fetchPromotions } from '../api/promotions';

export const PromotionsContext = React.createContext([]);

export const PromotionsContextProvider = ({ children }) => {
    const [promotions, setPromotions] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetchPromotions();

            setPromotions(response);
        };
        fetchData();
    }, []);

    return (<PromotionsContext.Provider value={promotions}>{children}</PromotionsContext.Provider>);
};

export default PromotionsContextProvider;