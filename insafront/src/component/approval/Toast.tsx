import React, {useEffect} from 'react';
import ReactDOM from 'react-dom';
import styles from '@/styles/approval/Documents.module.css';

interface ToastProps {
  message: string;
  onClose: () => void;
  duration?: number;
}

const Toast: React.FC<ToastProps> = ({message, onClose, duration = 3000}) => {
  useEffect(() => {
    const timer = setTimeout(onClose, duration);
    return () => clearTimeout(timer);
  }, [onClose, duration]);

  return ReactDOM.createPortal(
      <div className={styles.toastContainer}>
        <p>{message}</p>
      </div>,
      document.body
  );
};

export default Toast;
