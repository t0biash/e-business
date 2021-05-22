import React from 'react';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';

export default function Engines(props) {
    return (
        <div className='selector'>
            <DropdownButton title={ props.selectedValue == null ? 'Silnik' : props.selectedValue } onSelect={props.handleSelect}>
                { props.engines.map(engine => <Dropdown.Item key={engine.id} eventKey={engine.id}>{engine.engine}</Dropdown.Item>) }
            </DropdownButton>
        </div>
    );
}