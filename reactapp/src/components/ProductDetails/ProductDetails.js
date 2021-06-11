import React, { useEffect, useState, useContext } from 'react';
import Comment from '../Comment/Comment';
import NewComment from '../NewComment/NewComment';
import Menu from '../Menu/Menu';
import { useParams } from 'react-router-dom';
import { ProductsContext } from '../../contexts/ProductsContext';
import { UserContext } from '../../contexts/UserContext';
import { fetchCommentsByProductId } from '../../api/productComments';

export default function ProductDetails() {
    const { categories, products, partsManufacturers } = useContext(ProductsContext);
    const { authenticated } = useContext(UserContext);

    const { productId } = useParams();

    const [category, setCategory] = useState(null);
    const [product, setProduct] = useState(null);
    const [partsManufacturer, setPartsManufacturer] = useState(null);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const responseProduct = products.filter(p => p.id === parseInt(productId));
            const responseCategory = categories.filter(c => c.id === responseProduct.categoryId);
            const responsePartsManufacturers = partsManufacturers.filter(pm => pm.id === responseProduct.partsManufacturerId);
            const responseComments = await fetchCommentsByProductId(productId);
            
            setCategory(responseCategory);
            setProduct(responseProduct);
            setPartsManufacturer(responsePartsManufacturers);
            setComments(Array.isArray(responseComments) ? responseComments : [responseComments]);
        };
        fetchData();
    }, [categories, products, partsManufacturers, productId, comments])



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
                    { comments.length !== 0 ? comments.map(comment => <Comment key={comment.id} data={comment} />) : <></> }
                    { authenticated ? <NewComment productId={productId} comments={comments} /> : <></> }
                </div>
            </>
        );
    else
        return (<></>);
}