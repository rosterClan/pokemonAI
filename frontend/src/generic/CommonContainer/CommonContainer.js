

import { useEffect, useRef, useState } from 'react';
import './CommonContainer.css';

import CustomContainer from '../CustomContainer/CustomContainer'

import top_left from './static_assets/top_left.png'
import top_right from './static_assets/top_right.png'

import bottom from './static_assets/bottom.png'
import top from './static_assets/top.png'

import bottom_left from './static_assets/bottom_left.png'
import bottom_right from './static_assets/bottom_right.png'

import left from './static_assets/left_straight.png'
import right from './static_assets/right_straight.png'

const CommonContainer = ({style_obj, children}) => {
  return (
      <CustomContainer style_obj={style_obj} 
        top={top}
        bottom={bottom}
        top_left={top_left}
        top_right={top_right}
        bottom_left={bottom_left}
        bottom_right={bottom_right}
        left={left}
        right={right}>
      {children}
      </CustomContainer>
  );
}

export default CommonContainer;
