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
import { EXPENSE_CATEGORIES } from './constants';
import { formatDate } from './utils';

const ExpenseTrackerForm = () => {
    const [date, setDate] = useState(new Date());
    const [category, setCategory] = useState('');
    const [amount, setAmount] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Here you would integrate with your Spring Boot backend
        console.log({ date, category, amount });
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-slate-900">
            <Card className="w-full max-w-md mx-4">
                <CardHeader>
                    <CardTitle className="text-2xl font-medium text-center">
                        Track Expense
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="space-y-2">
                            <label className="text-sm font-medium">Date</label>
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
                            <label className="text-sm font-medium">Category</label>
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
                            <label className="text-sm font-medium">Amount</label>
                            <div className="relative">
                                <span className="absolute left-3 top-2.5">$</span>
                                <Input
                                    type="number"
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    className="pl-6"
                                    placeholder="0.00"
                                    step="0.01"
                                />
                            </div>
                        </div>
                    </form>
                </CardContent>
                <CardFooter>
                    <Button
                        className="w-full"
                        onClick={handleSubmit}
                    >
                        Add Expense
                    </Button>
                </CardFooter>
            </Card>
        </div>
    );
};

export default ExpenseTrackerForm;