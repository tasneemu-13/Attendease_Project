import React, { useState } from "react";
import toast, { Toaster } from "react-hot-toast";
import LoginHeader from '../components/LoginHeader';
export default function Support() {
  const [feedback, setFeedback] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!feedback.trim()) {
      toast.error("Feedback cannot be empty.");
      return;
    }

    setIsSubmitting(true);

    // The user's email is no longer needed to submit feedback
    try {
      const response = await fetch("http://localhost:8080/attendease_backend/api/submit-feedback", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        // UPDATED: Now sending only the feedback in the request body
        body: JSON.stringify({ feedback }),
      });

      if (response.ok) {
        toast.success("Thank you for your feedback!");
        setSubmitted(true);
        setFeedback("");
      } else {
        const errorData = await response.json();
        toast.error(`Submission failed: ${errorData.error}`);
        console.error("Submission error:", errorData.error);
      }
    } catch (error) {
      toast.error("An error occurred. Please try again later.");
      console.error("Network error:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <LoginHeader />
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 via-purple-100 to-pink-100 px-4">
      <Toaster />
      <div className="relative w-full max-w-xl p-1 rounded-3xl bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 animate-gradient-x shadow-lg">
        <div className="bg-white rounded-3xl p-8">
          <h2 className="text-3xl font-bold text-center text-indigo-700 mb-4">
            Support & Feedback
          </h2>
          <p className="text-center text-gray-600 mb-6">
            Tell us about your experience with <strong>Attendease</strong>. Your feedback helps us improve.
          </p>

          {submitted ? (
            <div className="text-center text-green-600 font-semibold">
              Thank you for your feedback! We’re working hard to make Attendease better every day.
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              <textarea
                value={feedback}
                onChange={(e) => setFeedback(e.target.value)}
                placeholder="Share your thoughts..."
                rows={5}
                className="w-full p-4 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-purple-400 resize-none"
              />
              <button
                type="submit"
                className="w-full py-3 px-6 bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 text-white font-semibold rounded-xl shadow-md hover:shadow-lg transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
                disabled={isSubmitting || !feedback.trim()}
              >
                {isSubmitting ? "Submitting..." : "Submit Feedback"}
              </button>
            </form>
          )}

          <div className="mt-8 text-center text-sm text-gray-500">
            For any queries, feel free to contact us at{" "}
            <a
              href="mailto:attendease.support@example.com"
              className="text-purple-600 underline"
            >
              attendease.support@google.com
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>  
  );
}
