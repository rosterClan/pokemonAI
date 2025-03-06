import { useEffect, useState } from 'react';
import {useNavigate, useLocation} from "react-router-dom";

import './ScanViewImage.css';

import Template from '../template/template';
import Button from '../../generic/button/button';

const ScanViewImage = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const base64data = location.state?.base64data;

  const cancel_action = () => {
    navigate('/scan');
  };

  const submit_action = () => {
    navigate('/loading', { state: { base64data } });
  }

  return (
    <Template> 
      <div class='scan_confirm_page_wpr'>
        <div class='title_txt'>
          <p>Scan  New  Entry</p>
        </div>
        
        <div class='upload_confirm_controls_wpr'>
          <div class='upload_confirm_control_image'>
            <img src={base64data}/>
          </div>

          <div class='upload_control_control_btn'>
            <Button trig_function={submit_action} style_obj={{width:"100%", height:"20px"}} txt="Submit" />
          </div>
          <div class='upload_control_control_btn'>
            <Button trig_function={cancel_action} style_obj={{width:"100%", height:"20px"}} txt="Cancel" />
          </div>
        </div>

      </div>
    </Template>
  );
}

export default ScanViewImage;