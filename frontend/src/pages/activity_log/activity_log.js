import './activity_log.css';
import {useEffect, useState} from 'react';

import Template from '../template/template';
import CommonContainer from '../../generic/CommonContainer/CommonContainer';

import ActivityRow from '../../generic/ActivityRow/ActivityRow';

const Activity_log = (props) => {
  const [table_rows, set_table_rows] = useState({});
  
  const get_activities = (token) => {
    fetch('http://localhost:8080/api/getUserLog', {
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
        set_table_rows(json_obj);
      }
    })
  };

  useEffect(() => {
    get_activities(sessionStorage.getItem('token'));
  },[]);
  
  return (
    <Template> 
      <div class='activity_content_wpr'>
        
        <p class='title_font center'>Activity Log</p>
        

        <div class='table_elements_wpr'>
            <div class='table_elements'>
              <CommonContainer style_obj={{width:"100%", height:"90%"}}>
                <div class='dummy_background_colour'>

                  {Object.entries(table_rows).map(([key, row]) => (
                    <ActivityRow activity_data={row} key={row.Type || key}/>   
                  ))}
                
                </div>
              </CommonContainer>
            </div>
        </div>

      </div>



    </Template>
  );
}

export default Activity_log;