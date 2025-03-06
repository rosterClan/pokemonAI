import { useEffect, useRef, useState } from 'react';
import './bar_loader.css';

const BarLoader = (props) => {
    const [left_offset, set_left_offset] = useState(0);
    const ref = useRef(null);

    setTimeout(() => { 
        var older_offset = left_offset;
        older_offset++;

        if (props.reference.current && ref.current) {
            var parent_element = props.reference.current;
            var curr_bar = ref.current;

            var curr_bar_width = curr_bar.getBoundingClientRect().width; 
            var parent_element_width = parent_element.getBoundingClientRect().width;

            var curr_bar_left_offset = Number(curr_bar.style.marginLeft.replace("px", ""));

            if (curr_bar_width+curr_bar_left_offset < parent_element_width) {
                set_left_offset(older_offset);
            } else {
                set_left_offset(0);
            }
        }
    }, 10);


    return (
        <div ref={ref} style={{marginLeft:left_offset}} className='bar_loader'>
            <div class="bar_loader_bar" />
            <div class="bar_loader_bar" />
            <div class="bar_loader_bar" />
            <div class="bar_loader_bar" />
            <div class="bar_loader_bar" />
        </div>
    );
}

export default BarLoader;
