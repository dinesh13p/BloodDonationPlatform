# Database ERD - Sabaiko Blood Bank

## Entity Relationship Diagram

```
┌─────────────────┐
│      users      │
├─────────────────┤
│ id (PK)         │
│ username (UK)   │
│ password        │
│ email           │
│ full_name       │
│ phone           │
│ role            │
│ status          │
│ created_at      │
│ updated_at      │
└────────┬────────┘
         │
         │ 1:1
         │
    ┌────┴────┐
    │         │
    │         │
┌───▼────┐ ┌──▼──────────┐
│donor_  │ │receiver_   │
│details │ │details     │
├────────┤ ├────────────┤
│id (PK) │ │id (PK)     │
│user_id │ │user_id (FK)│
│blood_  │ │blood_group │
│group   │ │needed      │
│address │ │address     │
│city    │ │city        │
│state   │ │state       │
│zip_code│ │zip_code    │
│availab │ │verified    │
│ility   │ │            │
│stars   │ │            │
└────────┘ └────────────┘
    │
    │ 1:Many
    │
┌───▼──────────────┐
│donation_history  │
├──────────────────┤
│id (PK)           │
│donor_id (FK)     │
│receiver_id (FK)  │
│donation_date     │
│verified_by_      │
│receiver          │
│verified_by_admin │
│notes             │
└──────────────────┘

┌─────────────┐
│  documents   │
├─────────────┤
│id (PK)      │
│user_id (FK) │
│document_    │
│type         │
│file_path    │
│file_name    │
└─────────────┘
```

## Relationships

1. **users → donor_details**: One-to-One
   - One user can have one donor profile
   - Cascade delete

2. **users → receiver_details**: One-to-One
   - One user can have one receiver profile
   - Cascade delete

3. **users → documents**: One-to-Many
   - One user can have multiple documents
   - Cascade delete

4. **users → donation_history (as donor)**: One-to-Many
   - One donor can have multiple donation records
   - Cascade delete

5. **users → donation_history (as receiver)**: One-to-Many
   - One receiver can have multiple donation records
   - Cascade delete

## Tables Description

### users
- Stores all user accounts (Admin, Donor, Receiver)
- Common fields for all user types
- Status: PENDING, APPROVED, RESTRICTED

### donor_details
- Donor-specific information
- Blood group, location, availability
- Stars for verified donations

### receiver_details
- Receiver-specific information
- Blood group needed
- Verification status (blue tick)

### documents
- Uploaded documents (Citizenship, PAN)
- Linked to users

### donation_history
- Records of all donations
- Links donors and receivers
- Verification status tracking


