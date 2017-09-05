package controllers;

import api.CreateTagRequest;
import api.TagResponse;
import api.ReceiptResponse;
import dao.TagDao;
import dao.ReceiptDao;
import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsreceiptsRecord;
import generated.tables.Receipts;
import generated.tables.Tags;
import generated.tables.Tagsreceipts;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.ArrayList;

import static java.util.stream.Collectors.toList;

@Path("/tags/{tag}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;
    final ReceiptDao receipts;

    public TagController(TagDao tags, ReceiptDao receipts) {
        this.tags = tags;
        this.receipts = receipts;
    }

    @PUT
    public String toggleTag(@PathParam("tag") String tagName, int receiptId ) {

        if (receipts.contains(receiptId)){
            if(!tags.contains(tagName)){
                tags.insert(tagName, receiptId);
                return "new tag";
            }
            else{
                int tid = tags.getTagIdByName(tagName);

                if(!receipts.hasTag(receiptId, tid)){
                    tags.insert(tagName, receiptId);
                    return "ok tag";
                }
                else{
                    tags.delete(tid, receiptId);
                    return "ok untag";
                }
            }
        }
        else{
            return "no receipt id";
        }
    }

    @GET
    public List<ReceiptResponse> getReceipts(@NotNull @PathParam("tag") String tagName) {
        //if(!tags.tagExists(tag)){new WebApplicationException("tag does not exist", Response.Status.NOT_FOUND);}

            Integer tagId = tags.getTagIdByName(tagName);
            List<Integer> receiptIDs = receipts.getReceiptsByTagId(tagId);
            List<ReceiptsRecord> ReceiptsRecords = new ArrayList<ReceiptsRecord>();
            for (int id: receiptIDs) {
                ReceiptsRecords.add(receipts.getReceiptFromID(id));
            }
            return ReceiptsRecords.stream().map(ReceiptResponse::new).collect(toList());// the function

    }
}
