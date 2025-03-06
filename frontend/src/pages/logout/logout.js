import Template from '../template/template';
import {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";

const Logout = (props) => {
    const [error, set_error] = useState(props.default_error);
    const navigate = useNavigate();

    useEffect(() => {
        log_out(sessionStorage.getItem('token'));
    },[]);
  
    const log_out = (token) => {
        fetch('http://localhost:8080/api/logout', {
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
            let json_obj = JSON.parse(body);
            set_error("");
            if (status != 200) {
                set_error(json_obj['error']);
                navigate("/myDex");
            } else {
                sessionStorage.setItem('token', null)
                navigate("/login");
            }
        })
    };


  return (
    <Template> 
    </Template>
  );
}

export default Logout;