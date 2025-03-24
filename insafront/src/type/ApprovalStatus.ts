// src/type/ApprovalStatus.ts

export enum ApprovalStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  REFERENCES = 'REFERENCES'
}

export type ApprovalStatusType =
    | ApprovalStatus.PENDING
    | ApprovalStatus.APPROVED
    | ApprovalStatus.REJECTED
    | ApprovalStatus.REFERENCES;
