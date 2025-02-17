import { Link, useLocation } from "react-router-dom";

const Menu = () => {
    const location = useLocation();

    const getLinkClass = (path) =>
        `block p-2 rounded transition ${location.pathname === path ? "bg-gray-700" : "hover:bg-gray-700"}`;

    return (
        <div className="w-64 h-screen bg-gray-800 text-white p-4 flex flex-col">
            <h2 className="text-xl font-bold mb-4">MyFinApp</h2>
            <nav className="space-y-2">
                <Link to="/" className={getLinkClass("/")}>
                    🏠 Home
                </Link>
                <Link to="/create" className={getLinkClass("/create")}>
                    ➕ Create Transaction
                </Link>
                <Link to="/update" className={getLinkClass("/update")}>
                    ✏️ Update Transaction
                </Link>
                <Link to="/delete" className={getLinkClass("/delete")}>
                    ❌ Delete Transaction
                </Link>
                <Link to="/charts" className={getLinkClass("/charts")}>
                    📊 View Charts
                </Link>
            </nav>
        </div>
    );
};

export default Menu;