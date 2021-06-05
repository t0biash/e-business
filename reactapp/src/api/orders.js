const fetchUserOrders = async (userId) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/orders?userId=${userId}`);
    return response.json();
}

const fetchOrderProducts = async (orderId) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/orders/${orderId}/products`);
    return response.json();
}

const createOrder = async (order) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users/${order.userId}/orders`, { 
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(order)
    });
    return response.json();
}

const createOrderProduct = async (order) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/orders/${order.orderId}/products`, { 
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(order)
    });
    return response.json();
}

export {
    fetchUserOrders,
    fetchOrderProducts,
    createOrder,
    createOrderProduct
}