package com.mobiwarez.laki.sville.converter


//import com.example.Laki.myapplication.backend.Item
import android.location.Location
import com.example.laki.myapplication.backend.myApi.model.Comment
import com.example.laki.myapplication.backend.myApi.model.Item
import com.example.laki.myapplication.backend.myApi.model.ItemStatusResponse
import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel
import com.mobiwarez.laki.sville.models.CommentsViewModel
import com.mobiwarez.laki.sville.ui.models.ToyViewModel
import com.mobiwarez.laki.sville.ui.toys.sharedtoys.StatusResponse
import com.mobiwarez.laki.sville.util.IdGenerator
import java.util.*

/**
 * Created by Laki on 01/11/2017.
 */

class EngineToModelConverter{

    fun convertToStatusResponse(iResponse: ItemStatusResponse): StatusResponse{
        return StatusResponse(true, iResponse.takerName, iResponse.takerUUID, iResponse.timeTaken)
    }


    fun convertCommentToCommentViewModel(comment: Comment): CommentsViewModel{
        val commentModel = CommentsViewModel(
                comment.commentId,
                comment.commentorName,
                comment.commentedName,
                comment.commentorAvatarUrl,
                comment.commentedAvatarUrl,
                comment.commentedUUID,
                comment.comment,
                comment.timePosted

        )

        return commentModel
    }

    fun convertItemToToyModel (item: Item, cLocation: Location):ToyViewModel {

        return ToyViewModel(
                item.id,
                item.title,
                item.itemDescription,
                item.itemCategory,
                item.itemAgeGroup,
                item.itemUrl,
                item.itemStatus,
                item.timePosted,
                item.giverName,
                item.givenLocation,
                item.giverAvatarUrl,
                item.latitude,
                item.longitude,
                item.pickLocation,
                item.pickTime,
                item.giverUUID,
                cLocation
        )

    }

    fun convertToyModelToReceivedModel(model: ToyViewModel): ReceivedToyModel{
        val recievedItems = ReceivedToyModel()
        recievedItems.avatarUrl = model.avatarUrl
        recievedItems.giverName = model.giverName
        recievedItems.timePosted = model.timePosted
        recievedItems.toyAgeGroup = model.toyAgeGroup
        recievedItems.timeReceived = IdGenerator.getTimeStamp()
        recievedItems.toyDescription = model.toyDescription
        recievedItems.toyGiven = 1
        recievedItems.toyName = model.toyname
        recievedItems.toyPickUpLocation = model.pickLocation
        recievedItems.toyPickUpTime = model.pickTime
        recievedItems.toyUrl = model.toyImageUrl
        recievedItems.giverUuid = model.giverUUID

        return recievedItems

    }
}
