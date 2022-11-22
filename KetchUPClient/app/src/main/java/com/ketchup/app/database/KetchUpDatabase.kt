package com.ketchup.app.database



import android.graphics.Bitmap
import androidx.room.*
import androidx.room.ForeignKey.NO_ACTION
import java.util.*



@Entity(tableName = Users.TABLE_NAME)
data class Users(
    @ColumnInfo(name = "alias") var alias: String,
    @PrimaryKey var user_id: Int,
    @ColumnInfo(name = "pfp") var pfp: String?,
    @ColumnInfo(name = "status") var status: String?,
    @Ignore var pictureBitmap: Bitmap?
){
    companion object{
        const val TABLE_NAME = "Users"
    }
    constructor(alias: String, user_id: Int, pfp: String?,status: String?) : this(alias, user_id, pfp, status,null){
        this.alias = alias
        this.user_id = user_id
        this.pfp = pfp
        this.status = status
    }
}

@Entity(tableName = Chats.TABLE_NAME)
data class Chats(
    @PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "chat_id")val chat_id: Int,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "isGroup") var isGroup: Boolean

){
    companion object{
        const val TABLE_NAME = "Chats"
    }
}


@Entity(foreignKeys = [ForeignKey(entity = Chats::class,
    parentColumns = arrayOf("chat_id"),
    childColumns = arrayOf("group_id"),
    onDelete = NO_ACTION
)] ,
    tableName = AdditionalChatGroupInformation.TABLE_NAME
)

data class AdditionalChatGroupInformation(
    @PrimaryKey  @ColumnInfo(name = "group_id")val group_id: Int,
    @ColumnInfo(name = "picture") var picture: String?,
){
    companion object{
        const val TABLE_NAME = "AdditionalChatGroupInformation"
    }
}
@Entity(foreignKeys = [ForeignKey(entity = Users::class,
    parentColumns = arrayOf("user_id"),
    childColumns = arrayOf("user_id"),
    onDelete = NO_ACTION
),
    ForeignKey(entity = Chats::class,
        parentColumns = arrayOf("chat_id"),
        childColumns = arrayOf("chat_id"),
        onDelete = NO_ACTION
    )],
    tableName = ChatMembership.TABLE_NAME
)
data class ChatMembership(
    @PrimaryKey var membership_id : Int,
    @ColumnInfo(name = "chat_id")var chat_id: Int,
    @ColumnInfo(name = "role") var role: String,
    @ColumnInfo(name = "user_id") var user_id: Int

){
    companion object{
        const val TABLE_NAME = "ChatMembership"
    }

}



@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatMembership::class,
            parentColumns = arrayOf("membership_id"),
            childColumns = arrayOf("user_sender"),
            onDelete = NO_ACTION
        ), ForeignKey(
            entity = ChatEntries::class,
            parentColumns = arrayOf("entry_id"),
            childColumns = arrayOf("response_to"),
            onDelete = NO_ACTION
        ),
        ForeignKey(
            entity = Chats::class,
            parentColumns = arrayOf("chat_id"),
            childColumns = arrayOf("chat_id"),
            onDelete = NO_ACTION
        )],

    tableName = ChatEntries.TABLE_NAME,
)
data class ChatEntries(
    @PrimaryKey  @ColumnInfo(name = "entry_id")val entry_id: Int,
    @ColumnInfo(name = "text") var text: String?,
    @ColumnInfo(name = "user_sender") var user_sender: Int,
    @ColumnInfo(name = "chat_id") var chat_id: Int,
    @ColumnInfo(name = "response_to") var response_to: Int?,
    @ColumnInfo(name = "date") var date: Date?,
    @ColumnInfo(name = "contains_attachment") var contains_attachment: Boolean,
    @ColumnInfo(name = "readed") var readed : Boolean
){
    companion object{
        const val TABLE_NAME = "ChatEntries"
    }
}

@Entity(tableName = EntryAttachments.TABLE_NAME)
data class EntryAttachments(
    @PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "att_id")val att_id: Int,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "chat_id") var chat_id: Int,
    @ColumnInfo(name = "location") var location: String,
){
    companion object{
        const val TABLE_NAME = "EntryAttachments"
    }
}

data class EntriesWithAttachments(
    @Embedded val entryAttachments: EntryAttachments,
    @Relation(
        parentColumn = "chat_id",
        entityColumn = "chat_id"
    )
    val chatEntries: List<ChatEntries>
)


