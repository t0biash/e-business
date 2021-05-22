const fetchUserOrders = async (userId) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/orders?userId=${userId}`)
    return await response.json();
}

export {
    fetchUserOrders
}