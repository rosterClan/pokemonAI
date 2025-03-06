import { useEffect, useState } from 'react';
import {useNavigate, useLocation} from "react-router-dom";

import './processes.css';

import Template from '../template/template';
import Button from '../../generic/button/button';
import LoadingBar from '../../generic/loading_bar/loading_bar';

import { upload_image } from '../../helper';

const Process = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const base64data = location.state?.base64data;

  const responce_function = (data) => {
    console.log("/entry/" + data['sightingID']);
    navigate("/entry/" + data['sightingID']);
  }

  const error_function = (data) => {
    navigate("/error", { state: { error: data} });
  }

  useEffect(() => {
    if (base64data != null) {
      upload_image(sessionStorage.getItem('token'), base64data, responce_function, error_function);
    }
  }, []);
  
  return (
    <Template> 
      <div class='processor_bar'>
        
        
        <div class='loading_bar_wpr'>
          <div class='loading_bar_title'>
            <p>PROCESSING</p>
          </div>
          <div class='loading_bar_component'>
            <LoadingBar />
          </div>
        </div>
        

      </div>
    </Template>
  );
}

export default Process;