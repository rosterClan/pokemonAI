import { useEffect, useState } from 'react';
import './template.css';
 
import CustomContainer from '../../generic/CustomContainer/CustomContainer.js';
import CommonContainer from '../../generic/CommonContainer/CommonContainer.js';

import top_left from './static_assets/top_left.png'
import top_right from './static_assets/top_right.png'

import bottom from './static_assets/bottom.png'
import top from './static_assets/top.png'

import bottom_left from './static_assets/bottom_left.png'
import bottom_right from './static_assets/bottom_right.png'

import left from './static_assets/left_straight.png'
import right from './static_assets/right_straight.png'

const Template = ({prop, children}) => {
    const [screenHeight, setScreenHeight] = useState(0);

    const change_size = () => {
      let bodyElement = document.getElementsByTagName('body')[0];
      let elementHeight = bodyElement.offsetHeight;
      setScreenHeight(elementHeight-21);
    }

    useEffect(() => {
      window.addEventListener('resize',change_size);
      change_size();
      return () => window.removeEventListener('resize',change_size);
    },[]);

    const style_obj = {
      width:"100%",
      height:"100%"
    }

  return (
    <div style={{height:screenHeight}} class='main_divider'>
        <CommonContainer style_obj={style_obj}>
          {children}
        </CommonContainer>
    </div>
  );
}

export default Template;
