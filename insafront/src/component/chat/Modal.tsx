// import React from 'react';
//
// function Modal({ visible, onClose, children }) {
//     if (!visible) return null; // 모달이 안보이면 렌더X
//
//     // 모달 영역 바깥을 클릭하면 onClose
//     return (
//         <div
//             style={{
//                 position: 'fixed',
//                 zIndex: 1000,
//                 left: 0, top: 0, right: 0, bottom: 0,
//                 backgroundColor: 'rgba(0,0,0,0.5)',
//             }}
//             onClick={onClose}
//         >
//             <div
//                 style={{
//                     width: 300,
//                     margin: '10% auto',
//                     backgroundColor: '#fff',
//                     padding: 20,
//                     borderRadius: 8
//                 }}
//                 onClick={(e) => e.stopPropagation()}
//             >
//                 {children}
//             </div>
//         </div>
//     );
// }
//
// export default Modal;
