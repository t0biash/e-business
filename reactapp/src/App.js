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
import OrderDetails from './components/OrderDetails/OrderDetails';
import Cart from './components/Cart/Cart';
import { BrowserRouter, Route, Redirect } from 'react-router-dom';
import CarContextProvider from './contexts/CarContext';
import ProductsContextProvider from './contexts/ProductsContext';
import PromotionsContextProvider from './contexts/PromotionsContext';
import { CartContext } from './contexts/CartContext';
import { UserContext } from './contexts/UserContext';
import React, { useState } from 'react';
import Cookies from 'js-cookie';
import './App.css';

export default function App(props) {
    const [authenticated, setAuthenticated] = useState(Cookies.get('csrfToken') === undefined ? false : true);
    const [userId, setUserId] = useState(Cookies.get('user') === undefined ? 0 : parseInt(Cookies.get('user')));

    const [cart, setCart] = useState([]);
    const addProduct = (product) => setCart([...cart, product]);
    const removeProduct = (id) => {
        const productToRemoveIndex = cart.findIndex(product => product.id === id);
        if (productToRemoveIndex > -1) {
            const newCart = [...cart];
            newCart.splice(productToRemoveIndex, 1);
            setCart(newCart);
        }
    };

    return (
        <CarContextProvider>
            <ProductsContextProvider>
                <UserContext.Provider value={{ authenticated, userId, setAuthenticated, setUserId }}>
                    <CartContext.Provider value={{ cart, setCart, addProduct, removeProduct }}>
                        <BrowserRouter>
                            <NavigationBar />
                            <Route exact path='/'>
                                <Home />
                            </Route>
                            <Route path='/register'>
                                <Register />
                            </Route>
                            <Route path='/login'>
                                { !authenticated ? <Login /> : <Redirect to='/dashboard' /> }
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
                                <PromotionsContextProvider>
                                    <Promotions />
                                </PromotionsContextProvider>
                            </Route>
                            <Route path='/dashboard'>
                                { authenticated ? <UserDashboard /> : <Redirect to='/login' /> }
                            </Route>
                            <Route path='/orders'>
                                { authenticated ? <Orders /> : <Redirect to='/login' /> }
                            </Route> 
                            <Route path='/order-details/:orderId'>
                                { authenticated ? <OrderDetails /> : <Redirect to='/login' /> }
                            </Route>
                            <Route path='/cart'>
                                <Cart />
                            </Route>
                        </BrowserRouter>
                    </CartContext.Provider>
                </UserContext.Provider>
            </ProductsContextProvider>
        </CarContextProvider>
    );
}