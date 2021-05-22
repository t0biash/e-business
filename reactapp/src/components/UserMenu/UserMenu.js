import React from 'react';
import { Link } from 'react-router-dom';

export default function UserMenu() {
    return (
        <div className='menu'>
            <div className='first-tab'><Link to='/dashboard'>Dashboard</Link></div>
            <div className='second-tab'><Link to='/orders'>Zamówienia</Link></div>
            <div className='third-tab'><Link to='/orders'>Płatności</Link></div>
        </div>
    );
}