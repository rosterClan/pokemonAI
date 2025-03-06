import './login.css';

import Template from '../template/template';
import Button from '../../generic/button/button';
import TextFeild from '../../generic/text_input/text_input';

import {useNavigate} from "react-router-dom";
import { useEffect, useState } from 'react';
import { login_handler } from '../../helper';

const Login = (props) => {
  const [error, set_error] = useState(props.default_error);
  const navigate = useNavigate();

  useEffect(() => {
    sessionStorage.setItem('token', null);
    if (error == null) {
      set_error("");
    }
  },[]);

  const responce_function = () => {
    navigate("/myDex");
  }

  const error_function = (errorTxt) => {
    set_error(errorTxt);
  }

  const login = () => {
    let password = document.getElementById('password_entry').value.trim();
    let user_name = document.getElementById('name_entry').value.trim();
    login_handler(user_name, password, responce_function, error_function);
  };

  return (
    <Template> 
      <p class='title_font center'>Login</p>
      <p class='error_txt' id='error_txt'>{error}</p>

      <div class="login_body">
        <div class='dummy_wpr'>

          <div class='login_btn_wpr'>
            <div class='input_label input_form'>
              <p>Username:</p>
            </div>
            <div class='input_element input_form'>
              <TextFeild field_id="name_entry" style_obj={{width:"300px", height:"20px"}} />
            </div>
          </div>

          <div class='login_btn_wpr extra_padding'>
            <div class='input_label input_form'>
              <p>Password:</p>
            </div>
            <div class='input_element input_form'>
              <TextFeild field_id="password_entry" style_obj={{width:"300px", height:"20px"}} />
            </div>
          </div>

          <div class='login_btn_wpr'>
            <Button trig_function={login} style_obj={{width:"200px", height:"20px"}} txt="Login" />
          </div>

        </div>
      </div>
    </Template>
  );
}

export default Login;