import './leaderboard.css';

import Template from '../template/template';
import { useEffect, useRef, useState } from 'react';
import LeaderboardRow from '../../generic/loaderboard_row/loaderboard_row';
import { get_leaderboard } from '../../helper';

function LeaderBoard() {
  const [entries, set_entries] = useState([]); 

  useEffect(() => {
    get_leaderboard(sessionStorage.getItem('token'), set_entries);
  },[]);

  return (
    <Template> 
      <p class='title_font center'>LeaderBoard</p>

      <div class='leaderboard_row_wpr'>
        {entries.map((item, index) => (
          <LeaderboardRow id={item['userID']} details={item} />
        ))}
      </div>

    </Template>
  );
}

export default LeaderBoard;