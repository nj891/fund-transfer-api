package com.finance.fund.transfer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.Identifiable;
import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dao.BaseDao;
import com.finance.fund.transfer.dao.TransactionDao;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.AccountHolder;
import com.finance.fund.transfer.dto.Transaction;
import com.finance.fund.transfer.utils.Bank;
import com.finance.fund.transfer.utils.JsonUtils;
import com.finance.fund.transfer.utils.PaginationParams;
import com.finance.fund.transfer.utils.TransactionPayload;

import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

final class FundTransferService {

    private static final String WITHOUT_DATA = "do_not_generate_data";
    private static final Logger logger = LoggerFactory.getLogger(FundTransferService.class);

    static void start() {
        final String[] args = {WITHOUT_DATA};
        FundTransferService.main(args);
        Spark.awaitInitialization();
    }

    static void startWithData() {
        FundTransferService.main(null);
        Spark.awaitInitialization();
    }

    static void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {
        generateData(args);
        Spark.port(4567);
        Spark.threadPool(10);
        Spark.after((req, res) -> res.type("application/json"));

        initAccountHolderRoutes();
        initAccountRoutes();
        initTransactionRoutes();
        initExceptionsHandling();
    }

    private static void initAccountHolderRoutes() {
        // http://localhost:4567/accountholders?limit=10
        Spark.get("/accountholders", (req, res) -> {
            final BaseDao<AccountHolder> baseDao = Bank.getInstance().getContext().getAccountHolderDao();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(baseDao.getAll(pgParams));
        });

        // http://localhost:4567/accountholders/1
        Spark.get("/accountholders/:id", (req, res) -> {
            final BaseDao<AccountHolder> baseDao = Bank.getInstance().getContext().getAccountHolderDao();
            return JsonUtils.make().toJson(findById(AccountHolder.class, baseDao, req));
        });

        // http://localhost:4567/accountholders/1/accounts
        Spark.get("/accountholders/:id/accounts", (req, res) -> {
            final BaseDao<AccountHolder> baseDao = Bank.getInstance().getContext().getAccountHolderDao();
            final AccountHolder accountHolder = findById(AccountHolder.class, baseDao, req);
            final AccountDao accountsDao = Bank.getInstance().getContext().getAccountsDao();
            return JsonUtils.make().toJson(accountsDao.getByHolder(accountHolder));
        });
    }

    private static void initAccountRoutes() {
        // http://localhost:4567/accounts?limit=10
        Spark.get("/accounts", (req, res) -> {
            final BaseDao<Account> baseDao = Bank.getInstance().getContext().getAccountsDao();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(baseDao.getAll(pgParams));
        });

        // http://localhost:4567/accounts/1
        Spark.get("/accounts/:id", (req, res) -> {
            final BaseDao<Account> baseDao = Bank.getInstance().getContext().getAccountsDao();
            return JsonUtils.make().toJson(findById(Account.class, baseDao, req));
        });

        // http://localhost:4567/accounts/1/transactions?limit=100
        Spark.get("/accounts/:id/transactions", (req, res) -> {
            final BaseDao<Account> baseDao = Bank.getInstance().getContext().getAccountsDao();
            final Account account = findById(Account.class, baseDao, req);
            final TransactionDao transactionDao = Bank.getInstance().getContext().getTransactionDao();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(transactionDao.getByAccount(account, pgParams));
        });
    }

    private static void initTransactionRoutes() {
        // http://localhost:4567/transactions?limit=100
        Spark.get("/transactions", (req, res) -> {
            final BaseDao<Transaction> baseDao = Bank.getInstance().getContext().getTransactionDao();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(baseDao.getAll(pgParams));
        });

        // http://localhost:4567/transactions/1
        Spark.get("/transactions/:id", (req, res) -> {
            final BaseDao<Transaction> baseDao = Bank.getInstance().getContext().getTransactionDao();
            return JsonUtils.make().toJson(findById(Transaction.class, baseDao, req));
        });

        // http://localhost:4567/transactions
        Spark.post("/transactions", (req, res) -> {
            final TransactionPayload payload = JsonUtils.make().fromJson(req.body(), TransactionPayload.class);
            final Transaction trn = Bank.getInstance().transfer(payload);
            res.status(HttpServletResponse.SC_CREATED);
            res.header("Location", "/transactions/" + trn.getId());
            return JsonUtils.make().toJson(trn);
        });
    }

    private static void initExceptionsHandling() {
        Spark.exception(IllegalArgumentException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NullPointerException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NumberFormatException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NoSuchElementException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_NOT_FOUND));
    }

    private static void generateData(String[] args) {
        if (args != null && args.length > 0 && WITHOUT_DATA.equals(args[0].toLowerCase())) {
            return;
        }

        try {
            Bank.getInstance().generateData();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private static void fillErrorInfo(Response res, Exception err, int errCode) {
        res.type("application/json");
        res.status(errCode);
        res.body(JsonUtils.toJson(err, errCode));
    }

    private static <T extends Identifiable> T findById(Class<T> type, BaseDao<T> baseDao, Request req) {
        final Long id = getId(req);
        final T t = baseDao.getById(id);
        if (t.isNotValid()) {
            throw new NoSuchElementException(String.format("%s with id %d is not found", type.getSimpleName(), id));
        }
        return t;
    }

    private static Long getId(Request req) {
        return Long.valueOf(req.params("id"), 10);
    }
}
