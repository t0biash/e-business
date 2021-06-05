const fetchUserByUsername = async (username) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users?username=${username}`);
    return response.json();
}

const fetchUserById = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users/${id}`);
    return response.json();
}

const createUser = async (user) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/users`, { 
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    });
    return response.json();
}

export {
    fetchUserByUsername,
    fetchUserById,
    createUser
}