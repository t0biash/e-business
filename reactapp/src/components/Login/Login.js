import React, { useState, useContext } from 'react';
import { fetchUserByUsername } from '../../api/users';
import { UserContext } from '../../contexts/UserContext';

export default function Login(props) {
    const { authenticated, setAuthenticated } = useContext(UserContext);
    
    const [inputUsername, setInputUsername] = useState('');
    const [inputPassword, setInputPassword] = useState('');

    const handleLogin = async () => {
        const response = await fetchUserByUsername(inputUsername);
        let validData = false;

        if (Array.isArray(response)) {
            alert('Invalid username');
            validData = false;
        }
        else {
            if (response.password === inputPassword) 
                validData = true;
            else {
                alert('Invalid password');
                validData = false;
            }
        }

        setInputUsername('');
        setInputPassword('');

        if (validData) {
            const user = { id: response.id, username: response.username };
            localStorage.setItem('user', JSON.stringify(user));
            setAuthenticated(true);
        }
    };

    if (!authenticated)
        return (
            <div className='register panel'>
                <h1>Logowanie</h1>
                <label htmlFor='username'>Nazwa użytkownika</label>
                <input type='text' id='username' name='username' value={inputUsername} onChange={e => setInputUsername(e.target.value)} />
                <label htmlFor='password'>Hasło</label>
                <input type='password' id='password' name='password' value={inputPassword} onChange={e => setInputPassword(e.target.value)} />
                <button type='submit' onClick={handleLogin}>Zaloguj się</button>
            </div>
        );
}