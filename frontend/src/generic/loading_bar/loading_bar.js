import { useEffect, useRef, useState } from 'react';
import './loading_bar.css';

import BodyContent from '../CustomContainer/CustomContainer';

import top_left from './static_assets/top_left.png'
import top_right from './static_assets/top_right.png'

import bottom from './static_assets/bottom.png'
import top from './static_assets/top.png'

import bottom_left from './static_assets/bottom_left.png'
import bottom_right from './static_assets/bottom_right.png'

import left from './static_assets/left_straight.png'
import right from './static_assets/right_straight.png'

import BarLoader from './bar_loader/bar_loader';

const LoadingBar = () => {
    const ref = useRef(null);
  
    return (
        <div ref={ref} class='loading_bar'>
        <BodyContent 
            top={top}
            bottom={bottom}
            top_left={top_left}
            top_right={top_right}
            bottom_left={bottom_left}
            bottom_right={bottom_right}
            left={left}
            right={right} >
                <div class='loading_bar_backdrop'>
                    <BarLoader reference={ref}/>
                </div>
        </BodyContent>
        </div>
    );
}

export default LoadingBar;
