import React from 'react';

type User = {
  employeeId: string;
  name: string;
  department: string;
  authorityList: string[];
};

type Props = {
  user: User;
  actionType: 'grant' | 'revoke';
  onAction: (user: User) => void;
};

export default function UserCard({user, actionType, onAction}: Props) {

  return (
      <div className="user-card">
        <div className="user-info">
          <span className="name">{user.name} ({user.employeeId})</span>
        </div>
        <button
            className={`action-button ${actionType}`}
            onClick={() => onAction(user)}
        >
          {actionType === 'grant' ? 'Admin 권한 부여' : 'Admin 권한 제거'}
        </button>
      </div>
  );
}