import React, { useState, useEffect } from 'react';
import './profile.css';

import TextFeild from '../../generic/text_input/text_input';
import Button from '../../generic/button/button';
import Template from '../template/template';
import { getUserDetails } from '../../helper';

const Profile = () => {
  const [formData, setFormData] = useState({
    password: '',
    email: ''
  });
  const [token, setToken] = useState('');
  const [error, setError] = useState(''); // For error feedback
  const [message, setMessage] = useState(''); // For success feedback

  const populateData = (data) => {
    console.log("here is data", data);
    document.getElementById("password_text").value = data['password'];
    document.getElementById("email_text").value = data['email'];
  }

  useEffect(() => {
    // Fetch token from sessionStorage
    const savedToken = sessionStorage.getItem('token');
    if (savedToken) {
      setToken(savedToken);
    }

    getUserDetails(savedToken, populateData);
  }, []);

  // Function to handle profile updates
  const updateProfile = () => {
    if (!sessionStorage.getItem('token')) {
      setError('Error: Missing token. Cannot update profile.');
      return;
    }

    // Send the request to update the user's profile
    fetch('http://localhost:8080/api/updateUser', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        'token': sessionStorage.getItem('token'), // Send the token in the body
        'email': document.getElementById("email_text").value,  // Send updated email
        'password': document.getElementById("password_text").value // Send updated password
      })
    })
    .then((response) => {
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        // If response is JSON, parse it as JSON
        return response.json().then((body) => {
          return { status: response.status, body };
        });
      } else {
        // If response is plain text, return it as text
        return response.text().then((body) => {
          return { status: response.status, body };
        });
      }
    })
    .then(({ status, body }) => {
      if (status !== 200) {
        if (typeof body === 'object' && body.error) {
          setError(body.error);
        } else {
          setError(body || 'Failed to update profile');
        }
      } else {
        setMessage(body || 'Profile updated successfully!');
        setError('');
      }
    })
    .catch((error) => {
      console.error('Error updating profile:', error);
      setError('Error updating profile. Please try again.');
    });
  };

  // Input field change handler
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  return (
    <Template>
      <div className="cutaway"></div>
      <div className="profile-avatar"></div>
      <form className="profile-form" onSubmit={(e) => e.preventDefault()}>
        <div className="title_txt_username">
          <p>Username</p>
        </div>

      {/* Display error or success messages */}
      {error && <p className="error-message">{error}</p>}
      {message && <p className="success-message">{message}</p>}
        
        <div className="input_label">
          <p>Password</p>
        </div>

        <div className="input-wrapper">
          <TextFeild
            label="Password"
            field_id="password_text"
            name="password"
            value={formData.password}
            placeholder="**************"
            onChange={handleChange}
            style_obj={{width:"100%", height:"20px"}}
            required
          />
        </div>

        <div className="input_label">
          <p>Email</p>
        </div>
        <div className="input-wrapper">
          <TextFeild
            type="email"
            label="Email"
            field_id="email_text"
            name="email"
            value={formData.email}
            placeholder="dummy_email@gmail.com"
            onChange={handleChange}
            style_obj={{width:"100%", height:"20px"}}
            required
          />
        </div>

        <div className="submit-btn-wrapper">
          <Button trig_function={updateProfile} style_obj={{ width: "300px", height: "25px" }} txt="Submit Changes" />
        </div>
      </form>
    </Template>
  );
};

export default Profile;
