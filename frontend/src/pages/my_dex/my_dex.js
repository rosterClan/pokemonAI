import { useEffect, useState } from 'react';
import {useNavigate} from "react-router-dom";

import './my_dex.css';

import Template from '../template/template';
import PokemonDisplay from '../../generic/pokemon_display/pokemon_display';
import TextFeild from '../../generic/text_input/text_input';
import Button from '../../generic/button/button';

import ClockImage from '../../generic/ActivityRow/static_assets/clock.png';
import LogoutImage from '../../generic/logout/static_assets/logout.png';

import { dex_handler, getUserDetails } from '../../helper';

function MyDex() {
  const [entry, set_entry] = useState([]);
  const [filtered_entry, set_filtered_entry] = useState([]);
  const [hide, setHide] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    filter_items();
  }, [entry]);

  const hideDetails = (data) => {
    console.log("pog",data);
    setHide(data['role'] == "1");
  };
  
  useEffect(() => {
    dex_handler(sessionStorage.getItem('token'), set_entry);
    getUserDetails(sessionStorage.getItem('token'),hideDetails );
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

  const open_activity_log = () => {
    navigate("/activity_log");
};

  const open_logout = () => {
    navigate("/logout");
  };

  return (
    <Template> 
      <p class='title_font center' id='my_dex_title'>My Dex</p>

      { 
        hide ? <div onClick={() => open_activity_log()} class='clock_loc_data_image_wpr'>
          <img class='clock_loc_data_image' src={ClockImage}/>
        </div> : <></>
      }


      <div onClick={() => open_logout()} class='logout_loc_data_image_wpr'>
          <img class='logout_loc_data_image' src={LogoutImage}/>
      </div>

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

export default MyDex;