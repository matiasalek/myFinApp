import Menu from "./Menu";

const Layout = ({ children }) => {
    return (
        <div className="flex h-screen w-screen">
            {/* Sidebar Menu */}
            <Menu />

            {/* Main Content Area */}
            <div className="flex-1 p-6">{children}</div>
        </div>
    );
};

export default Layout;
