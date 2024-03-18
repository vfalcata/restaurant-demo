
import { useState } from 'react';
import { Button, Card } from 'react-bootstrap';
import { Route, Switch, useHistory } from 'react-router-dom';
import AdminHome from './homepages/AdminHome';
import ManagerHome from './homepages/ManagerHome';


const ItemCard = (props) => {
    const history = useHistory();
    const click=()=> {
        history.push("/item", {itemId:props.itemId}); 
      }
    return(
<Card className="px-3 mx-5 py-5 my-3" onClick={click}>
  
  <Card.Body >
    <Card.Title>{props.name} <br/> item id is: {props.itemId}</Card.Title>

  </Card.Body>

</Card>

)
}
export default ItemCard;