// src/components/TypedText.jsx
import React, { useEffect, useRef } from "react";
import Typed from "typed.js";

const TypedText = () => {
  const el = useRef(null);

  useEffect(() => {
    const typed = new Typed(el.current, {
      strings: ["Attendease", "Attendance Made Easy"],
      typeSpeed: 100,
      backSpeed: 50,
      backDelay: 1000,
      loop: true,
      showCursor: true,
      cursorChar: "|",
    });

    return () => {
      typed.destroy();
    };
  }, []);

  return (
    <span
      ref={el}
      className="text-7xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-sky-400 via-purple-500 to-pink-300"
    />
  );
};

export default TypedText;
