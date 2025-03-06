import { useEffect, useRef, useState } from 'react';
import './CustomContainer.css';

const BodyContent = ({style_obj, top_left, top_right, top, bottom_left, bottom_right, bottom, left, right, children}) => {
  const [ajusted_height, set_ajusted_height] = useState(0);
  const childRef = useRef(null);

  useEffect(() => {
    if (childRef.current) {
      set_ajusted_height(childRef.current.getBoundingClientRect().height);
    }
  },[children, style_obj]);

  return (
    <div ref={childRef} class='bodyContent' style={style_obj}>
      <div class='box template_background' />

      <div class='box'>
        {children}
      </div>

      <div class='box non_clickable'>
          <div class="frame_wpr">
            <div class='body_frame_corner' style={{
              backgroundImage: 'url("'+top_left+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
            }}/>

            <div class='body_frame' style={{
              backgroundImage: 'url("'+top+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
            }}/>

            <div class='body_frame_corner' style={{
              backgroundImage: 'url("'+top_right+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
            }}/>
          </div>

          <div class="frame_wpr frame_wpr_middle"  style={{height:ajusted_height}}>
              <div class='body_frame_verticle' style={{
                backgroundImage: 'url("'+left+'")',
                backgroundSize: '100% 100%',
                backgroundRepeat: 'no-repeat'
              }}/>

              <div class='body_content_fr'/>

              <div class='body_frame_verticle' style={{
                backgroundImage: 'url("'+right+'")',
                backgroundSize: '100% 100%',
                backgroundRepeat: 'no-repeat'
              }}/>
          </div>

          <div class="frame_wpr">
            <div class='body_frame_corner' style={{
              backgroundImage: 'url("'+bottom_left+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
            }}/>
            <div class='body_frame' style={{
              backgroundImage: 'url("'+bottom+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
            }}/>
            <div class='body_frame_corner' style={{
              backgroundImage: 'url("'+bottom_right+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
            }}/>
          </div>
      </div>



    </div>
  );
}

export default BodyContent;
