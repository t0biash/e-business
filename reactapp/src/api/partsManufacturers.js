const fetchPartsManufacturers = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/parts-manufacturers`);
    return await response.json();
}

const fetchPartsManufacturerById = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/parts-manufacturers/${id}`);
    return await response.json();
}

export {
    fetchPartsManufacturers,
    fetchPartsManufacturerById
}