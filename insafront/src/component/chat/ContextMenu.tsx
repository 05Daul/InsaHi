import React from "react";

interface ContextMenuProps {
  x: number;
  y: number;
  visible: boolean;
  onClose: () => void;
  onDelete: () => void;
}

const ContextMenu: React.FC<ContextMenuProps> = ({x, y, visible, onClose, onDelete}) => {
  if (!visible) return null;

  return (
      <div
          style={{
            position: "absolute",
            top: y,
            left: x,
            backgroundColor: "white",
            border: "1px solid #ccc",
            boxShadow: "0 2px 6px rgba(0,0,0,0.2)",
            zIndex: 9999,
            padding: "5px",
          }}
          onClick={onClose}
      >
        <div onClick={onDelete} style={{padding: "8px", cursor: "pointer"}}>
          삭제
        </div>
      </div>
  );
};

export default ContextMenu;