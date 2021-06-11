import Cookies from 'js-cookie';

const signIn = async (data) => {
    return fetch(`${process.env.REACT_APP_API_URL}/signIn`, { 
        method: 'POST',
        mode: 'cors',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
};

const signOut = async () => {
    const csrfToken = Cookies.get('csrfToken');

    return fetch(`${process.env.REACT_APP_API_URL}/signOut`, { 
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'Csrf-Token': csrfToken
        }
    });
};

const authenticate = async (provider) => {
    return fetch(`${process.env.REACT_APP_API_URL}/authenticate/${provider}`);
}

const signUp = async (data) => {
    return fetch(`${process.env.REACT_APP_API_URL}/signUp`, { 
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
};

export {
   signIn,
   signOut,
   authenticate,
   signUp 
}