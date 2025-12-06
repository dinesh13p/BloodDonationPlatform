# UML Class Diagram - Sabaiko Blood Bank

## Entity Classes

```
┌─────────────────────────────────┐
│            User                 │
├─────────────────────────────────┤
│ - id: Long                      │
│ - username: String              │
│ - password: String              │
│ - email: String                │
│ - fullName: String              │
│ - phone: String                 │
│ - role: UserRole                │
│ - status: UserStatus            │
│ - createdAt: LocalDateTime      │
│ - updatedAt: LocalDateTime      │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│        DonorDetails             │
├─────────────────────────────────┤
│ - id: Long                      │
│ - user: User (1:1)             │
│ - bloodGroup: BloodGroup        │
│ - dateOfBirth: LocalDate        │
│ - address: String               │
│ - city: String                  │
│ - state: String                 │
│ - zipCode: String               │
│ - availability: Boolean          │
│ - medicalHistory: String        │
│ - profileImage: String          │
│ - stars: Integer                │
│ - lastDonationDate: LocalDate    │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│       ReceiverDetails           │
├─────────────────────────────────┤
│ - id: Long                      │
│ - user: User (1:1)             │
│ - bloodGroupNeeded: BloodGroup  │
│ - address: String               │
│ - city: String                  │
│ - state: String                 │
│ - zipCode: String                │
│ - verified: Boolean              │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│          Document               │
├─────────────────────────────────┤
│ - id: Long                      │
│ - user: User (Many:1)          │
│ - documentType: DocumentType    │
│ - filePath: String               │
│ - fileName: String              │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│      DonationHistory            │
├─────────────────────────────────┤
│ - id: Long                      │
│ - donor: User (Many:1)          │
│ - receiver: User (Many:1)       │
│ - donationDate: LocalDateTime   │
│ - verifiedByReceiver: Boolean   │
│ - verifiedByAdmin: Boolean      │
│ - notes: String                 │
└─────────────────────────────────┘
```

## Service Classes

```
┌─────────────────────────────────┐
│         UserService             │
├─────────────────────────────────┤
│ + loadUserByUsername()         │
│ + createUser()                  │
│ + findByUsername()              │
│ + findByEmail()                 │
│ + findById()                    │
│ + findByRole()                  │
│ + findByStatus()                │
│ + updateUserStatus()            │
│ + deleteUser()                  │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│        DonorService             │
├─────────────────────────────────┤
│ + createDonorDetails()          │
│ + findByUser()                  │
│ + findByBloodGroup()            │
│ + findByBloodGroupAndAvailability()│
│ + findByCity()                  │
│ + findByState()                 │
│ + updateDonorDetails()          │
│ + updateProfileImage()          │
│ + addStar()                     │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│       ReceiverService           │
├─────────────────────────────────┤
│ + createReceiverDetails()       │
│ + findByUser()                  │
│ + verifyReceiver()              │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│         AdminService            │
├─────────────────────────────────┤
│ + getPendingUsers()             │
│ + getPendingDonors()            │
│ + getPendingReceivers()         │
│ + approveUser()                 │
│ + rejectUser()                   │
│ + restrictUser()                │
│ + addStarToDonor()               │
│ + getAllDonors()                │
│ + getAllReceivers()             │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│    DonationHistoryService       │
├─────────────────────────────────┤
│ + createDonationHistory()       │
│ + verifyByReceiver()            │
│ + verifyByAdmin()               │
│ + getDonorHistory()            │
│ + getReceiverHistory()          │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│      FileUploadService          │
├─────────────────────────────────┤
│ + uploadFile()                  │
│ + deleteFile()                  │
└─────────────────────────────────┘
```

## Controller Classes

```
┌─────────────────────────────────┐
│        AuthController           │
├─────────────────────────────────┤
│ + showLoginPage()               │
│ + showDonorRegistrationForm()   │
│ + registerDonor()               │
│ + showReceiverRegistrationForm()│
│ + registerReceiver()            │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│        AdminController          │
├─────────────────────────────────┤
│ + showDashboard()               │
│ + showPendingUsers()            │
│ + showVerifiedUsers()           │
│ + approveUser()                 │
│ + rejectUser()                  │
│ + restrictUser()                │
│ + addStarToDonor()              │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│        DonorController          │
├─────────────────────────────────┤
│ + showProfile()                 │
│ + showEditForm()                │
│ + updateProfile()               │
│ + deleteAccount()               │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│      ReceiverController         │
├─────────────────────────────────┤
│ + showProfile()                 │
│ + searchDonors()                │
│ + verifyDonation()              │
│ + deleteAccount()                │
└─────────────────────────────────┘
```

## Repository Interfaces

```
┌─────────────────────────────────┐
│      UserRepository             │
│  extends JpaRepository<User>    │
├─────────────────────────────────┤
│ + findByUsername()              │
│ + findByEmail()                 │
│ + findByRole()                  │
│ + findByStatus()                │
│ + findByRoleAndStatus()         │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│   DonorDetailsRepository        │
│  extends JpaRepository<DonorDetails>│
├─────────────────────────────────┤
│ + findByUser()                  │
│ + findByBloodGroup()            │
│ + findByBloodGroupAndAvailability()│
│ + findByCity()                  │
│ + findByState()                 │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  ReceiverDetailsRepository      │
│  extends JpaRepository<ReceiverDetails>│
├─────────────────────────────────┤
│ + findByUser()                  │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│     DocumentRepository          │
│  extends JpaRepository<Document>│
├─────────────────────────────────┤
│ + findByUser()                  │
│ + findByUserAndDocumentType()   │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  DonationHistoryRepository      │
│  extends JpaRepository<DonationHistory>│
├─────────────────────────────────┤
│ + findByDonor()                 │
│ + findByReceiver()              │
└─────────────────────────────────┘
```

## Enum Classes

```
┌─────────────────────────────────┐
│         UserRole                │
├─────────────────────────────────┤
│ ADMIN                           │
│ DONOR                           │
│ RECEIVER                        │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│        UserStatus               │
├─────────────────────────────────┤
│ PENDING                         │
│ APPROVED                        │
│ RESTRICTED                      │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│        BloodGroup               │
├─────────────────────────────────┤
│ A_POSITIVE, A_NEGATIVE          │
│ B_POSITIVE, B_NEGATIVE          │
│ AB_POSITIVE, AB_NEGATIVE        │
│ O_POSITIVE, O_NEGATIVE          │
│ + getDisplayName()              │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│       DocumentType              │
├─────────────────────────────────┤
│ CITIZENSHIP                     │
│ PAN                             │
└─────────────────────────────────┘
```

## DTO Classes

```
┌─────────────────────────────────┐
│      RegistrationDTO            │
├─────────────────────────────────┤
│ - username: String              │
│ - password: String               │
│ - email: String                 │
│ - fullName: String               │
│ - phone: String                  │
│ - bloodGroup: BloodGroup         │
│ - bloodGroupNeeded: BloodGroup   │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│       DonorUpdateDTO            │
├─────────────────────────────────┤
│ - address: String                │
│ - city: String                   │
│ - state: String                  │
│ - zipCode: String                │
│ - availability: Boolean          │
│ - medicalHistory: String        │
│ - dateOfBirth: LocalDate        │
│ - bloodGroup: BloodGroup         │
└─────────────────────────────────┘
```


