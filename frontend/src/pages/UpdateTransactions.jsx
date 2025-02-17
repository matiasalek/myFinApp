import React, { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select";
import { EXPENSE_CATEGORIES } from "@/components/ExpenseTracker/constants.js";
import { Pencil, Check } from "lucide-react";

const API_URL = "http://localhost:8080/api/transaction";

const UpdateTransactions = () => {
    const [transactions, setTransactions] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedTransaction, setEditedTransaction] = useState({});

    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                console.log("Fetching transactions...");

                const response = await fetch(API_URL);
                console.log(`Response status: ${response.status}`);

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`HTTP error! Status: ${response.status} - ${errorText}`);
                }

                const data = await response.json();
                console.log("Fetched transactions:", data);

                setTransactions(data);
            } catch (error) {
                console.error("Error fetching transactions:", error.message);
            }
        };

        fetchTransactions().catch(error => console.error("Unhandled fetch error:", error));

    }, []);

    const formatCategory = (category) => {
        if (!category) return "Uncategorized";
        return category.replace(/_/g, " ").toLowerCase().replace(/\b\w/g, (char) => char.toUpperCase());
    };

    const handleEdit = (txn) => {
        setEditingId(txn.id);
        setEditedTransaction({ ...txn });
    };

    const handleChange = (field, value) => {
        setEditedTransaction((prev) => ({
            ...prev,
            [field]: field === "category" ? { id: value, name: EXPENSE_CATEGORIES.find(cat => cat.toUpperCase() === value) } : value
        }));
    };

    const handleSave = async () => {
        try {
            const response = await fetch(`${API_URL}/${editingId}`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(editedTransaction),
            });
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

            setTransactions((prev) =>
                prev.map((txn) => (txn.id === editingId ? editedTransaction : txn))
            );
            setEditingId(null);
        } catch (error) {
            console.error("Error updating transaction:", error);
        }
    };

    return (
        <div className="flex-1 p-6 bg-gray-50">
            <h2 className="text-2xl font-semibold mb-4">Update Transactions</h2>
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
                    {transactions.map((txn) => (
                        <tr key={txn.id} className="border-b">
                            <td className="p-2">{txn.date}</td>
                            <td className="p-2">
                                {editingId === txn.id ? (
                                    <Input
                                        value={editedTransaction.description}
                                        onChange={(e) => handleChange("description", e.target.value)}
                                    />
                                ) : (
                                    txn.description
                                )}
                            </td>
                            <td className="p-2">
                                {editingId === txn.id ? (
                                    <Input
                                        type="number"
                                        value={editedTransaction.amount}
                                        onChange={(e) => handleChange("amount", e.target.value)}
                                    />
                                ) : (
                                    `$${txn.amount}`
                                )}
                            </td>
                            <td className="p-2">
                                {editingId === txn.id ? (
                                    <Select onValueChange={(value) => handleChange("category", value)}>
                                        <SelectTrigger>
                                            <SelectValue placeholder={formatCategory(txn.category?.name) || "Uncategorized"} />
                                        </SelectTrigger>
                                        <SelectContent>
                                            {EXPENSE_CATEGORIES.map((cat) => (
                                                <SelectItem key={cat} value={cat.toUpperCase()}>{cat}</SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                ) : (
                                    formatCategory(txn.category?.name)
                                )}
                            </td>
                            <td className="p-2">
                                {editingId === txn.id ? (
                                    <Button variant="success" onClick={handleSave}>
                                        <Check size={16} />
                                    </Button>
                                ) : (
                                    <Button variant="ghost" onClick={() => handleEdit(txn)}>
                                        <Pencil size={16} />
                                    </Button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UpdateTransactions;
