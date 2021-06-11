import React from 'react';

export const UserContext = React.createContext({ 
    authenticated: false,
    userId: 0, 
    setAuthenticated: (auth) => {},
    setUserId: (id) => {} 
});
