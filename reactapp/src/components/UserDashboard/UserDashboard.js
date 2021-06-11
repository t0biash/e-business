import React, { useContext } from 'react';
import UserMenu from '../UserMenu/UserMenu';
import { UserContext } from '../../contexts/UserContext';
import { signOut } from '../../api/authentication';

export default function UserDashboard(props) {
    const { setAuthenticated, setUserId } = useContext(UserContext);

    const handleLogout = async () => {
        const response = await signOut();
        
        if (response.status === 200) {
            setAuthenticated(false);
            setUserId(0);
        }
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