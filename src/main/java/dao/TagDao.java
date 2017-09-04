package dao;
// operate the database
import api.TagResponse;
import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;// database script language

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tag, int receiptid) {
        int tagsid = dsl
                .insertInto(TAGS, TAGS.TAGNAME)
                .values(tag)
                .returning(TAGS.ID)
                .fetchOne();
        dsl.insertInto(TAGSRECEIPTS, TAGSRECEIPTS.TAGID, TAGSRECEIPTS.RECEIPTID)
                .values(tagsid, receiptid);

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert Tags failed");
        //******not sure whether it should  check these
        //******leaving the recipt id check in TagController

        //return tagsRecord.getId();// what you get after successfully post a receipt
    }

    public Integer getTagIdByName(String tagName ) {
        // find the id of a tag
        int tagid = dsl.selectFrom(TAGS)
                .where(TAGS.TAGNAME.eq(tagName))
                .fetchOne()
                .getId();
        return tagid;
    }
    // undone
    public List<ReceiptsRecord> getReceiptsByTagId(int tagid) {
        // find the receipts associated with tags
        /*return dsl.selectFrom(RECEIPTS)
                .Join(TAGSRECEIPTS)
                .on(RECEIPTS.ID.eq(TAGSRECEIPTS.RECEIPTID))
                .where(TAGSRECEIPTS.TAGID.eq(tagid))
                .fetch();*/
        return dsl.selectFrom(RECEIPTS).fetch();
    }
    // undone
}
