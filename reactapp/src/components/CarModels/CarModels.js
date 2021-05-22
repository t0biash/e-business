import React from 'react';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';

export default function CarModels(props) {
    return (
        <div className='selector'>
            <DropdownButton title={ props.selectedValue == null ? 'Model samochodu' : props.selectedValue } onSelect={props.handleSelect}>
                { props.carModels.map(carModel => <Dropdown.Item key={carModel.id} eventKey={carModel.id}>{carModel.name}</Dropdown.Item>) }
            </DropdownButton>
        </div>
    );
}