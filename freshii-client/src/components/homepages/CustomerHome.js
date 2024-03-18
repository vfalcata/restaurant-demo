const CustomerHome = (props) => {
    return (<div>Welcome customer {JSON.parse(document.cookie).username} {this.props.location.state.itemName}</div>)
}
export default CustomerHome;