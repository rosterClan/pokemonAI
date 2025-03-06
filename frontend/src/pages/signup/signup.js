import './signup.css';

import {useNavigate} from "react-router-dom";

import Template from '../template/template';
import Button from '../../generic/button/button';
import TextFeild from '../../generic/text_input/text_input';

function SignUp() {
  const navigate = useNavigate();

  const signup_handler = (username, password_1, password_2, email) => {
    fetch('http://localhost:8080/api/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        'username': username,
        'password1': password_1,
        'password2': password_2,
        'email': email,
        'securityQuestion': '',
        'securityAnswer': '',
        'role': 'user'
      })
    }).then((response) => {
      const status = response.status;
      return response.text().then((body) => {
        return { status, body };
      });
    }).then(({ status, body }) => {
      let json_obj = JSON.parse(body);
      document.getElementById('error_txt').innerText = "successful signup";
      if (status != 200) {
        document.getElementById('error_txt').innerText = json_obj['error'];
        sessionStorage.setItem('token', null);
      } else {
        sessionStorage.setItem('token', json_obj['token']);
        navigate("/myDex");
      }
    })
  };

  const signup_function = () => {
    var username = document.getElementById('username_input').value.trim();
    var password = document.getElementById('password_input').value.trim();
    var password_verify = document.getElementById('username_input_verify').value.trim();
    var email_input = document.getElementById('email_input').value.trim();

    var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    var error_state = false; 
    var error_msg = '';

    if (username.length == 0 || password.length == 0 || email_input.length == 0) {
      error_msg += "Required feilds are empty\n";
      error_state = true; 
    } else if (password != password_verify) {
      error_msg += "Entered passwords don't match\n";
      error_state = true; 
    } else if (emailPattern.test(email_input) == false) {
      error_msg += "Provided email is invalid\n";
      error_state = true; 
    }

    if (error_state == false) {
      signup_handler(username, password, password_verify, email_input);
    } else {
      alert(error_msg);
    }

  };

  return (
    <Template> 
      <p class='title_font center'>Scan</p>
      <p class='error_txt' id='error_txt'></p>

      <div class='signup_wpr'>
        <div class='signup_body'>

          <div class='signup_label_column column_display'>
            <div class='row'>
              <p>Username:</p>
            </div>
            <div class='row'>
              <p>Password:</p>
            </div>
            <div class='row'>
              <p>Confirm Password:</p>
            </div>
            <div class='row'>
              <p>Email:</p>
            </div>
          </div>

          <div class='signup_input_feild_column column_display'>
            <div class='row row_feild_input'>
              <div class='input_element input_form'>
                <TextFeild field_id="username_input" style_obj={{width:"300px", height:"20px"}} />
              </div>
            </div>
            <div class='row row_feild_input'>
              <div class='input_element input_form'>
                <TextFeild field_id="password_input" style_obj={{width:"300px", height:"20px"}} />
              </div>
            </div>
            <div class='row row_feild_input'>
              <div class='input_element input_form'>
                <TextFeild field_id="username_input_verify" style_obj={{width:"300px", height:"20px"}} />
              </div>
            </div>
            <div class='row row_feild_input'>
              <div class='input_element input_form'>
                <TextFeild field_id="email_input" style_obj={{width:"300px", height:"20px"}} />
              </div>
            </div>
          </div>

        </div>

        <div class='signup_btn_wpr'>
          <Button trig_function={signup_function} style_obj={{width:"200px", height:"20px"}} txt="Submit" />
        </div>



      </div>

    </Template>
  );
}

export default SignUp;