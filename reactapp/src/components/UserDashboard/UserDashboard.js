import React from 'react';
import UserMenu from '../UserMenu/UserMenu';

export default function UserDashboard(props) {
    const handleLogout = () => {
        localStorage.clear();
        props.handleLoggedIn(false);
    }

    return (
        <>
            <UserMenu />
            <div className='register panel'>
                <button type='submit' onClick={handleLogout}>Wyloguj się</button>
            </div>
        </>
    );
}