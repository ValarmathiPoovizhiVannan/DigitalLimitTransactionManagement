```mermaid
SequenceDiagram
    participant Client
    participant AuthFilter
    participant LoginServlet
    participant UserRegistrationServlet
    participant TransactionServlet
    participant LogoutServlet
    participant TransactionService
    participant AccountDao
    participant CustomerDao
    participant UserDao
    participant TransactionDao
    participant PasswordUtil
    participant DB as Database

    %% ======================
    %% USER REGISTRATION
    %% ======================
    Client ->> UserRegistrationServlet: POST /register
    UserRegistrationServlet ->> PasswordUtil: hash(password)
    UserRegistrationServlet ->> CustomerDao: createCustomer()
    CustomerDao ->> DB: INSERT customer
    DB -->> CustomerDao: customerId
    CustomerDao -->> UserRegistrationServlet: customerId

    UserRegistrationServlet ->> AccountDao: createAccount()
    AccountDao ->> DB: INSERT account
    DB -->> AccountDao: accountNumber
    AccountDao -->> UserRegistrationServlet: accountNumber
    UserRegistrationServlet -->> Client: USER_CREATED + ACCOUNT_NUMBER

    %% ======================
    %% LOGIN
    %% ======================
    Client ->> LoginServlet: POST /login
    LoginServlet ->> UserDao: getPassword(username)
    UserDao ->> DB: SELECT password
    DB -->> UserDao: hashedPassword
    UserDao -->> LoginServlet: hashedPassword
    LoginServlet ->> PasswordUtil: match(password, hash)

    alt login success
        LoginServlet -->> Client: LOGIN_SUCCESS
    else login failed
        LoginServlet -->> Client: INVALID_USERNAME_PASSWORD
    end

    %% ======================
    %% TRANSACTION (AUTH FILTER)
    %% ======================
    Client ->> AuthFilter: POST /upi/transaction
    AuthFilter ->> AuthFilter: validate session

    alt session valid
        AuthFilter ->> TransactionServlet: allow request
    else session invalid
        AuthFilter -->> Client: 401 Unauthorized
    end

    TransactionServlet ->> TransactionService: processTransaction()
    TransactionService ->> AccountDao: getAccountByAccountNumber()
    AccountDao ->> DB: SELECT account
    DB -->> AccountDao: Account
    AccountDao -->> TransactionService: Account

    alt DEBIT
        alt insufficient balance or limit exceeded
            TransactionService ->> TransactionDao: insertTransaction(REJECTED)
            TransactionDao ->> DB: INSERT transaction_history
        else success
            TransactionService ->> AccountDao: updateBalance()
            AccountDao ->> DB: UPDATE account
            TransactionService ->> TransactionDao: insertTransaction(SUCCESS)
            TransactionDao ->> DB: INSERT transaction_history
        end
    else CREDIT
        TransactionService ->> AccountDao: updateBalance()
        AccountDao ->> DB: UPDATE account
        TransactionService ->> TransactionDao: insertTransaction(SUCCESS)
        TransactionDao ->> DB: INSERT transaction_history
    end

    TransactionService -->> TransactionServlet: result
    TransactionServlet -->> Client: SUCCESS / FAILED

    %% ======================
    %% GET ACCOUNT DETAILS
    %% ======================
    Client ->> TransactionServlet: GET /transaction?accountNumber
    TransactionServlet ->> TransactionService: getAccountDetails()
    TransactionService ->> AccountDao: getAccountByAccountNumber()
    AccountDao ->> DB: SELECT account
    DB -->> AccountDao: Account
    AccountDao -->> TransactionService: Account
    TransactionService -->> TransactionServlet: Account
    TransactionServlet -->> Client: Account Details

    %% ======================
    %% LOGOUT
    %% ======================
    Client ->> LogoutServlet: POST /logout
    LogoutServlet ->> LogoutServlet: invalidate session
    LogoutServlet -->> Client: LOGOUT_SUCCESS

