import { Link } from "react-router-dom";

const Menu = () => {
    return (
        <div className="w-64 h-screen bg-gray-800 text-white p-4 flex flex-col">
            <h2 className="text-xl font-bold mb-4">MyFinApp</h2>
            <nav className="space-y-2">
                <Link to="/create" className="block p-2 rounded hover:bg-gray-700">
                    ➕ Create Transaction
                </Link>
                <Link to="/transactions" className="block p-2 rounded hover:bg-gray-700">
                    ❌ Delete Transaction
                </Link>
                <Link to="/transactions" className="block p-2 rounded hover:bg-gray-700">
                    ✏️ Update Transaction
                </Link>
                <Link to="/charts" className="block p-2 rounded hover:bg-gray-700">
                    📊 View Charts
                </Link>
            </nav>
        </div>
    );
};
export default Menu;