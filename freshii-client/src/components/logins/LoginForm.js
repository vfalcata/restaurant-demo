import axios from 'axios';
import React, { useState,  } from 'react';
import { Form, Button, Container, Alert } from 'react-bootstrap';
import {
    Redirect,
    useHistory
  } from "react-router-dom";

const LoginForm = (props) => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const role = props.role;
    const loginUrl=props.loginUrl;

    const history = useHistory();
  
    const usernameChangeHandler = (event) => {
        setUsername(event.target.value);
    };
  
    const passwordChangeHandler = (event) => {
        setPassword(event.target.value);
    };
  
  
  
    const submitHandler = (event) => {
      event.preventDefault();
  
      //reset the values of input fields
      setUsername('');
      setPassword('');

        axios.post(loginUrl,{
            username,
            password,
            role,
            headers:{
              'Authorization':'Basic '+'admin:mypassword',
              "Access-Control-Allow-Headers": "*",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "*" 
            },
        })
          .then(function (response) {
            console.log(response.data);
            response.data.role=role;
            document.cookie=JSON.stringify(response.data)
            alert("Log in sucessful")
            history.push("/home",{state:{name:'new pushed name'}});
            

        

          })
          .catch(function (error) {
            console.log(error);
          });
  
  
    };

    return(
        <Container>
        <Form className="px-3 mx-5 py-5 my-2" onSubmit={submitHandler}>
        <Form.Group  controlId="form.username">
              <Form.Label>{role.charAt(0).toUpperCase() + role.slice(1)} Username</Form.Label>
              <Form.Control type="text" value={username} onChange={usernameChangeHandler} placeholder="Enter Id" required/>
          </Form.Group>
          <Form.Group controlId="form.password">
              <Form.Label>Password</Form.Label>
              <Form.Control type="text" value={password} onChange={passwordChangeHandler} placeholder="Enter User Name" required/>
          </Form.Group>
          <Button className='my-3' type='submit'>Login</Button>
        </Form>
      </Container>
      );

}
export default LoginForm;