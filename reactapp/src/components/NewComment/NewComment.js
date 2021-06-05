import React, { useState } from 'react';
import { createComment } from '../../api/productComments';
import Button from 'react-bootstrap/Button';
import './NewComment.css';

export default function NewComment(props) {
    const [contentInput, setContentInput] = useState('');
    const [rateInput, setRateInput] = useState(0);

    const currentUser = JSON.parse(localStorage.getItem('user'));
    
    const addComment = async () => {
        if (contentInput === '') {
            alert('Komentarz nie może być pusty');
            return;
        }

        const comment = { rate: parseInt(rateInput), content: contentInput, userId: currentUser.id, productId: parseInt(props.productId) };
        await createComment(comment);
        
        props.comments.push(comment);
        setContentInput('');
        setRateInput(0);
    };

    if (props.comments.filter(comment => comment.userId === currentUser.id).length !== 0)
        return <></>;
    
    return (
        <div className='comment-form'>
            <strong>Komentarz: </strong><input type='text' name='content' onChange={e => setContentInput(e.target.value)} />
            <strong>Ocena: </strong><input type='number' defaultValue='0' name='rate' min='0' max='5' onChange={e => setRateInput(e.target.value)} />
            <Button onClick={addComment} variant="success">
                Dodaj komentarz
            </Button>
        </div>
    );
}