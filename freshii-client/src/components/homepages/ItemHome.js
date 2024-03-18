import React from 'react';
import axios from 'axios';
import ItemCard from '../ItemCard';
import { Container, Row } from 'react-bootstrap';
import FreshiiNavbar from '../FreshiiNavbar';

export default class ItemHome extends React.Component {
  // state = {
  //   items: []
  // }


  componentDidMount() {
    axios.get(`http://localhost:8080/items`,{
      headers:{
        'Authorization':'Basic admin:mypassword',
        "Access-Control-Allow-Headers": "*",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "*" 
      },
    })
      .then(res => {
        const items = res.data;
        this.setState({ items });
      })
  }

  render() {
    const items = [
      {name: "chicken fingers"},
      {name: "hotdog"},
      {name: "fries"},
    ]
    return (
        <div><FreshiiNavbar />
        <Container className="px-3 mx-5 py-5 my-2">
           <div>Please select an item</div>
        <Row>
          {this.state.items
            .map(item =>
              <ItemCard name={item.name}/>
            )}
        </Row>

        
      </Container>
      </div>
    )
  }
}
