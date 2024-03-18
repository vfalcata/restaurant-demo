import logo from './logo.svg';
import './App.css';


import CustomerHome from './components/homepages/CustomerHome';
import ManagerHome from './components/homepages/ManagerHome';
import AdminHome from './components/homepages/AdminHome';
import LoginPortal from './components/LoginPortal';
import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import { Image } from 'react-bootstrap';
import ItemCard from './components/ItemCard';
import ItemHome from './components/homepages/ItemHome';

function App() {
  // const role = props.role;
  // const loginUrl=props.loginUrl;
  // const roleHomeComponent=props.roleHomeComponent;

  return (
    // <div className="App">
    //   <ItemHome ></ItemHome>
    // </div>
    <Router>
      
    <div className="App">
      
        <Switch>
          <Route path="/home">
            <CustomerHome />
          </Route>
          <Route path="/admin">
            <AdminHome />
          </Route>
          <Route path="/manager">
            <ManagerHome />
          </Route>
          <Route path="/item">
            <ManagerHome />
          </Route>
          <Route path="/portal">
            <LoginPortal />
          </Route>
          <Route path="/">
            <ItemHome />
          </Route>

        </Switch>

    </div>

  </Router>
  );
}

export default App;
