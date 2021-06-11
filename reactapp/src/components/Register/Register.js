import React, { useState } from 'react';
import { signUp } from '../../api/authentication';
import './Register.css';

export default function Register(props) {
    const [inputEmail, setInputEmail] = useState('');
    const [inputPassword, setInputPassword] = useState('');


    const handleRegister = async () => {
        if (inputPassword.length < 8) {
            alert('Hasło musi mieć co najmniej 8 znaków');
            return;
        }

        const user = {
            email: inputEmail,
            password: inputPassword
        };
        const response = await signUp(user);
        if (response.status === 201)
            alert('Użytkownik został poprawnie zarejestrowany');
        else
            alert('Użytkownik o podanym adresie email już istnieje');
        
        setInputEmail('');
        setInputPassword('');
    };

    return (
        <div className='register panel'>
            <h1>Rejestracja</h1>
            <label htmlFor='email'>Email</label>
            <input type='text' id='email' name='email' value={inputEmail} onChange={e => setInputEmail(e.target.value)} />
            <label htmlFor='password'>Hasło</label>
            <input type='password' id='password' name='password' value={inputPassword} onChange={e => setInputPassword(e.target.value)} />
            <button type='submit' onClick={handleRegister}>Zarejestruj się</button>
        </div>
    );
}