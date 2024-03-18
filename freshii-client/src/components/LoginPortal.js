import { Form, Button, Card, Tab, Tabs,Container } from 'react-bootstrap';
import SignupForm from './dataforms/SignupForm';
import FreshiiNavbar from './FreshiiNavbar';

import LoginForm from './logins/LoginForm';

const LoginPortal = (props) => {
    const url = (endpoint)=>{
        return `http://localhost:8080/${endpoint}`
      }
    return(
<div>
    <FreshiiNavbar />
<Container className="w-50 py-5">

<Card className="my-5">
<Tabs defaultActiveKey="login" id="uncontrolled-tab-example" className="mb-3">
<Tab eventKey="login" title="Customer Login">
<LoginForm  role="customer" loginUrl={url('customerLogin')} />
</Tab>
<Tab eventKey="signup" title="Customer Signup">
<SignupForm role="customer" loginUrl={url('customerSignup')} />
</Tab>
<Tab eventKey="managerLogin" title="Manager Login">
<LoginForm  role="manager" loginUrl={url('managerLogin')} />
</Tab>
<Tab eventKey="adminLogin" title="Admin Login">
<LoginForm  role="ADMIN" loginUrl={url('admin/login')} />

</Tab>
</Tabs>
</Card>
</Container>
</div>
)
}
export default LoginPortal;