@Dao
interface UserDao{
    //USERS
    @Insert
    fun insertUser(users: Users)

    @Update
    fun updateUser(vararg users: Users)

    @Delete
    fun deleteUser(vararg users: Users)

    @Query("SELECT * FROM " + Users.TABLE_NAME)
    fun getAllUsers(): List<Users>

    @Query("SELECT user_id FROM " + Users.TABLE_NAME)
    fun getAllUsersId(): List<Int>

    @Query("SELECT user_id FROM " + Users.TABLE_NAME + " WHERE user_id = :user_id")
    fun getUsersId(user_id: Int): Int

    @Query("SELECT MAX(user_id) FROM " + Users.TABLE_NAME)
    fun getMaxUserId(): Int

    @Query("SELECT pfp FROM " + Users.TABLE_NAME + " WHERE user_id = :user_id")
    fun getUsersPFP(user_id: Int): String

    // CHATS
    @Insert
    fun insertChat(chats: Chats)

    @Update
    fun updateChat(vararg chats: Chats)

    @Delete
    fun deleteChat(vararg chats: Chats)

    @Query("SELECT * FROM " + Chats.TABLE_NAME)
    fun getAllChats(): List<Chats>

    @Query("SELECT chat_id FROM " + Chats.TABLE_NAME + " WHERE chat_id = :chat_id")
    fun getChatsId(chat_id: Int): Int

    @Query("Select * from " +Users.TABLE_NAME +" where user_id != :user_id and user_id " +
            "in (select user_id from "+ChatMembership.TABLE_NAME+" where chat_id " +
            "in (select chat_id from "+Chats.TABLE_NAME+" where isGroup = 0))")
    fun getSingleChats(user_id: Int): List<Users>

    @Query("SELECT chat_id FROM " + ChatMembership.TABLE_NAME + " WHERE user_id = :user_id")
    fun getChatId(user_id: Int): Int

    //GROUPCHAT
    @Insert
    fun insertGroup(chatGroup: AdditionalChatGroupInformation)

    @Update
    fun updateGroup(vararg chatGroup: AdditionalChatGroupInformation)

    @Delete
    fun deleteGroup(vararg chats: Chats)

    @Query("SELECT * FROM " + AdditionalChatGroupInformation.TABLE_NAME)
    fun getAllGroups(): List<AdditionalChatGroupInformation>



    //CHATMEMBERSHIPS
    @Insert
    fun insertMemberships(chatMembership: ChatMembership)

    @Update
    fun updateMemberships(vararg chatMembership: ChatMembership)

    @Delete
    fun deleteMemberships(vararg chatMembership: ChatMembership)

    @Query("SELECT * FROM " + ChatMembership.TABLE_NAME)
    fun getAllMemberships(): List<ChatMembership>

    @Query("SELECT membership_id FROM " + ChatMembership.TABLE_NAME + " WHERE membership_id = :membership_id")
    fun getMembershipsId(membership_id: Int): Int

    @Query("SELECT user_id FROM " + ChatMembership.TABLE_NAME + " WHERE user_id = :user_id" +
            " and chat_id in ( select chat_id from "+Chats.TABLE_NAME+" where isGroup = 0 )")
    fun getUserIdWithSingleChat(user_id: Int): Int

    //CHATENTRIES
    @Insert
    fun insertEntries(chatEntries: ChatEntries)

    @Update
    fun updateEntries(vararg chatEntries: ChatEntries)

    @Delete
    fun deleteEntries(vararg chatEntries: ChatEntries)

    @Query("SELECT * FROM " + ChatEntries.TABLE_NAME)
    fun getAllEntries(): List<ChatEntries>

    @Query("SELECT * FROM " + ChatEntries.TABLE_NAME + " WHERE chat_id = :chat_id " +
            "ORDER BY entry_id LIMIT 25")
    fun getAllEntriesFromChat(chat_id: Int): List<ChatEntries>


    //ENTRYATTACHMENTS
    @Insert
    fun insertAttachments(entryAttachments: EntryAttachments)

    @Update
    fun updateAttachments(vararg entryAttachments: EntryAttachments)

    @Delete
    fun deleteAttachments(vararg entryAttachments: EntryAttachments)

    @Transaction
    @Query("SELECT * FROM " + EntryAttachments.TABLE_NAME)
    fun getAllAttachments(): List<EntriesWithAttachments>
}