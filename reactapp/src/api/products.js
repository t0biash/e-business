const fetchProducts = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/products`);
    return response.json();
}

const fetchProductById = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/products/${id}`);
    return response.json();
}

export {
    fetchProducts,
    fetchProductById
} 
