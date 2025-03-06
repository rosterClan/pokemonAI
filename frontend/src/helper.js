import QuestionMark from "./question_mark.png";
import {useNavigate} from "react-router-dom";

const Unauthorised_token_procedure = () => {
    const navigate = useNavigate();
    sessionStorage.setItem('token', null);
    navigate("/login", {default_error: "unauthorized token"});
};

export function getUserDetails(token, responce_function) {
  console.log("tryinbg to login");
  fetch('http://localhost:8080/api/getUser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      'token': token
    })
  }).then((response) => {
    const status = response.status;
    return response.text().then((body) => {
      return { status, body };
    });
  }).then(({ status, body }) => {
    let json_obj = JSON.parse(body);
    if (status == 200) {
      responce_function(json_obj);
    } 
  })
}

export function login_handler(username, password, responce_function, error_function) {
  console.log("tryinbg to login");
  fetch('http://localhost:8080/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      'username': username,
      'password': password
    })
  }).then((response) => {
    const status = response.status;
    return response.text().then((body) => {
      return { status, body };
    });
  }).then(({ status, body }) => {
    let json_obj = JSON.parse(body);
    if (status != 200) {
      sessionStorage.setItem('token', null);
      error_function(json_obj['error']);
    } else {
      sessionStorage.setItem('token', json_obj['token']);
      responce_function();
    }
  })
};

export function upload_image(token, image, responce_function, error_function) {
  fetch('http://localhost:8080/api/performScan', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      'token': token,
      'image': image
    })
  }).then((response) => {
    const status = response.status;
    return response.text().then((body) => {
      return { status, body };
    });
  }).then(({ status, body }) => {
    let json_obj = JSON.parse(body);
    console.log("error test", json_obj);
    if (status == 200) {
      responce_function(json_obj);
    } else if (status == 401) {
      Unauthorised_token_procedure();
    } else {
      error_function("Image Processing Error");
    }
  })
};

export function get_userID_dex(token, desired_user, responce_function) {
    fetch('http://localhost:8080/api/getExternalUserDex', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        'token': token,
        'queryID': desired_user
      })
    }).then((response) => {
      const status = response.status;
      return response.text().then((body) => {
        return { status, body };
      });   
    }).then(({ status, body }) => {
      if (status == 200) {
        var json_obj = JSON.parse(body);
        responce_function(json_obj);
      } else if (status == 401) {
        Unauthorised_token_procedure();
      }
    })
};

export function dex_handler(token, responce_function) {
    fetch('http://localhost:8080/api/getUserDex', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        'token': token
      })
    }).then((response) => {
      const status = response.status;
      return response.text().then((body) => {
        return { status, body };
      });   
    }).then(({ status, body }) => {
      if (status == 200) {
        var json_obj = JSON.parse(body);
        responce_function(json_obj);
      } else if (status == 401) {
        Unauthorised_token_procedure();
      }
    })
};

export function get_pokemon_image(token, SightingID, responce_function) {
    fetch('http://localhost:8080/api/getPokemonImageById', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          'token': token,
          'SightingID': SightingID
        })
      }).then((response) => {
        const status = response.status;
        return response.text().then((body) => {
          return { status, body };
        });
      }).then(({ status, body }) => {
        var json_obj = JSON.parse(body);
        if (status == 200) {
            responce_function(`data:image/png;base64,${json_obj['image']}`)
        } else if (status == 404) {
            responce_function(image_not_found);
        } else if (status == 401) {
            Unauthorised_token_procedure();
        }
    })
}

export function get_leaderboard(token, responce_function) {
    fetch('http://localhost:8080/api/leaderboard', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          'token': token
        })
      }).then((response) => {
        const status = response.status;
        return response.text().then((body) => {
          return { status, body };
        });
      }).then(({ status, body }) => {
        var json_obj = JSON.parse(body);
        if (status == 200) {
            responce_function(json_obj)
        } else if (status == 404) {
          responce_function(image_not_found)
        } else if (status == 401) {
            Unauthorised_token_procedure();
        }
    })
}

export function get_attribute_category_image(token, sightingID, responce_function) {
  fetch('http://localhost:8080/api/getAttributeCategoriesImages', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      'sightingID': sightingID,
      'token': token
    })
  }).then((response) => {
    const status = response.status;
    return response.text().then((body) => {
      return { status, body };
    });
  }).then(({ status, body }) => {
    let json_obj = JSON.parse(body);
    if (status == 200) {
      responce_function(json_obj);
    } else if (status == 401) {
      Unauthorised_token_procedure();
    } else {
      responce_function(image_not_found);
    } 
})
}

export const get_pokemon_entry = (token, sightingID, responce_function) => {
  fetch('http://localhost:8080/api/getSightingByID', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      'token': token,
      'sightingID' : sightingID
    })
  }).then((response) => {
    const status = response.status;
    return response.text().then((body) => {
      return { status, body };
    });
  }).then(({ status, body }) => {
    if (status == 200) {
      var json_obj = JSON.parse(body);
      responce_function(json_obj);
    } else if (status == 401) {
      Unauthorised_token_procedure();
    }
  })
};

export const image_not_found = QuestionMark;



