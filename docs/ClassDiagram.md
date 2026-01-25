```mermaid
classDiagram
%% =======================
%% MODEL LAYER
%% =======================
class Account {
    -String accountId
    -long customerId
    -String accountNumber
    -String customerName
    -BigDecimal balance
    -BigDecimal dailyLimit
    -BigDecimal monthlyLimit
    +getters()
    +setters()
}

class Transaction {
    -long txnId
    -long accountId
    -String txnType
    -BigDecimal amount
    -LocalDate txnDate
    -String status
    -String reason
}

class User {
    -String username
    -String password
}

%% =======================
%% DAO LAYER
%% =======================
class AccountDao {
    +getAccountByAccountNumber(String) Account
    +updateBalanceByAccountNumber(String, BigDecimal)
    +createAccount(Account) String
}

class CustomerDao {
    +createCustomer(String, String, String, String, String) long
}

class TransactionDao {
    +insertTransaction(long, String, BigDecimal, String, String)
}

class UserDao {
    +getPassword(String) String
}

%% =======================
%% SERVICE LAYER
%% =======================
class TransactionService {
    -AccountDao accountDao
    -TransactionDao transactionDao
    +processTransaction(String, String, BigDecimal) String
    +getAccountDetails(String) Account
}

%% =======================
%% SERVLET LAYER
%% =======================
class LoginServlet {
    +doPost(HttpServletRequest, HttpServletResponse)
}

class LogoutServlet {
    +doPost(HttpServletRequest, HttpServletResponse)
}

class TransactionServlet {
    -TransactionService service
    +doPost(HttpServletRequest, HttpServletResponse)
    +doGet(HttpServletRequest, HttpServletResponse)
}

class UserRegistrationServlet {
    -CustomerDao customerDao
    -AccountDao accountDao
    +doPost(HttpServletRequest, HttpServletResponse)
}

%% =======================
%% FILTER
%% =======================
class AuthFilter {
    +doFilter(ServletRequest, ServletResponse, FilterChain)
}

%% =======================
%% UTILITIES
%% =======================
class DBConnectionUtil {
    <<Singleton>>
    +getConnection() Connection
    +contextInitialized(ServletContextEvent)
}

class AccountNumberGenerator {
    <<Utility>>
    +generateAccountNumber() String
}

class PasswordUtil {
    <<Utility>>
    +hash(String) String
    +match(String, String) boolean
}

%% =======================
%% EXCEPTION
%% =======================
class AccessException {
    <<RuntimeException>>
}

%% =======================
%% RELATIONSHIPS
%% =======================
TransactionService --> AccountDao
TransactionService --> TransactionDao

AccountDao --> Account
TransactionDao --> Transaction
UserDao --> User

UserRegistrationServlet --> CustomerDao
UserRegistrationServlet --> AccountDao

TransactionServlet --> TransactionService
LoginServlet --> UserDao
LoginServlet --> PasswordUtil

AccountDao --> DBConnectionUtil
CustomerDao --> DBConnectionUtil
TransactionDao --> DBConnectionUtil
UserDao --> DBConnectionUtil

AccountDao --> AccountNumberGenerator
LoginServlet --> PasswordUtil

AccountDao --> AccessException
CustomerDao --> AccessException
TransactionDao --> AccessException

AuthFilter --> HttpSession
