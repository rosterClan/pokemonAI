import './footer_btn.css';
import unit_holder from './static_asset/unit_holder.png';
import btn_square from './static_asset/btn_square.png';

const FooterBtn = ({click_function, icon}) => {
  return (
    <div onClick={click_function} class='footer_btn_wpr footer_overlay'>

      <div class='unit_holder-1'>
        <div class='icon_img' style={{
          backgroundImage: 'url("'+unit_holder+'")',
          backgroundSize: '100% 100%',
          backgroundRepeat: 'no-repeat'
        }}/>    
      </div>


      <div class='unit_holder-2 footer_overlay'>
        <div class='footer_holder_1'>
          <div class='unit_holder_btn' style={{
              backgroundImage: 'url("'+btn_square+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
          }}/>
        </div>
        <div class='footer_holder_2'>
          <div class='custom_footer_icon' style={{
              backgroundImage: 'url("'+icon+'")',
              backgroundSize: '100% 100%',
              backgroundRepeat: 'no-repeat'
          }}/>
        </div>
      </div>
      

    </div>

  );
}

export default FooterBtn;