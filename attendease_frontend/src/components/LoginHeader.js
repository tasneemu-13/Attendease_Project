import { useNavigate } from "react-router-dom";
import { Home } from "lucide-react"; // Optional: use emoji instead if you prefer

function Header() {
  const navigate = useNavigate();

  return (
    <header className="w-full shadow-md bg-white">
      <div className="max-w-7xl mx-auto flex items-center justify-between px-4 py-3">
        
        {/* Logo */}
        <div className="text-xl font-bold text-purple-900 animate-spin-slow">
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

        {/* Right Side: Home + Login */}
        <div className="hidden md:flex items-center space-x-3">
          <button
            onClick={() => navigate('/')}
            className="flex items-center px-4 py-1 rounded-full border-2 border-pink-300 text-purple-800 hover:bg-white hover:shadow-md hover:border-pink-400 hover:text-blue-900 transition-all"
          >
            🏠 <span className="ml-2">Home</span>
          </button>
        </div>

        {/* Mobile Menu Icon - Optional */}
        <div className="md:hidden">
          {/* Hamburger icon can go here if needed */}
        </div>
      </div>

      {/* Mobile Nav (Optional if mobile responsive menu needed) */}
      <div className="md:hidden px-4 pb-9">
        <button onClick={() => navigate('/features')} className="block py-2 w-full text-left">Features</button>
        <button onClick={() => navigate('/blogs')} className="block py-2 w-full text-left">Blog</button>
        <button onClick={() => navigate('/support')} className="block py-2 w-full text-left">Support</button>
        <button onClick={() => navigate('/')} className="block py-2 w-full text-left">Home</button>
        
      </div>
    </header>
  );
}

export default Header;
