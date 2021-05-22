import React from 'react';
import SearchBar from '../SearchBar/SearchBar';
import Menu from '../Menu/Menu';
import './Home.css';

export default function Home() {
    return (
        <div className='home panel'>
            <Menu />
            <SearchBar />
        </div>
    );
}