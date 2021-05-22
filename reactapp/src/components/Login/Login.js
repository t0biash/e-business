import React from 'react';
import { fetchUserByUsername } from '../../api/users';

export default class Login extends React.Component {
    constructor(props) {
        super(props);

        this.handleLogin = this.handleLogin.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.state = { username: '', password: '' }
    }

    handleLogin = async () => {
        const response = await fetchUserByUsername(this.state.username);

        if (Array.isArray(response)) {
            alert('Invalid username');
        }
        else {
            if (response.password === this.state.password) {
                localStorage.setItem('username', response.username);
                localStorage.setItem('userId', response.id);
                this.props.handleLoggedIn(true);
            }
            else
                alert('Invalid password');
        }
        this.setState({ username: '', password: '' });
    }

    handleInputChange = (event) => this.setState({ [event.target.name]: event.target.value });

    render() {
        if (this.props.isLoggedIn === false)
            return (
                <div className='register panel'>
                    <h1>Logowanie</h1>
                    <label htmlFor='username'>Nazwa użytkownika</label>
                    <input type='text' id='username' name='username' value={this.state.username} onChange={this.handleInputChange} />
                    <label htmlFor='password'>Hasło</label>
                    <input type='password' id='password' name='password' value={this.state.password} onChange={this.handleInputChange} />
                    <button type='submit' onClick={this.handleLogin}>Zaloguj się</button>
                </div>
            );
        else
            return (
                <div className='register panel'>
                    <button type='submit' onClick={this.handleLogout}>Wyloguj się</button>
                </div>
            );
    }
}