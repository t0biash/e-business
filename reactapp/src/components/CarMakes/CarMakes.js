import React from 'react';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';

export default function CarMakes(props) {
    return (
        <div className='selector'>
            <DropdownButton title={ props.selectedValue == null ? 'Marka samochodu' : props.selectedValue } onSelect={props.handleSelect}>
                { props.carMakes.map(carMake => <Dropdown.Item key={carMake.id} eventKey={carMake.id}>{carMake.name}</Dropdown.Item>) }
            </DropdownButton>
        </div>
    );
}