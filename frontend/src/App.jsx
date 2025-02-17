import React from 'react';
import ExpenseTrackerForm from "@/components/ExpenseTracker/ExpenseTrackerForm.jsx";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Layout from "@/components/Layout";
import Home from "./pages/Home.jsx";
import UpdateTransactions from "./pages/UpdateTransactions.jsx"
import DeleteTransactions from "./pages/DeleteTransactions.jsx";

function App() {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/create" element={<ExpenseTrackerForm />} />
                    <Route path="/update" element={<UpdateTransactions />} />
                    <Route path="/delete" element={<DeleteTransactions />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;