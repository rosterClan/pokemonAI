import { useEffect, useState } from 'react';
import { useLocation } from "react-router-dom";

import './other_usr_dex.css';

import Template from '../template/template';
import PokemonDisplay from '../../generic/pokemon_display/pokemon_display';
import TextFeild from '../../generic/text_input/text_input';
import Button from '../../generic/button/button';



import { get_userID_dex } from '../../helper';

const GetAltUserDex = (props) => {
  const [entry, set_entry] = useState([]);
  const [filtered_entry, set_filtered_entry] = useState([]);

  const location = useLocation();
  const { userID } = location.state || {};
  const { userName } = location.state || {};

  useEffect(() => {
    filter_items();
  }, [entry]);
  
  useEffect(() => {
    if (userID != null) {
      get_userID_dex(sessionStorage.getItem('token'), userID, set_entry);
    }
  },[]);

  const filter_items = () => {
    let all_items = entry; 
    let search_term = document.getElementById('filter_dex_entry').value; 
    let filtered_items = [];

    for (var idx = 0; idx < all_items.length; idx++) {
      if (all_items[idx]['PokemonName'].includes(search_term)) {
        filtered_items.push(all_items[idx]);
      }
    }

    set_filtered_entry(filtered_items);
  };

  return (
    <Template> 
      <p class='title_font center' id='my_dex_title'>{userName}s' Dex</p>

        <div class='filter_controls'>
          <div class='filter_wpr'>
            <TextFeild field_id="filter_dex_entry" style_obj={{width:"100%", height:"20px"}} placeholder="Search" />
          </div>

          <div class='filter_wpr filter_btn_wpr'>
            <Button trig_function={filter_items} style_obj={{width:"100%", height:"20px"}} txt="Search" />
          </div>
        </div>

        {filtered_entry.map((item, index) => (
          <PokemonDisplay pokemon_details={item} />
        ))}

    </Template>
  );
}

export default GetAltUserDex;