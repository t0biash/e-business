import React, { useContext } from 'react';
import UserMenu from '../UserMenu/UserMenu';
import { UserContext } from '../../contexts/UserContext';

export default function UserDashboard(props) {
    const { setAuthenticated } = useContext(UserContext);

    const handleLogout = () => {
        localStorage.clear();
        setAuthenticated(false);
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