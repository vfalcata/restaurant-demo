import React from 'react';
import axios from 'axios';

import FreshiiNavbar from '../FreshiiNavbar';

export default class AdminHome extends React.Component {
  state = {
    authenticated: false
  }

  componentDidMount() {
    axios.post(`http://localhost:8080/admin/login`,{
        username:document.cookie.username,
        password:document.cookie.password,
        role:document.cookie.role,
        headers:{
          'Authorization':'Basic '+'admin:mypassword',
          "Access-Control-Allow-Headers": "*",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "*" 
        },

    })
      .then(res => {
        const items = res.data;
        this.setState({ authenticated:true });
        alert('worked')
      })
  }

  render() {
      if(this.state.authenticated){
        return (
        
            <div><FreshiiNavbar />
            welcome admine to the homepage {JSON.parse(document.cookie).username} 
          </div>
        )
      }else{
        return (
        
            <div><FreshiiNavbar />
            Sorry you do not have admin access
          </div>
        )
      }

  }
}
