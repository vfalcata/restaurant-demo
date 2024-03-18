import React from 'react';
import axios from 'axios';
import ItemCard from '../ItemCard';
import { Card, Container, Row } from 'react-bootstrap';

export default class ItemHome extends React.Component {
  state = {
    item
  }

  componentDidMount() {
    axios.get(`http://localhost:8080/item/${props.itemId}`,{
      headers:{
        'Authorization':'Basic '+'admin:mypassword',
        "Access-Control-Allow-Headers": "*",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "*" 
      },
    })
      .then(res => {
        const items = res.data;
        this.setState({ item });
      })
  }

  render() {
    return (
     
<Card style={{ width: '18rem' }}>
  <Card.Body>
    <Card.Title>{props.name}</Card.Title>
    <Card.Text>
    {props.nutrition}
    </Card.Text>
    <Button variant="primary">Go somewhere</Button>
  </Card.Body>
</Card>
    )
  }
}
