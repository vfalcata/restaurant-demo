import axios from 'axios';
import React, { useState } from 'react';
import { Form, Button, Container, Alert } from 'react-bootstrap';

const SignupForm = (props) => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [address, setAddress] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const role = props.role;
    const signupUrl=props.loginUrl;
    const roleHomeComponent=props.roleHomeComponent;

    
  
  
    const usernameChangeHandler = (event) => {
        setUsername(event.target.value);
    };
  
    const passwordChangeHandler = (event) => {
        setPassword(event.target.value);
    };
  
    const addressChangeHandler = (event) => {
      setAddress(event.target.value);
    };
    const phoneNumberChangeHandler = (event) => {
      setPhoneNumber(event.target.value);
    };
  
  
    const submitHandler = (event) => {
      event.preventDefault();

      setUsername('');
      setPassword('');
      setAddress('');
      setPhoneNumber('');
        axios.post(signupUrl,{
            username,
            password,
            address,
            phoneNumber,
            headers:{
              'Authorization':'Basic '+'admin:mypassword',
              "Access-Control-Allow-Headers": "*",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "*" 
            },
        })
          .then(function (response) {
            console.log(response.data);
            document.cookie=JSON.stringify(response.data)
            return ({roleHomeComponent})
          })
          .catch(function (error) {
            console.log(error);
            alert("Signup not sucessful")
          });  
    };

    return(
        <Container>
        <Form className="px-3 mx-5 py-5 my-2" onSubmit={submitHandler}>
        <Form.Group  controlId="form.username">
              <Form.Label>New {role.charAt(0).toUpperCase() + role.slice(1)} Username</Form.Label>
              <Form.Control type="text" value={username} onChange={usernameChangeHandler} placeholder="Enter Id" required/>
          </Form.Group>
          <Form.Group controlId="form.password">
              <Form.Label>Password</Form.Label>
              <Form.Control type="text" value={password} onChange={passwordChangeHandler} placeholder="Enter User Name" required/>
          </Form.Group>
          <Form.Group  controlId="form.address">
              <Form.Label>Address</Form.Label>
              <Form.Control type="text" value={address} onChange={addressChangeHandler} placeholder="Enter Id" required/>
          </Form.Group>
          <Form.Group controlId="form.phoneNumber">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control type="text" value={phoneNumber} onChange={phoneNumberChangeHandler} placeholder="Enter User Name" required/>
          </Form.Group>
          <Button className='my-3' type='submit'>Login</Button>
        </Form>
      </Container>  
      );
}
export default SignupForm;