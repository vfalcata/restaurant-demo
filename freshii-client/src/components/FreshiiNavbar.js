import { Container, Nav, Navbar } from "react-bootstrap";

const FreshiiNavbar = (props) => {
    return (
        <div>    
            <Navbar bg="dark" variant="dark">
        <Container>
        <Navbar.Brand href="#home">Freshii</Navbar.Brand>
        <Nav className="me-auto">
          <Nav.Link href="/">Home</Nav.Link>
          <Nav.Link href="/portal">Login Portal</Nav.Link>
          <Nav.Link href="/manager">Manager</Nav.Link>
          <Nav.Link href="/admin">Admin</Nav.Link>
        </Nav>
        </Container>
        </Navbar>

        </div>

    )
}
export default FreshiiNavbar;
