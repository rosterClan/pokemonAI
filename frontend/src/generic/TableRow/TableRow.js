import {useNavigate} from "react-router-dom";

import './TableRow.css';
import { useEffect, useState } from 'react';

import OpenEye from './static_assets/view.png';
import ClosedEye from './static_assets/closed_eye.png';
import TempIcon from './static_assets/leaf.png';

import { get_pokemon_image, get_attribute_category_image } from "../../helper";

const TableRow = (props) => {
    const navigate = useNavigate();

    const [test, set_test] = useState([]);
    const [eye_display, set_eye_display] = useState(OpenEye);
    const [pokemon_data, set_pokemon_data] = useState(props.pokemon_data);
    const [image_data, set_image_data] = useState();

    const page_jmp = () => {
        navigate("/entry/"+pokemon_data['SightingID']);
    }

    useEffect(() => {
        get_pokemon_image(sessionStorage.getItem("token"), pokemon_data['SightingID'], set_image_data);
        get_attribute_category_image(sessionStorage.getItem("token"), pokemon_data['SightingID'], set_test);
    },[pokemon_data]);

    const hide_entry = (element) => {
        let true_target_element = element.parentElement.parentElement;
        if (true_target_element.style.opacity == "") {
            true_target_element.style.opacity = "100%";
        }

        if (true_target_element.style.opacity == 1) {
            true_target_element.style.opacity = "50%";
            set_eye_display(ClosedEye);
            props.filter_function(pokemon_data['SightingID'], 1);
        } else {
            true_target_element.style.opacity = "100%";
            set_eye_display(OpenEye);
            props.filter_function(pokemon_data['SightingID'], 0);
        }
    }

    return (
        <div class='row_wpr'>
            <div class='row_image'>
                <img src={image_data} class='row_image_display'/>
            </div>
            <div onClick={page_jmp} class='row_name'>
                <p>{pokemon_data['PokemonName']} - {pokemon_data['SpeciesName']}</p>
            </div>
            <div class='row_attributes'>
                {test.map((item, index) => (
                    <img src={`data:image/png;base64,${item['image']}`} class='icon_container'/>
                ))}
            </div>
            <div class='row_hide_btn'>
                <img onClick={(event) => hide_entry(event.target)} src={eye_display}/>
            </div>
        </div>
    );
}

export default TableRow;