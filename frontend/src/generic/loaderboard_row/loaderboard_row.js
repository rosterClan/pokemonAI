import { useEffect, useRef, useState } from 'react';
import { get_userID_dex } from '../../helper';
import {useNavigate} from "react-router-dom";

import './loaderboard_row.css';

import CommonContainer from '../CommonContainer/CommonContainer';
import DummyImage from './static_assets/profile_image.png';

const LeaderboardRow = (props) => {
    const navigate = useNavigate();

    const open_user_dex = (userID) => {
        navigate("/UserDex", { state: { userID: userID, userName: props.details['userName']} });
    };

    return (
        <div class='leaderboard_row_style_wpr'>
            <CommonContainer>
                <div onClick={() => open_user_dex(props.details['userID'])} class="leaderboard_row_content_wpr">
                    <div class='leaderboard_img_dummy'>
                        <img src={DummyImage} />
                    </div>
                    <div class='leaderboard_main_content'>
                        <div class='leaderboard_title'>
                            <p>{props.details['userName']}</p>
                        </div>
                        <div class='leaderboard_details'>
                            <p>Num Captures: {props.details['numSightings']}</p>
                        </div>
                    </div>
                </div>
            </CommonContainer>
        </div>
    );
}

export default LeaderboardRow;
