const fetchEmailByUserId = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users/${id}`);
    return response.json();
}

export {
    fetchEmailByUserId
} 