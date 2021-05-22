import React, { useState, useEffect } from 'react';
import { fetchUserById } from '../../api/users';
 
export default function Comments(prop) {
    const [user, setUser] = useState(null);
    
    useEffect(() => {
        const fetchData = async () => {
            const response = await fetchUserById(prop.data.userId);
            setUser(response);
        }
        fetchData();
    }, [prop.data.userId]);

    if (user != null)
        return (
            <div className='list-element'>
                <h5>Użytkownik: {user.username}</h5>
                <strong>Ocena: </strong>{prop.data.rate}/5<br />
                {prop.data.content}
                <hr />
            </div>
        );
    else
        return (<></>);
}