import React, { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select";
import { EXPENSE_CATEGORIES } from "@/components/ExpenseTracker/constants.js";

const API_URL = "http://localhost:8080/api/transaction";

const DeleteTransactions = () => {
    const [transactions, setTransactions] = useState([]);
    const [filters, setFilters] = useState({ date: "", category: "", description: "" });

    // Fetch transactions when component mounts
    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                const response = await fetch(API_URL);
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                const data = await response.json();
                console.log("Fetched transactions:", data);
                setTransactions(data);
            } catch (error) {
                console.error("Error fetching transactions:", error);
            }
        };

        fetchTransactions().catch(console.error);

    }, []);


    const handleFilterChange = (field, value) => {
        setFilters((prevFilters) => ({ ...prevFilters, [field]: value }));
    };

    const filteredTransactions = transactions.filter(txn =>
        (filters.date ? txn.date.includes(filters.date) : true) &&
        (filters.category && filters.category !== "all" ? txn.category === filters.category : true) &&
        (filters.description ? txn.description.toLowerCase().includes(filters.description.toLowerCase()) : true)
    );

    const handleDelete = async (id) => {
        try {
            const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

            setTransactions((prev) => prev.filter((txn) => txn.id !== id)); // Remove from UI
        } catch (error) {
            console.error("Error deleting transaction:", error);
        }
    };

    return (
        <div className="flex-1 p-6 bg-gray-50">
            <h2 className="text-2xl font-semibold mb-4">Delete Transactions</h2>

            {/* Filters */}
            <div className="flex space-x-4 mb-4">
                <Input
                    type="date"
                    value={filters.date}
                    onChange={(e) => handleFilterChange("date", e.target.value)}
                    className="w-1/3"
                />
                <Select onValueChange={(value) => handleFilterChange("category", value || "all")}>
                    <SelectTrigger className="w-1/3">
                        <SelectValue placeholder="Filter by Category" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="all">All</SelectItem>
                        {EXPENSE_CATEGORIES.map((cat) => (
                            <SelectItem key={cat} value={cat}>
                                {cat}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
                <Input
                    type="text"
                    placeholder="Search by description"
                    value={filters.description}
                    onChange={(e) => handleFilterChange("description", e.target.value)}
                    className="w-1/3"
                />
            </div>

            {/* Transactions Table */}
            <div className="bg-white shadow-md rounded-lg p-4">
                <table className="w-full text-left">
                    <thead>
                    <tr className="border-b">
                        <th className="p-2">Date</th>
                        <th className="p-2">Description</th>
                        <th className="p-2">Amount</th>
                        <th className="p-2">Category</th>
                        <th className="p-2">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredTransactions.length > 0 ? (
                        filteredTransactions.map((txn) => (
                            <tr key={txn.id} className="border-b">
                                <td className="p-2">{txn.date}</td>
                                <td className="p-2">{txn.description}</td>
                                <td className="p-2">${txn.amount}</td>
                                <td className="p-2">{txn.category}</td>
                                <td className="p-2">
                                    <Button
                                        variant="destructive"
                                        onClick={() => handleDelete(txn.id)}
                                    >
                                        Delete
                                    </Button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className="p-4 text-center text-gray-500">
                                No transactions found.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default DeleteTransactions;
