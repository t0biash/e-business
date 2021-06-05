import React from 'react';
import Card from 'react-bootstrap/Card';
import { Link } from 'react-router-dom';

export default function Order(props) {
    return (
        <>
            <Card style={{ width: '20rem' }}>
                <Card.Body>
                    <Card.Title style={{ textAlign: 'center', marginBottom: '32px' }} bg='primary'>Data złożenia zamówienia: {props.data.date}</Card.Title>
                </Card.Body>
                <Card.Body>
                    <Link to={`/order-details/${props.data.id}`}>Sprawdź szczegóły</Link>
            </Card.Body>
            </Card>
        </>
    );
}