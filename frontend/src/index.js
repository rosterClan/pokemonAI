import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {BrowserRouter, Routes, Route, Link, NavLink} from 'react-router-dom';

import Login from './pages/login/login';
import MyDex from './pages/my_dex/my_dex';
import Map from './pages/map/map';
import Scan from './pages/scan/scan';
import LeaderBoard from './pages/leaderboard/leaderboard';
import Profile from './pages/profile/profile';
import EntryPage from './pages/entry_page/entry_page';
import Activity_log from './pages/activity_log/activity_log';
import Logout from './pages/logout/logout';

import Footer from './pages/template/footer/footer';
import SignUp from './pages/signup/signup';
import ScanViewImage from './pages/scan_view_image/ScanViewImage';
import Process from './pages/processes/processes';
import GetAltUserDex from './pages/other_usr_dex/other_usr_dex';
import ErrorPage from './pages/error_page/error_page';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(

    
    <BrowserRouter>
      <Routes>
        <Route path='*' element={<ErrorPage />} />

        <Route path="/" element={<Login/>}/>
        <Route index path="/login" element={<Login/>}/>
        <Route path="/signup" element={<SignUp/>}/>

        <Route path="/scan" element={<Scan/>}/>
        <Route path="/scan_confirm" element={<ScanViewImage/>}/>
        <Route path="/loading" element={<Process/>}/>

        <Route index path="/UserDex" element={<GetAltUserDex/>}/>

        <Route path="/myDex" element={<MyDex/>}/>
        <Route path="/map" element={<Map/>}/>
        <Route path="/leaderboard" element={<LeaderBoard/>}/>
        <Route path="/profile" element={<Profile/>}/>

        <Route path="/error" element={<ErrorPage />}/>

        <Route path="/entry/:SightingID" element={<EntryPage/>}/>

        <Route path="/activity_log" element={<Activity_log/>}/>
        <Route path="/logout" element={<Logout/>}/>
      </Routes>
      <Footer />
    </BrowserRouter>


);