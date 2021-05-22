import React from 'react';
import Card from 'react-bootstrap/Card';
import { Link } from 'react-router-dom';

export default function Product(prop) {
    return (
        <Card style={{ width: '20rem', marginBottom: '32px' }}>
            <Card.Body>
                <Card.Title style={{ textAlign: 'center', marginBottom: '32px' }} bg='primary'>{prop.data.name}</Card.Title>
                <Card.Text>
                    <strong>Opis: </strong>{prop.data.description} <br />
                    <strong>Cena: </strong>{prop.data.price} zł
                </Card.Text>
            </Card.Body>
            <Card.Body>
                <Card.Link>Dodaj do koszyka</Card.Link><br />
                <Link to={`/product-details/${prop.data.id}`}>Sprawdź szczegóły</Link>
            </Card.Body>
        </Card>
    );
}