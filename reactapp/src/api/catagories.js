const fetchCategories = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/categories`);
    return await response.json();
}

const fetchCategoryById = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/categories/${id}`);
    return await response.json();
}

export { 
    fetchCategories, 
    fetchCategoryById 
}
