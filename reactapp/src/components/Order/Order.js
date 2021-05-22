import React from 'react';
import Card from 'react-bootstrap/Card';

export default function Order(prop) {
    return (
        <>
            <Card style={{ width: '20rem' }}>
                <Card.Body>
                    <Card.Title style={{ textAlign: 'center', marginBottom: '32px' }} bg='primary'>Data złożenia zamówienia: {prop.data.date}</Card.Title>
                </Card.Body>
            </Card>
        </>
    );
}