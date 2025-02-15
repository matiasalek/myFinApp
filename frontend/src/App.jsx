import React from 'react';
import ExpenseTrackerForm from "@/components/ExpenseTracker/ExpenseTrackerForm.jsx";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "@/components/Layout";
import Home from "./pages/Home.jsx";
//import Transactions from "./pages/Transactions.jsx";
//import Charts from "./pages/Charts.jsx";
//<Route path="/transactions" element={<Transactions />} />
//<Route path="/charts" element={<Charts />} />
function App() {
    return (
        <div className="h-screen w-screen flex">
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/create" element={<ExpenseTrackerForm />} />
                </Routes>
            </Layout>
        </Router>
        </div>
    );
}

export default App;