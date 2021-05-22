const fetchCommentsByProductId = async (id) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/comments/${id}`);
    return await response.json();
}

export {
    fetchCommentsByProductId
}