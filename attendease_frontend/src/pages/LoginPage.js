import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import LoginHeader from "../components/LoginHeader";
// The LoginPage component handles user authentication.
export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState({ text: "", type: "" });
  const navigate = useNavigate();

  // Function to display temporary success or error messages.
  const showMessage = (text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: "", type: "" }), 3000);
  };

  // Handles the login form submission.
  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/attendease_backend/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok) {
        showMessage("Login successful!", "success");

        // ✅ Store role
        localStorage.setItem("role", data.role);

        // ✅ Store data based on role
        if (data.role === "teacher") {
          localStorage.setItem("teacherId", data.userId);
          localStorage.setItem("teacherName", data.name);
          navigate("/teacher");
        } else if (data.role === "student") {
          localStorage.setItem("studentId", data.userId);
          localStorage.setItem("studentName", data.name);
          localStorage.setItem("enrollmentId", data.enrollmentId);
          navigate("/student");
        } else if (data.role === "admin") {
          setTimeout(() => navigate("/admin"), 1000);
        } else {
          setTimeout(() => navigate("/"), 1000);
        }
      } else {
        showMessage(data.message || "Login failed. Please check your credentials.", "error");
      }
    } catch (error) {
      console.error("Login failed:", error);
      showMessage("An error occurred. Please try again.", "error");
    }
  };

  return (
   <div>
      <LoginHeader />  
    <div className="flex h-screen bg-white">
    
      {/* Left Side: Form */}
      <div className="w-1/2 bg-gradient-to-br from-purple-300 via-blue-300 to-pink-400 flex flex-col items-center justify-center px-12">
        {/* Welcome Message */}
        <div className="mb-6">
          <h1 className="text-5xl font-bold text-blue-900 text-center font-bubbles">Welcome Back!</h1>
          <p className="text-blue-900 text-center mt-2 text-2xl">We're happy to see you again 😊</p>
        </div>

        {/* Message Box for success and error messages */}
        {message.text && (
          <div
            className={`mb-4 px-6 py-3 rounded-xl shadow-lg transition-all duration-300 ease-in-out ${
              message.type === "success" ? "bg-green-500 text-white" : "bg-red-500 text-white"
            }`}
          >
            <p className="font-semibold">{message.text}</p>
          </div>
        )}

        {/* Login Form */}
        <form
          className="w-full max-w-md bg-white/30 backdrop-blur-md p-10 rounded-2xl shadow-2xl space-y-6"
          onSubmit={handleLogin}
        >
          <h2 className="text-3xl font-bold text-center text-pink-900">Login</h2>

          <div className="flex flex-col">
            <label htmlFor="email" className="mb-1 text-sm font-semibold text-pink-500">
              Email
            </label>
            <input
              type="email"
              id="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="p-3 border border-white/40 rounded-lg outline-blue-300 bg-white/70"
              placeholder="Enter your email"
            />
          </div>

          <div className="flex flex-col">
            <label htmlFor="password" className="mb-1 text-sm font-semibold text-pink-500">
              Password
            </label>
            <input
              type="password"
              id="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="p-3 border border-white/40 rounded-lg outline-blue-300 bg-white/70"
              placeholder="Enter your password"
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 rounded-lg bg-white text-blue-800 font-semibold hover:bg-blue-100 transition"
          >
            Login
          </button>
        </form>
      </div>

      {/* Right Side: Image */}
      <div className="w-1/2 h-full">
        <img src="images\\image3.jpg" alt="Login Visual" className="h-full w-full object-cover" />
      </div>
    </div>
  </div>  
  );
}
