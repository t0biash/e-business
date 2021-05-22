const fetchCarModels = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/car-models`);
    return await response.json();
}

export {
    fetchCarModels
}