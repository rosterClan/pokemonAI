import './entry_page.css';

import { useEffect, useState } from 'react';

import Globe from './static_assets/globe.png';
import Clock from './static_assets/clock.png';
import BloopDrop from './static_assets/blood_drop.png';

import html2canvas from 'html2canvas';

import Template from '../template/template';
import {useParams} from 'react-router-dom';
import {useNavigate} from "react-router-dom";

import { get_pokemon_image, get_pokemon_entry } from '../../helper';

import top_left from './static_assets/top_left.png'
import top_right from './static_assets/top_right.png'

import bottom from './static_assets/bottom.png'
import top from './static_assets/top.png'

import bottom_left from './static_assets/bottom_left.png'
import bottom_right from './static_assets/bottom_right.png'

import left from './static_assets/left_straight.png'
import right from './static_assets/right_straight.png'

import CustomContainer from '../../generic/CustomContainer/CustomContainer';
import DownloadImage from '../../download.png';

function EntryPage() {
  const [image, set_image] = useState();
  const [name, set_name] = useState("");
  const [species, set_species] = useState("");
  const [long, set_long] = useState("");
  const [lat, set_lat] = useState("");
  const [timestamp, set_timestamp] = useState(new Date());
  const [hp, set_hp] = useState("");
  const [description, set_description] = useState("");
  const [attack_description, set_attack_description] = useState("");
  const [defence_description, set_defence_description] = useState("");
  let { SightingID } = useParams();

  const mass_setter = (json_obj) => {
    set_name(json_obj['PokemonName']);
    set_species(json_obj['SpeciesName']);
    set_long(json_obj['Longitude']);
    set_lat(json_obj['Latitude']);
    set_timestamp(new Date(json_obj['FoundDate']));
    set_hp(json_obj['HP']);
    set_description(json_obj["PlantTypeDesc"]);
    set_attack_description(json_obj['AttackDesc']);
    set_defence_description(json_obj['DefenseDesc']);
  }

  const download_card = () => {
    var element = document.getElementById("print_me");
    
    html2canvas(element).then(function (canvas) {
      var myImage = canvas.toDataURL("image/png");
      var link = document.createElement("a");
  
      link.download = "cartao-virtual.png";
      link.href = myImage;
      document.body.appendChild(link);
      
      link.click();
      document.body.removeChild(link);
    });
  };
  
  useEffect(() => {
    get_pokemon_entry(sessionStorage.getItem('token'), SightingID, mass_setter);
    get_pokemon_image(sessionStorage.getItem('token'), SightingID, set_image);
  },[]);

  return (
    <div id='print_me'>
      <Template> 
        <div class='entry_page_wpr'>
          <div onClick={() => download_card()} class='download_loc_data_image_wpr'>
            <img class='download_loc_data_image' src={DownloadImage}/>
          </div>

          <div class='entry_page_header'>
            <p class='entry_page_title'>{name}</p>
            <p class='entry_page_species'>{species}</p>

            <div class='page_content_image'> 
              <img src={image} class='page_content_img'/>
            </div>
            

          </div>
          <div class='entry_page_body'>

            <div class='entry_page_misc_wpr'>
              <div class='entry_page_misc'>
                <div class='entry_page_misc_image'>
                  <img src={Globe} alt="Girl in a jacket"/>
                </div>
                <div class='entry_page_misc_text'>
                  <p>{long},  {lat}</p>  
                </div>
              </div>

              <div class='entry_page_misc'>
                <div class='entry_page_misc_image'>
                  <img src={Clock} alt="Girl in a jacket"/>
                </div>
                <div class='entry_page_misc_text'>
                  <p>{timestamp.toISOString()}</p>  
                </div>
              </div>

              <div class='entry_page_misc'>
                <div class='entry_page_misc_image'>
                  <img src={BloopDrop} alt="Girl in a jacket"/>
                </div>
                <div class='entry_page_misc_text'>
                  <p>{hp}</p>  
                </div>
              </div>
            </div>

            <div class='entry_page_body_main'>
              <p class='entry_page_body_description'>{description}</p>

              <div class='attribute_wpr'>
                <CustomContainer style_obj={{width:'90%', margin:'auto', height: "100px"}} 
                  top={top} 
                  bottom={bottom} 
                  top_left={top_left} 
                  top_right={top_right} 
                  bottom_left={bottom_left} 
                  bottom_right={bottom_right} 
                  left={left} 
                  right={right}
                >
                  <p class='attack_description'>{attack_description}</p>
                </CustomContainer>
              </div>
              <div class='attribute_wpr'>
                <CustomContainer style_obj={{width:'90%', margin:'auto', height: "100px"}} 
                  top={top} 
                  bottom={bottom} 
                  top_left={top_left} 
                  top_right={top_right} 
                  bottom_left={bottom_left} 
                  bottom_right={bottom_right} 
                  left={left} 
                  right={right}
                >
                  <p class='attack_description'>{defence_description}</p>
                </CustomContainer>
              </div>
            </div>



          </div>
        </div>
      </Template>
    </div>
  );
}

export default EntryPage;