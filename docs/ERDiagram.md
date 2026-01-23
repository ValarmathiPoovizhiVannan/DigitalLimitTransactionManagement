CUSTOMER {
        BIGINT customer_id PK
        VARCHAR name
        VARCHAR mobile
        VARCHAR email
        TIMESTAMP created_at
    }

    ACCOUNT {
        BIGINT account_id PK
        BIGINT customer_id FK
        VARCHAR account_number
        DECIMAL balance
        DECIMAL daily_limit
        DECIMAL monthly_limit
    }

    TRANSACTION_HISTORY {
        BIGINT txn_id PK
        BIGINT account_id FK
        VARCHAR txn_type
        DECIMAL amount
        DATE txn_date
        VARCHAR status
        VARCHAR reason
    }

    CUSTOMER ||--o{ ACCOUNT : owns
    ACCOUNT ||--o{ TRANSACTION_HISTORY : records





