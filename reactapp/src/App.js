import NavigationBar from './components/NavigationBar/NavigationBar'
import Home from './components/Home/Home';
import Register from './components/Register/Register';
import Login from './components/Login/Login';
import PartsManufacturers from './components/PartsManufacturers/PartsManufacturers';
import Products from './components/Products/Products';
import Promotions from './components/Promotions/Promotions';
import ProductDetails from './components/ProductDetails/ProductDetails';
import UserDashboard from './components/UserDashboard/UserDashboard';
import Orders from './components/Orders/Orders';
import { BrowserRouter, Route, Redirect } from 'react-router-dom';
import React from 'react';
import './App.css';

export default class App extends React.Component {
    constructor(props) {
        super(props);

        this.handleLoggedIn = this.handleLoggedIn.bind(this);
        this.state = { IsLoggedIn: false };
    }

    handleLoggedIn = (isLoggedIn) => {
        this.setState({ IsLoggedIn: isLoggedIn });
    }

    render() {
        return (
            <BrowserRouter>
                <NavigationBar isLoggedIn={this.state.IsLoggedIn} />
                <Route exact path='/'>
                    <Home />
                </Route>
                <Route path='/register'>
                    <Register />
                </Route>
                <Route path='/login'>
                    { !this.state.IsLoggedIn ? <Login handleLoggedIn={this.handleLoggedIn} isLoggedIn={this.state.IsLoggedIn} /> : <Redirect to='/dashboard' /> }
                </Route>
                <Route path='/shop'>
                    <Products />
                </Route>
                <Route path='/product-details/:productId'>
                    <ProductDetails />
                </Route>
                <Route path='/parts-manufacturers'>
                    <PartsManufacturers />
                </Route>
                <Route path='/promotions'>
                    <Promotions />
                </Route>
                <Route path='/dashboard'>
                    { this.state.IsLoggedIn ? <UserDashboard handleLoggedIn={this.handleLoggedIn} isLoggedIn={this.state.IsLoggedIn} /> : <Redirect to='/login' /> }
                </Route>
                <Route path='/orders'>
                    { this.state.IsLoggedIn ? <Orders /> : <Redirect to='/login' /> }
                </Route> 
            </BrowserRouter>
        );
    }
}