import React, { useState, useEffect } from 'react';
import { fetchUserById } from '../../api/users';
 
export default function Comment(props) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const user = await fetchUserById(props.data.userId);
            setUser(user);
        }
        fetchData();
    }, [props.data.userId]);

    if (user != null) 
        return (
            <div className='list-element'>
                <strong>Użytkownik: </strong>{user.username}<br />
                <strong>Ocena: </strong>{props.data.rate}/5<br />
                {props.data.content}
                <hr />
            </div>
        );
    else
        return (<></>);
}