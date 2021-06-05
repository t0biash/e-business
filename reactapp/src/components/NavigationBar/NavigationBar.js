import React, { useContext } from 'react';
import { UserContext } from '../../contexts/UserContext';
import { Link } from 'react-router-dom';
import './NavigationBar.css';

export default function NavigationBar(props) {
    const { authenticated, setAuthenticated } = useContext(UserContext);

    const handleLogout = () => {
        localStorage.clear();
        setAuthenticated(false);
    }

    if (!authenticated)
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
    else {
        const user = JSON.parse(localStorage.getItem('user'));
        
        return (
            <div className='navigation-bar'>
                <Link to='/' className='brand-logo'>PlayShop</Link>
                <ul>
                    <li><Link to='/cart'>Koszyk</Link></li>
                    <li><Link to='/dashboard'>Cześć {user.username}!</Link></li>
                    { authenticated ? <li><Link to='/login' onClick={handleLogout}>Wyloguj się</Link></li> : <></> }
                </ul>
            </div>
        );     
    }
}