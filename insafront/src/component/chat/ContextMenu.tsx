import React from "react";

interface ContextMenuProps {
    x: number;
    y: number;
    visible: boolean;
    onClose: () => void;
    menuItems: { label: string; onClick: () => void }[];
}

const ContextMenu: React.FC<ContextMenuProps> = ({ x, y, visible, onClose, menuItems }) => {
    if (!visible) return null;

    return (
        <div
            style={{
                position: "absolute",
                top: y,
                left: x,
                background: "white",
                border: "1px solid #ccc",
                padding: "5px",
                zIndex: 1000,
            }}
            onMouseLeave={onClose}
        >
            {menuItems.map((item, index) => (
                <div key={index} onClick={item.onClick} style={{ padding: "5px", cursor: "pointer" }}>
                    {item.label}
                </div>
            ))}
        </div>
    );
};

export default ContextMenu;
