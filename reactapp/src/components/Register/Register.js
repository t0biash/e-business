import React from 'react';
import { createUser } from '../../api/users';
import './Register.css';

export default class Register extends React.Component {
    constructor(props) {
        super(props);

        this.handleRegister = this.handleRegister.bind(this);
        this.handleInputChagne = this.handleInputChange.bind(this);
        this.state = { username: '', password: '' };
    }

    handleRegister = async () => {
        if (this.state.password.length < 8) {
            alert('Hasło musi mieć co najmniej 8 znaków');
            return;
        }

        const response = await createUser(this.state);
        alert(response.message);
        this.setState({ username: '', password: '' });
    }

    handleInputChange = (event) => this.setState({ [event.target.name]: event.target.value });

    render() {
        return (
            <div className='register panel'>
                <h1>Rejestracja</h1>
                <label htmlFor='username'>Nazwa użytkownika</label>
                <input type='text' id='username' name='username' value={this.state.username} onChange={this.handleInputChange} />
                <label htmlFor='password'>Hasło</label>
                <input type='password' id='password' name='password' value={this.state.password} onChange={this.handleInputChange} />
                <button type='submit' onClick={this.handleRegister}>Zarejestruj się</button>
            </div>
        );
    }
}