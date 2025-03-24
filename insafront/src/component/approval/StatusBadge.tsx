import React from 'react';
// import styles from '@/styles/approval/Documents.module.css';
import {ApprovalStatus} from "@/type/ApprovalStatus";

interface StatusBadgeProps {
  status: ApprovalStatus;
}

const StatusBadge: React.FC<StatusBadgeProps> = ({status}) => {
  const getBadgeColor = () => {
    switch (status) {
      case ApprovalStatus.APPROVED:
        return 'green';
      case ApprovalStatus.REJECTED:
        return 'red';
      case ApprovalStatus.PENDING:
        return 'orange';
      case ApprovalStatus.REFERENCES:
        return 'gray';
      default:
        return 'gray';
    }
  };

  const getBadgeLabel = () => {
    switch (status) {
      case ApprovalStatus.APPROVED:
        return '승인됨';
      case ApprovalStatus.REJECTED:
        return '반려됨';
      case ApprovalStatus.PENDING:
        return '진행 중';
      case ApprovalStatus.REFERENCES:
        return '참조자';
      default:
        return '알 수 없음';
    }
  };

  return (
      <span
          style={{
            padding: '4px 8px',
            backgroundColor: getBadgeColor(),
            color: 'white',
            borderRadius: '4px',
            fontSize: '12px'
          }}
      >
      {getBadgeLabel()}
    </span>
  );
};

export default StatusBadge;
