import React, { useState, useEffect } from 'react';
import { fetchEmailByUserId } from '../../api/users';
 
export default function Comment(props) {
    const [email, setEmail] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetchEmailByUserId(props.data.userId);
            setEmail(response);
        }
        fetchData();
    }, [props.data.userId]);

    if (email != null) 
        return (
            <div className='list-element'>
                <strong>Użytkownik: </strong>{email}<br />
                <strong>Ocena: </strong>{props.data.rate}/5<br />
                {props.data.content}
                <hr />
            </div>
        );
    else
        return (<></>);
}