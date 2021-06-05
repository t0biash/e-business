import React, { useState } from 'react';
import { createUser } from '../../api/users';
import './Register.css';

export default function Register(props) {
    const [inputUsername, setInputUsername] = useState('');
    const [inputPassword, setInputPassword] = useState('');


    const handleRegister = async () => {
        if (inputPassword.length < 8) {
            alert('Hasło musi mieć co najmniej 8 znaków');
            return;
        }

        const user = {
            username: inputUsername,
            password: inputPassword
        };
        const response = await createUser(user);
        alert(response.message);
        
        setInputUsername('');
        setInputPassword('');
    };

    return (
        <div className='register panel'>
            <h1>Rejestracja</h1>
            <label htmlFor='username'>Nazwa użytkownika</label>
            <input type='text' id='username' name='username' value={inputUsername} onChange={e => setInputUsername(e.target.value)} />
            <label htmlFor='password'>Hasło</label>
            <input type='password' id='password' name='password' value={inputPassword} onChange={e => setInputPassword(e.target.value)} />
            <button type='submit' onClick={handleRegister}>Zarejestruj się</button>
        </div>
    );
}