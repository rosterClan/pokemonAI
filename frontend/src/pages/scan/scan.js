import { useEffect, useState } from 'react';
import {useNavigate} from "react-router-dom";

import './scan.css';

import Template from '../template/template';
import Button from '../../generic/button/button';

const Scan = (props) => {
  const navigate = useNavigate();

  const dummy_upload_file = () => {
    document.getElementById('fileInput').click();
  }

  const handle_change = (event) => {
    let file = event.target.files[0];
    if (file) {
      let reader = new FileReader();
      reader.onload = (e) => {
        let base64data = e.target.result;
        navigate("/scan_confirm", { state: { base64data } });
      }
      reader.readAsDataURL(file);
    }
  }

  return (
    <Template> 
      <input class='hidden_element' type="file" id="fileInput" onChange={handle_change} accept="image/*"/>

      <div class='scan_page_wpr'>
        <div class='title_txt'>
          <p>Scan  New  Entry</p>
        </div>
        
        <div class='upload_controls_wpr'>
          <div class='upload_control_btn'>
            <Button trig_function={dummy_upload_file} style_obj={{width:"100%", height:"20px"}} txt="Upload Image" />
          </div>
          <div class='upload_control_txt'>
            <p>OR</p>
          </div>
          <div class='upload_control_btn'>
            <Button trig_function={null} style_obj={{width:"100%", height:"20px"}} txt="Take Photo" />
          </div>
        </div>

      </div>
    </Template>
  );
}

export default Scan;