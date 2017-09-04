package dao;
// operate the database
import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGSRECEIPTS;

public class ReceiptDao {
    DSLContext dsl;// database script language

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();// what you get after successfully post a receipt
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

    public boolean idExists(Integer receiptId){
        return dsl.fetchExists(RECEIPTS, RECEIPTS.ID.eq(receiptId));
    }

    public List<Integer> getReceiptsByTagId(int tagid) {
        // find the receipts associated with tags
        return dsl.selectFrom(TAGSRECEIPTS).where(TAGSRECEIPTS.TAGID.eq(tagid)).fetch(TAGSRECEIPTS.RECEIPTID);
    }

    public Boolean contains(int rid){
        return dsl.fetchExists(RECEIPTS, RECEIPTS.ID.eq(rid));
    }
    public Boolean hasTag(int rid, int tid){
        return dsl.fetchExists(TAGSRECEIPTS, TAGSRECEIPTS.RECEIPTID.eq(rid).and(TAGSRECEIPTS.TAGID.eq(tid)));
    }

    public ReceiptsRecord getReceiptFromID(int id){
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.eq(id)).fetchOne();
    }
}
