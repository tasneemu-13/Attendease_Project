import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Home, Menu, X } from "lucide-react";

function Header() {
  const navigate = useNavigate();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  return (
    <header className="w-full shadow-md bg-white sticky top-0 z-50">
      <div className="max-w-7xl mx-auto flex items-center justify-between px-4 py-3">
        
        {/* Logo */}
        <div className="text-xl font-bold text-purple-900">
          |T & V
        </div>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center space-x-8">
          <button
            onClick={() => navigate('/features')}
            className="px-4 py-1 rounded-full border-2 border-pink-300 text-purple-800 hover:bg-white hover:shadow-md hover:border-pink-400 hover:text-blue-900 transition-all"
          >
            Features
          </button>
          <button
            onClick={() => navigate('/blogs')}
            className="px-4 py-1 rounded-full border-2 border-pink-300 text-purple-800 hover:bg-white hover:shadow-md hover:border-pink-400 hover:text-blue-900 transition-all"
          >
            Blog
          </button>
          <button
            onClick={() => navigate('/support')}
            className="px-4 py-1 rounded-full border-2 border-pink-300 text-purple-800 hover:bg-white hover:shadow-md hover:border-pink-400 hover:text-blue-900 transition-all"
          >
            Support
          </button>
        </nav>

        {/* Right Side: Home + Login (Desktop) */}
        <div className="hidden md:flex items-center space-x-3">
          <button
            onClick={() => navigate('/')}
            className="flex items-center px-4 py-1 rounded-full border-2 border-pink-300 text-purple-800 hover:bg-white hover:shadow-md hover:border-pink-400 hover:text-blue-900 transition-all"
          >
            <Home size={18} className="mr-2" />
            <span>Home</span>
          </button>
          <button
            onClick={() => navigate('/login')}
            className="bg-pink-400 text-white px-4 py-2 rounded hover:bg-purple-500 transition-all"
          >
            Login
          </button>
        </div>

        {/* Mobile Menu Icon (Hamburger) */}
        <div className="md:hidden">
          <button onClick={toggleMobileMenu} className="text-purple-800 focus:outline-none">
            {isMobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </div>

      {/* Mobile Nav (Toggled by state) */}
      {isMobileMenuOpen && (
        <div className="md:hidden px-4 pb-4">
          <nav className="flex flex-col space-y-2">
            <button
              onClick={() => { navigate('/features'); toggleMobileMenu(); }}
              className="block py-2 w-full text-left font-medium text-purple-800 hover:bg-gray-100 rounded-md"
            >
              Features
            </button>
            <button
              onClick={() => { navigate('/blogs'); toggleMobileMenu(); }}
              className="block py-2 w-full text-left font-medium text-purple-800 hover:bg-gray-100 rounded-md"
            >
              Blog
            </button>
            <button
              onClick={() => { navigate('/support'); toggleMobileMenu(); }}
              className="block py-2 w-full text-left font-medium text-purple-800 hover:bg-gray-100 rounded-md"
            >
              Support
            </button>
            <button
              onClick={() => { navigate('/'); toggleMobileMenu(); }}
              className="block py-2 w-full text-left font-medium text-purple-800 hover:bg-gray-100 rounded-md"
            >
              Home
            </button>
            <button
              onClick={() => { navigate('/login'); toggleMobileMenu(); }}
              className="mt-2 bg-pink-400 text-white w-full py-2 rounded-md hover:bg-purple-500 transition-all"
            >
              Login
            </button>
          </nav>
        </div>
      )}
    </header>
  );
}

export default Header;