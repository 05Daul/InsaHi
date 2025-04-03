import {useEffect, useState} from 'react';
import UserCard from '@/component/admin/UserCard';
import styles from '@/styles/admin/adminPermission.module.css';
import {API_BASE_URL_Employee} from "@/api/api_base_url";

// API 응답 타입
type RawUser = {
  employeeId: string;
  name: string;
  department: string;
  authorityList: { authorityName: string }[];
};

// 프론트에서 사용할 타입
type User = {
  employeeId: string;
  name: string;
  department: string;
  authorityList: string[];
};

export default function AdminPermissionPage() {
  const [admins, setAdmins] = useState<User[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [companyCodeFromToken, setCompanyCodeFromToken] = useState<string>('');
  const [adminPage, setAdminPage] = useState(1);
  const [userPage, setUserPage] = useState(1);
  const adminsPerPage = 5;
  const usersPerPage = 10;

  useEffect(() => {
    if (typeof window === 'undefined') return;

    const companyCode = localStorage.getItem('companyCode') ?? '';
    setCompanyCodeFromToken(companyCode);
    const token = localStorage.getItem('accessToken') || '';

    // 관리자 목록 가져오기
    const fetchAdmins = async () => {
      try {
        const res = await fetch(
            `${API_BASE_URL_Employee}/auth-list?companyCode=${companyCode}&authorityName=ROLE_ADMIN`,
            {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
              },
            }
        );
        const rawData: RawUser[] = await res.json();
        const adminData: User[] = rawData.map((user) => ({
          ...user,
          authorityList: user.authorityList?.map((auth) => auth.authorityName) ?? [],
        }));
        setAdmins(adminData);
      } catch (error) {
        console.error('관리자 사용자 로딩 실패:', error);
      }
    };

    // 일반 사용자 목록 가져오기
    const fetchUsers = async () => {
      try {
        const res = await fetch(
            `${API_BASE_URL_Employee}/auth-list?companyCode=${companyCode}&authorityName=ROLE_USER`,
            {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
              },
            }
        );
        const rawData: RawUser[] = await res.json();
        const userData: User[] = rawData
        .map((user) => ({
          ...user,
          authorityList: user.authorityList?.map((auth) => auth.authorityName) ?? [],
        }))
        .filter((user) => !user.authorityList.includes('ROLE_ADMIN')); // ROLE_ADMIN 없는 사용자만
        setUsers(userData);
      } catch (error) {
        console.error('일반 사용자 로딩 실패:', error);
      }
    };

    fetchAdmins();
    fetchUsers();
  }, []);

  const paginatedAdmins = admins.slice((adminPage - 1) * adminsPerPage, adminPage * adminsPerPage);
  const paginatedUsers = users
  .filter((user) => !admins.some((admin) => admin.employeeId === user.employeeId))
  .slice((userPage - 1) * usersPerPage, userPage * usersPerPage);

  const grantAdmin = async (user: User) => {
    try {
      const token = localStorage.getItem('accessToken');
      const res = await fetch(
          `${API_BASE_URL_Employee}/grant-admin?employeeId=${user.employeeId}`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              ...(token ? {Authorization: `Bearer ${token}`} : {}),
            },
          }
      );
      const message = await res.text();
      alert(message);

      // 상태 업데이트
      const updatedUser = {...user, authorityList: [...user.authorityList, 'ROLE_ADMIN']};
      setAdmins([...admins, updatedUser]);
      setUsers(users.filter((u) => u.employeeId !== user.employeeId));
    } catch (error) {
      console.error('관리자 권한 부여 실패:', error);
      alert('관리자 권한 부여 실패');
    }
  };

  const revokeAdmin = async (user: User) => {
    try {
      const token = localStorage.getItem('accessToken');
      const res = await fetch(
          `${API_BASE_URL_Employee}/delete-admin?employeeId=${user.employeeId}`,
          {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              ...(token ? {Authorization: `Bearer ${token}`} : {}),
            },
          }
      );
      const message = await res.text();
      alert(message);

      // 상태 업데이트
      const updatedUser = {
        ...user,
        authorityList: user.authorityList.filter((auth) => auth !== 'ROLE_ADMIN'),
      };
      setUsers([...users, updatedUser]);
      setAdmins(admins.filter((a) => a.employeeId !== user.employeeId));
    } catch (error) {
      console.error('관리자 권한 제거 실패:', error);
      alert('관리자 권한 제거 실패');
    }
  };

  return (
      <div className={styles.adminContainer}>
        <div className={styles.card1}>
          <div className={styles.cardTitleBar}>관리자 사용자</div>
          <br/>
          <div className={styles.adminList1}>
            {paginatedAdmins.map((admin) => (
                <div className={styles.userCardRow} key={admin.employeeId}>
                  <UserCard
                      user={admin}
                      actionType="revoke"
                      onAction={revokeAdmin}
                  />
                </div>
            ))}
          </div>
          <div style={{display: 'flex', justifyContent: 'center', gap: '10px', marginTop: '1rem'}}>
            {Array.from({length: Math.ceil(admins.length / adminsPerPage)}).map((_, idx) => (
                <button
                    key={idx + 1}
                    onClick={() => setAdminPage(idx + 1)}
                    style={{
                      padding: '5px 10px',
                      backgroundColor: adminPage === idx + 1 ? '#ddd' : '#fff',
                      border: '1px solid #ccc',
                      cursor: 'pointer'
                    }}
                >
                  {idx + 1}
                </button>
            ))}
          </div>
        </div>
        <br/>
        <br/>

        {/* 일반 사용자 목록 */}
        <div className={styles.card1}>
          <div className={styles.cardTitleBar}>일반 사용자</div>
          <br/>
          <div className={styles.adminList1}>
            {paginatedUsers.map((user) => (
                <div className={styles.userCardRow} key={user.employeeId}>
                  <UserCard
                      user={user}
                      actionType="grant"
                      onAction={grantAdmin}
                  />
                </div>
            ))}
          </div>
          <div style={{display: 'flex', justifyContent: 'center', gap: '10px', marginTop: '1rem'}}>
            {Array.from({length: Math.ceil(users.length / usersPerPage)}).map((_, idx) => (
                <button
                    key={idx + 1}
                    onClick={() => setUserPage(idx + 1)}
                    style={{
                      padding: '5px 10px',
                      backgroundColor: userPage === idx + 1 ? '#ddd' : '#fff',
                      border: '1px solid #ccc',
                      cursor: 'pointer'
                    }}
                >
                  {idx + 1}
                </button>
            ))}
          </div>
        </div>
      </div>
  );
}