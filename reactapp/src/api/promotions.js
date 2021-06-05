const fetchPromotions = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/promotions`);
    return response.json();
}

export {
    fetchPromotions
} 
