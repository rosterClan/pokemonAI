import './text_input.css';

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

function TextFeild(props) {

  return (
    <div id='test'>
      <CustomContainer style_obj={props.style_obj} 
        top={top}
        bottom={bottom}
        top_left={top_left}
        top_right={top_right}
        bottom_left={bottom_left}
        bottom_right={bottom_right}
        left={left}
        right={right}>

          <div class='inner_feild' style_obj={props.style_obj}>
            <input id={props.field_id} class="input_feild" type="text" placeholder={props.placeholder} />
          </div>

      </CustomContainer>
    </div>
  );
}

export default TextFeild;