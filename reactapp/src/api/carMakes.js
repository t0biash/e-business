const fetchCarMakes = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/car-makes`);
    return await response.json();
}

export {
   fetchCarMakes 
}