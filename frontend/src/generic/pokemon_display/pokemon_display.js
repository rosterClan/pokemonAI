import { useEffect, useState } from 'react';
import './pokemon_display.css';

import {useNavigate} from "react-router-dom";

import CustomContainer from '../CustomContainer/CustomContainer'

import top_left from '../../pages/template/static_assets/top_left.png'
import top_right from '../../pages/template/static_assets/top_right.png'

import bottom from '../../pages/template/static_assets/bottom.png'
import top from '../../pages/template/static_assets/top.png'

import bottom_left from '../../pages/template/static_assets/bottom_left.png'
import bottom_right from '../../pages/template/static_assets/bottom_right.png'

import left from '../../pages/template/static_assets/left_straight.png'
import right from '../../pages/template/static_assets/right_straight.png'

import { get_pokemon_image, get_attribute_category_image } from '../../helper';

const PokemonDisplay = (props) => {
  const navigate = useNavigate();
  const [image, set_image] = useState();
  const [category_images, set_category_images] = useState([]);

  const page_jmp = () => {
    navigate("/entry/"+props.pokemon_details['SightingID']);
  };

  useEffect(() => {
    get_pokemon_image(sessionStorage.getItem('token'), props.pokemon_details['SightingID'], set_image)
    console.log(props.pokemon_details);
    get_attribute_category_image(sessionStorage.getItem('token'), props.pokemon_details['SightingID'], set_category_images);
  }, [props.pokemon_details['PokemonName']]);

  return (
    <div class='pokemon_lst_wpr'>
        <CustomContainer
            top={top} 
            bottom={bottom} 
            top_left={top_left} 
            top_right={top_right} 
            bottom_left={bottom_left} 
            bottom_right={bottom_right} 
            left={left} 
            right={right}
            style_obj={{width:'100%',height:'70px'}}
        >
            <div class='pokemon_division_wpr' onClick={page_jmp}>
                <div class='pokemon_image_display'>
                  <img src={image} class='pokemon_image_img'/>
                </div>

                <div class='pokemon_main_content'>
                  <div class='pokemon_main_wpr'>
                    <div class='pokemon_main_row title_pokemon'>
                        <p class='pokemon_name'>{props.pokemon_details['PokemonName']}</p>
                    </div>
                    <div class='pokemon_main_row misc_details'>
                        <p class='misc_details'>{props.pokemon_details['SpeciesName']}, {props.pokemon_details['Longitude']}, {props.pokemon_details['Latitude']}</p>
                    </div>
                    <div class='pokemon_main_row'>

                      {category_images.map((item, index) => (
                        <div class='pokemon_element_tag'>
                          <img src={`data:image/png;base64,${item['image']}`} />
                        </div>
                      ))}

                    </div>
                  </div>

                </div>
            </div>
        </CustomContainer>
    </div>
  );
}

export default PokemonDisplay;