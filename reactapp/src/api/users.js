const fetchUserByUsername = async (username) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users?username=${username}`);
    return await response.json();
}

const fetchUserById = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users/${id}`);
    return await response.json();
}

const createUser = async (body) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users`, { 
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });
    return await response.json();
}

export {
    fetchUserByUsername,
    fetchUserById,
    createUser
}