import React from 'react';
import ExpenseTrackerForm from "@/components/ExpenseTracker/ExpenseTrackerForm.jsx";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "@/components/Layout";
import Home from "./pages/Home.jsx";
import DeleteTransactions from "./pages/DeleteTransactions.jsx";

function App() {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/create" element={<ExpenseTrackerForm />} />
                    <Route path="/transactions" element={<DeleteTransactions />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;