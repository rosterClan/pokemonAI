import {useNavigate} from "react-router-dom";

import './ActivityRow.css';
import { useEffect, useState } from 'react';

const ActivityRow = (props) => {
    const navigate = useNavigate();
    const [activity_data, set_activity_data] = useState(props.activity_data);
    const [timestamp, set_timestamp]  = useState(new Date());
    const [realIp, setRealIp] = useState("");

    useEffect(() => {
        let ip = "";
        for (var i = 0; i < 4; i++) {
            ip += Math.floor(Math.random() * (255 - 1) + 1);
            if (i+1 < 4) {
                ip += ".";
            }
        }
        setRealIp(ip);
    }, []);

    useEffect(() => {
        set_activity_data(props.activity_data)
        set_timestamp(new Date(activity_data['Date']));
    },[activity_data]);

    return (
        <div class='row_wpr'>
            <div class='row_name'>
                <p>{activity_data['Type']} - {timestamp.toLocaleString()} ({Intl.DateTimeFormat().resolvedOptions().timeZone} Time) IP: {realIp}</p>
            </div>
        </div>
    );
}

export default ActivityRow;