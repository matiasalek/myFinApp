import React, { useState } from 'react';
import { Calendar } from '@/components/components/ui/calendar';
import { Button } from '@/components/components/ui/button';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/components/ui/select';
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
    CardFooter,
} from '@/components/components/ui/card';
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from '@/components/components/ui/popover';
import { Input } from '@/components/components/ui/input';
import { CalendarIcon } from 'lucide-react';
import { formatDate } from './utils';
import { EXPENSE_CATEGORIES } from "@/components/ExpenseTracker/constants.js";

const ExpenseTrackerForm = () => {
    const [date, setDate] = useState(new Date());
    const [category, setCategory] = useState('');
    const [description, setDescription] = useState('');
    const [amount, setAmount] = useState('');
    const [recurring, setRecurring] = useState('false');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [message, setMessage] = useState({ text: '', type: '' });

    const handleSubmit = async (e) => {
        e.preventDefault();

        setMessage({ text: '', type: '' });

        if (!description || !amount || !category) {
            setMessage({ text: 'Please fill in all fields', type: 'error' });
            return;
        }

        setIsSubmitting(true);

        try {
            const transactionData = {
                description,
                amount: amount.toString(),
                date: date.toISOString().split('T')[0],
                category: {
                    name: category.toUpperCase().replace(/ /g, '_')
                },
                recurring: recurring === 'true'
            };

            const response = await fetch('http://localhost:8080/api/transaction', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(transactionData),
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            setDescription('');
            setAmount('');
            setDate(new Date());
            setCategory('');
            setRecurring('false');
            setMessage({ text: 'Transaction added successfully!', type: 'success' });
        } catch (error) {
            setMessage({ text: 'Failed to add transaction. Please try again.', type: 'error' });
            console.error('Error:', error);
        } finally {
            setIsSubmitting(false);
        }
    };


    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-50">
            <Card className="w-full max-w-md mx-4">
                <CardHeader>
                    <CardTitle className="text-2xl font-medium text-center">
                        Add Transaction
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    {message.text && (
                        <div
                            className={`mb-4 p-3 rounded ${
                                message.type === 'error'
                                    ? 'bg-red-100 text-red-700 border border-red-200'
                                    : 'bg-green-100 text-green-700 border border-green-200'
                            }`}
                        >
                            {message.text}
                        </div>
                    )}
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="space-y-2">
                            <label className="text-sm font-medium">Date and Time</label>
                            <Popover>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant="outline"
                                        className="w-full justify-start text-left font-normal"
                                    >
                                        <CalendarIcon className="mr-2 h-4 w-4" />
                                        {formatDate(date)}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0">
                                    <Calendar
                                        mode="single"
                                        selected={date}
                                        onSelect={(date) => date && setDate(date)}
                                        initialFocus
                                    />
                                </PopoverContent>
                            </Popover>
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium">Description *</label>
                            <Input
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                placeholder="Enter transaction description"
                                required
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium">Amount *</label>
                            <div className="relative">
                                <span className="absolute left-3 top-2.5">$</span>
                                <Input
                                    type="number"
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    className="pl-6"
                                    placeholder="0.00"
                                    step="0.01"
                                    required
                                />
                            </div>
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium">Category *</label>
                            <Select value={category} onValueChange={setCategory}>
                                <SelectTrigger className="w-full">
                                    <SelectValue placeholder="Select category" />
                                </SelectTrigger>
                                <SelectContent>
                                    {EXPENSE_CATEGORIES.map((cat) => (
                                        <SelectItem key={cat} value={cat}>
                                            {cat}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium">Recurring</label>
                            <Select value={recurring} onValueChange={setRecurring}>
                                <SelectTrigger className="w-full">
                                    <SelectValue placeholder="Is this a recurring transaction?" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="false">No</SelectItem>
                                    <SelectItem value="true">Yes</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                    </form>
                </CardContent>
                <CardFooter>
                    <Button
                        className="w-full"
                        onClick={handleSubmit}
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? "Adding..." : "Add Transaction"}
                    </Button>
                </CardFooter>
            </Card>
        </div>
    );
};

export default ExpenseTrackerForm;
