const API_BASE_URL = 'http://localhost:8080/api';
/*
export async function addExpense(expenseData) {
    try {
        const response = await fetch(`${API_BASE_URL}/transaction`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(expenseData),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error adding expense:', error);
        throw error;
    }
}
*/
export async function getExpenses() {
    try {
        const response = await fetch(`${API_BASE_URL}/transaction`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching expenses:', error);
        throw error;
    }
}