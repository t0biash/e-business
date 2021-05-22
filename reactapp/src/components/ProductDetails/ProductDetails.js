import React, { useEffect, useState } from 'react';
import Comment from '../Comments/Comments';
import Menu from '../Menu/Menu';
import { useParams } from 'react-router-dom';
import { fetchProductById } from '../../api/products';
import { fetchCategoryById } from '../../api/catagories';
import { fetchPartsManufacturerById } from '../../api/partsManufacturers';
import { fetchCommentsByProductId } from '../../api/productComments';

export default function ProductDetails() {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [category, setCategory] = useState(null);
    const [partsManufacturer, setPartsManufacturer] = useState(null);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const product = await fetchProductById(productId);
            const category = await fetchCategoryById(product.categoryId);
            const partsManufacturer = await fetchPartsManufacturerById(product.partsManufacturerId);
            const comments = await fetchCommentsByProductId(productId);

            setProduct(product);
            setCategory(category);
            setPartsManufacturer(partsManufacturer);
            setComments(comments);
        }
        fetchData();
    }, [productId])

    if (product != null && category != null && partsManufacturer != null) 
        return (
            <>
                <Menu />
                <div className='list-element'>
                    <h3>{product.name}</h3>
                    <strong>Opis: </strong>{product.description}<br />
                    <strong>Cena: </strong>{product.price} zł<br />
                    <strong>Kategoria: </strong>{category.name}<br /> 
                    <strong>Producent: </strong>{partsManufacturer.name}<br /><br />
                    <strong>Komentarze:</strong><br />
                    { comments.length > 0 ? comments.map(comment => <Comment key={comment.id} data={comment} />) : <></> }
                </div>
            </>
        );
    else
        return (<></>);
}