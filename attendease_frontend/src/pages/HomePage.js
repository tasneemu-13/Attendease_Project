import AboutUs from '../components/AboutUs';
import Header from '../components/Header';
import SupportAnimation from '../components/SupportAnimation';
import TypedText from '../components/TypedText';
import Button from '../components/Button';
import { useNavigate } from "react-router-dom";
import ContributorsStory from '../components/ContributorStory';

const HomePage = () => {
  const navigate = useNavigate();

  return (

    <div className="min-h-screen bg-grid">
      <Header />
      {/* Main Content Section */}
      <div className="flex items-center justify-center">
        <div className="text-5xl text-purple-900 mb-4">
          <div className="w-full text-left" style={{ paddingRight: '450px' }}>
            <h1 className="font-dancing text-8xl">Welcome to</h1>

            {/* TypedText gradient container */}
            <div
              className="text-6xl inline-block px-4 py-2 rounded-lg font-bold bg-gradient-to-r from-sky-400 via-purple-500 to-pink-300 text-transparent bg-clip-text"
              style={{ fontFamily: "'Doodle Shadow', cursive" }}
            >
              <TypedText />
            </div>

           
            <div className="flex flex-col items-start space-y-4 mt-8 ml-4">
              <Button
                label="About Us"
                onClick={() => window.scrollTo(0, document.body.scrollHeight)}
              />
              <Button
                label="Get Started"
                onClick={() => navigate('/register')}
              />
            </div>
          </div>
        </div>

        {/* Animation */}
        <div className="min-h-screen flex items-center justify-start pl-30">
          <SupportAnimation />
        </div>
      </div>

      {/* Contributors */}
      <ContributorsStory />

      {/* About Us */}
      <AboutUs />
    </div>
  );
};

export default HomePage;
