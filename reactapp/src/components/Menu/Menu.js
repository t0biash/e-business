import React from 'react';
import { Link } from 'react-router-dom';
import './Menu.css';

export default function Menu() {
    return (
        <div className='menu'>
            <div className='first-tab'><Link to='/parts-manufacturers'>Producenci</Link></div>
            <div className='second-tab'><Link to='/shop'>Części samochodowe</Link></div>
            <div className='third-tab'><Link to='/promotions'>Promocje</Link></div>
        </div>
    );
}