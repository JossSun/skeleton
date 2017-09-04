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
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;
import static generated.Tables.TAGSRECEIPTS;


public class TagDao {
    DSLContext dsl;// database script language

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tag, int receiptid) {
        TagsRecord tagsRecord;
        if (dsl.fetchExists(TAGS,TAGS.TAGNAME.eq(tag))){
            int tagid = dsl.selectFrom(TAGS)
                    .where(TAGS.TAGNAME.eq(tag))
                    .fetchOne()
                    .getId();
            dsl.insertInto(TAGSRECEIPTS, TAGSRECEIPTS.TAGID, TAGSRECEIPTS.RECEIPTID)
                    .values(tagid, receiptid).returning(TAGS.ID)
                    .fetchOne();
        }
        else {
             tagsRecord = dsl
                    .insertInto(TAGS, TAGS.TAGNAME)
                    .values(tag)
                    .returning(TAGS.ID)
                    .fetchOne();

            dsl.insertInto(TAGSRECEIPTS, TAGSRECEIPTS.TAGID, TAGSRECEIPTS.RECEIPTID)
                    .values(tagsRecord.getId(), receiptid).returning(TAGS.ID)
                    .fetchOne();
        }
       // checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert Tags failed");
        //******not sure whether it should  check these
        //******leaving the recipt id check in TagController

        //return tagsRecord.getId();// what you get after successfully post a receipt
    }

    public void delete(int tid, int receiptId){
        dsl.delete(TAGSRECEIPTS)
                .where(TAGSRECEIPTS.TAGID.eq(tid))
                .and(TAGSRECEIPTS.RECEIPTID.eq(receiptId))
                .execute();
    }

    public int getTagIdByName(String tagName ) {
        // find the id of a tag
        int tagid = dsl.selectFrom(TAGS)
                .where(TAGS.TAGNAME.eq(tagName))
                .fetchOne()
                .getId();
        return tagid;
    }
    // undone
    public List<Integer> getReceiptIdbyTagId(int tagid){
        List<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);
        //return test;
        //return test;
        return dsl.selectFrom(TAGSRECEIPTS).where(TAGSRECEIPTS.TAGID.eq(tagid)).fetch(TAGSRECEIPTS.RECEIPTID);

    }

    public Boolean contains(String tagName){
        return dsl.fetchExists(TAGS, TAGS.TAGNAME.eq(tagName));
    }

}
