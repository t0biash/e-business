import React, { useContext } from 'react';
import { UserContext } from '../../contexts/UserContext';
import { Link } from 'react-router-dom';
import { signOut } from '../../api/authentication';
import './NavigationBar.css';

export default function NavigationBar(props) {
    const { authenticated, setAuthenticated, setUserId } = useContext(UserContext);

    const handleLogout = async () => {
        await signOut();

        setAuthenticated(false);
        setUserId(0);
	    sessionStorage.clear();
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
        return (
            <div className='navigation-bar'>
                <Link to='/' className='brand-logo'>PlayShop</Link>
                <ul>
                    <li><Link to='/cart'>Koszyk</Link></li>
                    <li><Link to='/dashboard'>Dashboard</Link></li>
                    { authenticated ? <li><Link to='/' onClick={handleLogout}>Wyloguj się</Link></li> : <></> }
                </ul>
            </div>
        );     
    }
}