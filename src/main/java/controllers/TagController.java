package controllers;

import api.CreateTagRequest;
import api.TagResponse;
import api.ReceiptResponse;
import dao.TagDao; // ***************************
import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import generated.tables.Receipts;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags/{tag}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;// notice that there is one parameter called receipt

    public TagController() {
        this.tags = tags;
    }

    @PUT
    public void toggleTag(@PathParam("tag") String tagName, Integer receiptId ) {// ?? String or Varchar ??
        if (!receipts.idExists(receiptId)){
            throw new WebApplicationException("receipt id does not exist", Response.Status.NOT_FOUND);
        }
        //return tags.insert(tag.id, tag.tagName);// Put tag for a receipt ******** Untag function
    }

    @GET
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tagName) {
        Integer tagId = tags.getTagIdByName(tagName);
        List<ReceiptsRecord> ReceiptsRecords = Receipts.getReceiptszByTag(tagId);//**** getReceiptsForTag
        return ReceiptsRecords.stream().map(TagResponse::new).collect(toList());// the function
    }
}
