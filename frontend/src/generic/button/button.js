import './button.css';

import { useEffect, useState } from 'react';

import CustomContainer from '../CustomContainer/CustomContainer'

import top_left from './static_assets/top_left.png'
import top_right from './static_assets/top_right.png'

import bottom from './static_assets/bottom.png'
import top from './static_assets/top.png'

import bottom_left from './static_assets/bottom_left.png'
import bottom_right from './static_assets/bottom_right.png'

import left from './static_assets/left_straight.png'
import right from './static_assets/right_straight.png'

function Button(props) {

  const activate_function = () => {
    if (props.trig_function != null) {
      props.trig_function();
    } else {
      //console.log("no function set");
    }
  };

  return (
    <div id='test' onClick={activate_function}>
      <CustomContainer style_obj={props.style_obj} 
        top={top}
        bottom={bottom}
        top_left={top_left}
        top_right={top_right}
        bottom_left={bottom_left}
        bottom_right={bottom_right}
        left={left}
        right={right}>
          <div class='inner_btn' style_obj={props.style_obj}>
            <p class="title_font unselectable">{props.txt}</p>
          </div>
      </CustomContainer>
    </div>
  );
}

export default Button;