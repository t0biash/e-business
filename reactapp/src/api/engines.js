const fetchEngines = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/engines`);
    return response.json();
}

export {
    fetchEngines
}