import React from 'react';
import { Link } from 'react-router-dom';
import './NavigationBar.css';

export default class NavigationBar extends React.Component {
    render() {
        if (this.props.isLoggedIn === false)
            return (
                <div className='navigation-bar'>
                    <Link to='/' className='brand-logo'>PlayShop</Link>
                    <ul>
                        <li><Link to='/cart'>Koszyk</Link></li>
                        <li><Link to='/login'>Logowanie</Link></li>
                        <li><Link to='/register'>Rejestracja</Link></li>
                    </ul>
                </div>
            );
        else
            return (
                <div className='navigation-bar'>
                    <Link to='/' className='brand-logo'>PlayShop</Link>
                    <ul>
                        <li><Link to='/cart'>Koszyk</Link></li>
                        <li><Link to='/dashboard'>Cześć {localStorage.getItem('username')}!</Link></li>
                    </ul>
                </div>
            );     
    }
}