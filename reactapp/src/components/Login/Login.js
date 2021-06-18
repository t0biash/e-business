import React, { useState, useContext } from 'react';
import { signIn } from '../../api/authentication';
import { UserContext } from '../../contexts/UserContext';

export default function Login(props) {
    const { authenticated, setAuthenticated, setUserId } = useContext(UserContext);
    
    const [inputEmail, setInputEmail] = useState('');
    const [inputPassword, setInputPassword] = useState('');

    const handleLogin = async () => {
        const user = {
            email: inputEmail,
            password: inputPassword
        };
        const response = await signIn(user);

        setInputEmail('');
        setInputPassword('');

        if (response.status === 200) {
            alert('Zalogowano');
            setAuthenticated(true);
	        if (response.headers.get('csrfToken') !== null)
		        sessionStorage.setItem('csrfToken', response.headers.get('csrfToken'));
	        if (response.headers.get('userId') !== null) {
		        sessionStorage.setItem('userId', response.headers.get('userId'));
		        setUserId(parseInt(response.headers.get('userId')));
	        }	
        }
        else {
            alert('Nieprawidłowe dane logowania');
        }
    };

    const navigateTo = (url) => {
        window.location.assign(`${process.env.REACT_APP_API_URL}` + url);
    };

    if (!authenticated)
        return (
            <div className='register panel'>
                <h1>Logowanie</h1>
                <label htmlFor='email'>Email</label>
                <input type='text' id='email' name='email' value={inputEmail} onChange={e => setInputEmail(e.target.value)} />
                <label htmlFor='password'>Hasło</label>
                <input type='password' id='password' name='password' value={inputPassword} onChange={e => setInputPassword(e.target.value)} />
                <button type='submit' onClick={handleLogin}>Zaloguj się</button>
                <button id='google-auth-btn' type='submit' onClick={() => navigateTo('/authenticate/google')}>Zaloguj się za pomocą Google</button>
                <button id='fb-auth-btn' type='submit' onClick={() => navigateTo('/authenticate/facebook')}>Zaloguj się za pomocą Facebooka</button>
            </div>
        );
}