import './footer.css';

import left_art_content from './static_asset/left_art_content.png';
import right_art_content from './static_asset/right_art_content.png';

import filler from './static_asset/filler.png'
import leftBoarder from './static_asset/left_border.png';
import rightBoarder from './static_asset/right_border.png';
import FooterBtn from './unit_holder/footer_btn';

import trophy from './static_asset/trophy.png';
import profile from './static_asset/profile.png';
import save from './static_asset/save.png';
import camrea from './static_asset/camrea.png'
import map from './static_asset/map.png'

import {useNavigate} from 'react-router-dom';

function Footer() {
  const navigate = useNavigate(); 

  const scetch_fix = () => {
    navigate("/map");
    window.location.replace(window.location.href);
  };

  return (
    <div class='footer'>
      <div class='corner' style={{
        backgroundImage: 'url("'+filler+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      <div class='art_content' style={{
        backgroundImage: 'url("'+left_art_content+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      <div class='corner' style={{
        backgroundImage: 'url("'+filler+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      <div class='footer_round_off' style={{
        backgroundImage: 'url("'+rightBoarder+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>
      
      <div class='content'>
        <FooterBtn click_function={()=>navigate("/leaderboard")} icon={trophy}/>
        <FooterBtn click_function={()=>navigate("/profile")} icon={profile}/>
        <FooterBtn click_function={()=>navigate("/scan")} icon={camrea}/>
        <FooterBtn click_function={()=>navigate("/MyDex")} icon={save}/>
        <FooterBtn click_function={()=>scetch_fix()} icon={map}/>
      </div>

      <div class='footer_round_off' style={{
        backgroundImage: 'url("'+leftBoarder+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      <div class='corner' style={{
        backgroundImage: 'url("'+filler+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      <div class='art_content' style={{
        backgroundImage: 'url("'+right_art_content+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      <div class='corner' style={{
        backgroundImage: 'url("'+filler+'")',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat'
      }}/>

      
    </div>
  );
}

export default Footer;