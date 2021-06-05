import React, { useContext } from 'react';
import { CartContext } from '../../contexts/CartContext';
import Card from 'react-bootstrap/Card';

export default function CartItem(props) {
    const { removeProduct } = useContext(CartContext);

    return (
        <Card style={{ width: '20rem', marginBottom: '32px' }}>
            <Card.Body>
                <Card.Title style={{ textAlign: 'center', marginBottom: '32px' }} bg='primary'>{props.data.name}</Card.Title>
                <Card.Text>
                    <strong>Opis: </strong>{props.data.description} <br />
                    <strong>Cena: </strong>{props.data.price} zł
                </Card.Text>
            </Card.Body>
            <Card.Body>
                <Card.Link onClick={() => removeProduct(props.data.id)}>Usuń z koszyka</Card.Link><br />
            </Card.Body>
        </Card>
    )
}