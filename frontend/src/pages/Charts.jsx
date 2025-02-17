import React, { useEffect, useState } from "react";
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select.jsx";
import { getMonth, getYear, format, parseISO } from "date-fns";

ChartJS.register(ArcElement, Tooltip, Legend);

const API_URL = "http://localhost:8080/api/transaction";

const months = Array.from({ length: 12 }, (_, i) => ({
    value: (i + 1).toString(),
    label: format(new Date(2022, i, 1), "MMMM")
}));

const currentYear = new Date().getFullYear();
const years = Array.from({ length: currentYear - 2019 + 2 }, (_, i) => ({
    value: (2020 + i).toString(),
    label: (2020 + i).toString()
}));

const Charts = () => {
    const [chartData, setChartData] = useState({ labels: [], datasets: [] });
    const [selectedMonth, setSelectedMonth] = useState((new Date().getMonth() + 1).toString());
    const [selectedYear, setSelectedYear] = useState(currentYear.toString());

    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                const response = await fetch(API_URL);
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                const data = await response.json();

                const filteredData = data.filter((txn) => {
                    if (!txn.date) return false;
                    const txnDate = parseISO(txn.date);
                    return getMonth(txnDate) + 1 === parseInt(selectedMonth) && getYear(txnDate) === parseInt(selectedYear);
                });

                const formatCategory = (category) => {
                    if (!category) return "Uncategorized";
                    return category
                        .toLowerCase()
                        .replace(/_/g, " ")
                        .replace(/\b\w/g, (char) => char.toUpperCase());
                };

                const categoryTotals = filteredData.reduce((acc, txn) => {
                    const category = formatCategory(txn.category?.name || "Uncategorized");
                    acc[category] = (acc[category] || 0) + txn.amount;
                    return acc;
                }, {});


                const labels = Object.keys(categoryTotals);
                const amounts = Object.values(categoryTotals);

                setChartData({
                    labels,
                    datasets: [
                        {
                            label: "Total Spent by Category ($)",
                            data: amounts,
                            backgroundColor: [
                                "#FF6384", "#36A2EB", "#FFCE56", "#4CAF50", "#BA68C8", "#FF9800", "#E91E63"
                            ]
                        }
                    ]
                });

            } catch (error) {
                console.error("Error fetching transactions:", error);
            }
        };
        fetchTransactions().catch(error => console.error("Unhandled fetch error:", error));
    }, [selectedMonth, selectedYear]);

    return (
        <div className="p-6 bg-white shadow-md rounded-lg">
            <h2 className="text-2xl font-semibold mb-4">Transaction Summary</h2>

            {/* Filter Controls */}
            <div className="flex space-x-4 mb-4">
                <Select value={selectedMonth} onValueChange={setSelectedMonth}>
                    <SelectTrigger className="w-1/2">
                        <SelectValue placeholder="Select Month" />
                    </SelectTrigger>
                    <SelectContent>
                        {months.map((month) => (
                            <SelectItem key={month.value} value={month.value}>
                                {month.label}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>

                <Select value={selectedYear} onValueChange={setSelectedYear}>
                    <SelectTrigger className="w-1/2">
                        <SelectValue placeholder="Select Year" />
                    </SelectTrigger>
                    <SelectContent>
                        {years.map((year) => (
                            <SelectItem key={year.value} value={year.value}>
                                {year.label}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
            </div>

            {/* Pie Chart */}
            <div className="w-full max-w-2xl mx-auto" style={{ height: "500px" }}>
                <Pie data={chartData} options={{ responsive: true, maintainAspectRatio: false }} />
            </div>
        </div>
    );
};

export default Charts;