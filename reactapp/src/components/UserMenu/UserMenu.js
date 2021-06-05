import React from 'react';
import { Link } from 'react-router-dom';

export default function UserMenu() {
    return (
        <div className='user-menu'>
            <div className='first-tab'><Link to='/dashboard'>Dashboard</Link></div>
            <div className='second-tab'><Link to='/orders'>Zamówienia</Link></div>
        </div>
    );
}