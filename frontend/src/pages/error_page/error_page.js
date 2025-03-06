import './error_page.css';

import Template from '../template/template';
import ErrorImage from './static_assets/ErrorImage.png';

import { useState } from 'react';
import { useLocation } from "react-router-dom";

function ErrorPage() {
  const location = useLocation();
  const { error } = location.state || {};

  

  return (
    <Template> 

      <div class='errorWpr'>
        <div class='errorImage'>
          <img src={ErrorImage}/>
        </div>
        <div class='errorTxt'>
          <p>{error}</p>
        </div>
      </div>

    </Template>
  );
}

export default ErrorPage;