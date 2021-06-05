const fetchCommentsByProductId = async (productId) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/comments?productId=${productId}`);
    return response.json();
}

const createComment = async (comment) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/products/${comment.productId}/comments`, { 
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(comment)
    });
    return response.json();
}

export {
    fetchCommentsByProductId,
    createComment
}