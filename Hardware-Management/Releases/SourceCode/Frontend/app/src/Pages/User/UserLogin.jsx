import React, { Component } from "react";
import "./UserLogin.css";
import { imagePath } from "../../Services";
import axios from "axios";
import { userLoginURL } from "../../Services/endpoints";
import Swal from "sweetalert2";
import { Link, withRouter } from "react-router-dom";
import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google";

// No need for withNavigation, using withRouter for React Router v5
class UserLogin extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      name: "",
    };
  }

  handleChange = (e) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const data = {
      username: this.state.username,
      password: this.state.password,
    };
    console.log(data);
    axios.post(userLoginURL, data).then((res) => {
      console.log(res);
      if (res.data === "UNAUTHORIZED") {
        Swal.fire({
          icon: "error",
          title: "Unauthorized",
          text: "Login failed. Redirecting to dashboard...",
        });
      } else {
        Swal.fire({
          icon: "success",
          title: "Login Successful!!",
        });
      }
      // Always redirect to dashboard regardless of authentication success or failure
      this.props.history.push("/dashboard");
    }).catch(error => {
      console.log("Error:", error);
      // Handle error but still redirect to dashboard
      this.props.history.push("/dashboard");
    });
  };

  // Handle Google Login success
  handleGoogleLoginSuccess = (response) => {
    console.log("Google Login Success: ", response);
    const data = {
      token: response.credential,
    };
    // Send the Google token to the backend to authenticate
    axios.post(`${userLoginURL}/google`, data).then((res) => {
      if (res.data === "UNAUTHORIZED") {
        Swal.fire({
          icon: "error",
          title: "Unauthorized",
          text: "Google login failed. Redirecting to dashboard...",
        });
      } else {
        Swal.fire({
          icon: "success",
          title: "Login Successful!!",
        });
      }
      // Always redirect to dashboard regardless of authentication success or failure
      this.props.history.push("/dashboard");
    }).catch(error => {
      console.log("Error:", error);
      // Handle error but still redirect to dashboard
      this.props.history.push("/dashboard");
    });
  };

  // Handle Google Login failure
  handleGoogleLoginFailure = (error) => {
    console.log("Google Login Failed: ", error);
    Swal.fire({
      icon: "error",
      title: "Login Failed",
      text: "Unable to login with Google. Redirecting to dashboard...",
    });
    // Always redirect to dashboard
    this.props.history.push("/dashboard");
  };

  render() {
    return (
      <GoogleOAuthProvider clientId="265749846034-fl011bte41bqsjk14q7o4qnrft7q19bf.apps.googleusercontent.com">
        <div className="UserLogin">
          <div className="d-flex justify-content-center">
            <img src={imagePath + `logo.png`} alt="" className="logo-login" />
          </div>
          <div className="User-Login-Heading-Container">
            <h3 className="Login-User-Heading">Admin Login</h3>
          </div>
          <div className="User-Login-Body-Container">
            <form onSubmit={this.handleSubmit}>
              <div className="mb-4 row">
                <label className="col-sm-10 col-form-label">Email :</label>
                <div className="col-sm-10">
                  <input
                    className="form-control"
                    type="text"
                    id="username"
                    name="username"
                    placeholder="Email"
                    required
                    value={this.state.username}
                    onChange={this.handleChange}
                  />
                </div>
              </div>
              <div className="mb-4 row">
                <label className="col-sm-10 col-form-label">Password :</label>
                <div className="col-sm-10">
                  <input
                    className="form-control"
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Password"
                    required
                    value={this.state.password}
                    onChange={this.handleChange}
                  />
                </div>
              </div>
              <div className="LoginRow">
                <button type="submit" className="User-Button-Login">
                  Login
                </button>
                <Link className="RegLink" to="/addUser">
                  Create Account
                </Link>
              </div>
            </form>

            <div className="d-flex justify-content-center mt-4">
              <GoogleLogin
                onSuccess={this.handleGoogleLoginSuccess}
                onError={this.handleGoogleLoginFailure}
              />
            </div>
          </div>
        </div>
      </GoogleOAuthProvider>
    );
  }
}

export default withRouter(UserLogin);
