import './map.css';
import {GoogleMap, LoadScript, Marker} from '@react-google-maps/api';
import {MarkerF} from '@react-google-maps/api'
import {useEffect, useState} from 'react';
import {useNavigate, useLocation} from "react-router-dom";

import Template from '../template/template';
import CommonContainer from '../../generic/CommonContainer/CommonContainer';
import TextFeild from '../../generic/text_input/text_input';
import Button from '../../generic/button/button';

import TableRow from '../../generic/TableRow/TableRow';
import DownloadImage from '../../download.png';

const Map = (props) => {
  const [center, set_center] = useState({lat: 53.344, lng: -6.267});
  const [locations, set_locations] = useState([]);
  const [entries, set_entries] = useState([]);
  const [filtered_entries, set_filtered_entries] = useState([]);
  const [table_rows, set_table_rows] = useState({});
  
  const get_sightings = (token) => {
    fetch('http://localhost:8080/api/getUserDex', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        'token': token
      })
    }).then((response) => {
      const status = response.status;
      return response.text().then((body) => {
        return { status, body };
      });
    }).then(({ status, body }) => {
      if (status == 200) {
        let json_obj = JSON.parse(body);
        set_entries(json_obj);
      } else if (status == 401) {
        
      }
    })
  };

  const download_data = () => {
    let csv_content = "";
    let raw_data = entries;
  
    for (let i = 0; i < raw_data.length; i++) {
      if (raw_data[i]['hide_status'] == false) {
        csv_content += `${raw_data[i]['SpeciesName']},${raw_data[i]['Latitude']},${raw_data[i]['Longitude']}\r\n`;
      }
    }
  
    const blob = new Blob([csv_content], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", "location_data.csv");
    
    document.body.appendChild(link); 
    link.click();
    
    document.body.removeChild(link); 
  };

  const search_filter = () => {
    var search_term = document.getElementById("map_subtext_filter").value; 
    var existing_entries = entries;
    var temp_entrys = {};
    var temp_entrys_list = [];

    for (var idx = 0; idx < existing_entries.length; idx++) {
      if ((existing_entries[idx]['PokemonName'].includes(search_term) || existing_entries[idx]['SpeciesName'].includes(search_term))) {
        temp_entrys[existing_entries[idx]['SightingID']] = existing_entries[idx];
        if (existing_entries[idx]['hide_status'] == false) {
          temp_entrys_list.push(existing_entries[idx]);
        }
      }
    }

    set_table_rows(temp_entrys);
    set_filtered_entries(temp_entrys_list);
  }

  const apply_filter = (entity_id, hide_status) => {
    var current_entities = table_rows;
    var temp_entities = [];

    for (var key in current_entities) {
      if (entity_id == key) {
        current_entities[key]['hide_status'] = hide_status;
      }

      if (current_entities[key]['hide_status'] == false) {
        temp_entities.push(current_entities[key]);
      }
    }

    set_filtered_entries(temp_entities);
  };

  useEffect(() => {
    get_sightings(sessionStorage.getItem('token'));
  },[]);

  useEffect(() => {
    var temp_entrys = entries;
    for (var idx = 0; idx < temp_entrys.length; idx++) {
      temp_entrys[idx]['hide_status'] = false; 
    }
    set_filtered_entries(temp_entrys);
    search_filter();
  },[entries]);

  useEffect(() => {
    var existing_locations = filtered_entries;

    if (filtered_entries.length > 0) {
      let temp_locations = []

      let sum_lat = 0; 
      let sum_lng = 0; 
  
      for (var idx = 0; idx < existing_locations.length; idx++) {
        let lng = existing_locations[idx]['Longitude'];
        let lat = existing_locations[idx]['Latitude'];
        
        sum_lat += lat;
        sum_lng += lng; 
        temp_locations.push({lat:lat,lng:lng});
      }
      
      var center_lat = sum_lat / temp_locations.length;
      var center_lng = sum_lng / temp_locations.length;
  
      set_center({lat:center_lat, lng: center_lng});
      set_locations(temp_locations);
    } else {
      set_locations([]);
    }

  },[filtered_entries]);

  return (
    <Template> 
      <div class='map_content_wpr'>
        
        <p class='title_font center'>Map</p>
        <div onClick={() => download_data()} class='download_loc_data_image_wpr'>
          <img class='download_loc_data_image' src={DownloadImage}/>
        </div>
        
        
        <div class='map_wpr'>
          <CommonContainer style_obj={{width:"100%", height:"100%"}}>
              <LoadScript googleMapsApiKey='AIzaSyDZBMnM05MTnUuJ0YaR6nvTr7iF3bk9GGk'>
                <GoogleMap
                  mapContainerStyle={{width:"100%", height:"100%"}}
                  center={center}
                  zoom={2}
                >

                  {locations.map((location, index) => (
                    <MarkerF
                      key={index} 
                      position={location}
                    />
                  ))}
                  
                </GoogleMap>
              </LoadScript>
          </CommonContainer>
        </div>
        
        <div class='control_elements'>
          <div class='filter_wpr'>
            <TextFeild field_id="map_subtext_filter" style_obj={{width:"100%", height:"20px"}} placeholder="Search" />
          </div>

          <div class='filter_wpr filter_btn_wpr'>
            <Button trig_function={search_filter} style_obj={{width:"100%", height:"20px"}} txt="Search" />
          </div>
        </div>

        <div class='table_elements_wpr'>
            <div class='table_elements'>
              <CommonContainer style_obj={{width:"100%", height:"90%"}}>
                <div class='dummy_background_colour'>

                  {Object.entries(table_rows).map(([key, row]) => (
                    <TableRow filter_function={apply_filter} pokemon_data={row} key={row.SightingID || key}/>   
                  ))}
                
                </div>
              </CommonContainer>
            </div>
        </div>

      </div>



    </Template>
  );
}

export default Map;