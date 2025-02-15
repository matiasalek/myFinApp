import Menu from "./Menu";

const Layout = ({ children }) => {
    return (
        <div className="h-full w-full flex">
            <Menu />
            <div className="flex-1 h-full bg-gray-100 p-6 overflow-auto">
                {children}
            </div>
        </div>
    );
};

export default Layout;