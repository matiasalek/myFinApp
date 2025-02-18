import React from "react";

const Home = () => {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-6 text-center">
            <h1 className="text-4xl font-extrabold text-gray-800 mb-4">Welcome to <span className="text-blue-600">myFinApp</span> 🚀</h1>
            <p className="text-lg text-gray-700 max-w-2xl">
                Take control of your personal finances with ease!
                myFinApp helps you track transactions, categorize expenses, and visualize spending habits effortlessly.
                Stay on top of your financial goals with real-time insights and smart budgeting.
            </p>
            <p className="text-md text-gray-600 mt-4 italic">
                "A smart way to manage your money, one transaction at a time."
            </p>
        </div>
    );
};

export default Home;