import React from 'react';

const Button = ({ label, onClick }) => {
  return (
    <button
      onClick={onClick}
      className="w-60 bg-pink-200 text-purple-900 font-semibold py-3 px-6 rounded-full text-lg shadow-md hover:shadow-lg hover:scale-105 transition duration-300"
    >
      {label}
    </button>
  );
};

export default Button;
