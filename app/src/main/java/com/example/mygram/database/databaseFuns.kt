package com.example.mygram.database

import android.net.Uri
import com.example.mygram.R
import com.example.mygram.models.CommonModel
import com.example.mygram.models.UserModel
import com.example.mygram.utilits.APP_ACTIVITY
import com.example.mygram.utilits.AppValueEventListener
import com.example.mygram.utilits.TYPE_GROUP
import com.example.mygram.utilits.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.HashMap

fun initFirebase() {
    AUTH =
        FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    USER =
        UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    )
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    )
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER =
                it.getValue(UserModel::class.java)
                    ?: UserModel()
            if (USER.fullname.isEmpty()) {
                USER.fullname =
                    USER.username
            }
            function()
        })
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(
            AppValueEventListener {
                it.children.forEach { snapshot ->
                    arrayContacts.forEach { contact ->
                        if (snapshot.key == contact.phone) {
                            REF_DATABASE_ROOT.child(
                                NODE_PHONES_CONTACTS
                            ).child(CURRENT_UID)
                                .child(snapshot.value.toString())
                                .child(CHILD_ID)
                                .setValue(snapshot.value.toString())
                                .addOnFailureListener { it.message.toString() }
                            REF_DATABASE_ROOT.child(
                                NODE_PHONES_CONTACTS
                            ).child(CURRENT_UID)
                                .child(snapshot.value.toString())
                                .child(CHILD_FULLNAME)
                                .setValue(contact.fullname)
                                .addOnFailureListener { it.message.toString() }
                        }
                    }
                }
            })
    }
}

public fun sendMessage(
    message: String,
    receivingUserId: String,
    typeText: String,
    function: () -> Unit
) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] =
        CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] =
        ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

public fun sendMessageToGroup(
    message: String,
    groupID: String,
    typeText: String,
    function: () -> Unit
) {
    val refMessages = "$NODE_GROUPS/$groupID/$NODE_MESSAGES"
    val messageKey = REF_DATABASE_ROOT.child(refMessages).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] =
        CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] =
        ServerValue.TIMESTAMP

    REF_DATABASE_ROOT
        .child(refMessages)
        .child(messageKey.toString())
        .updateChildren(mapMessage)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun updateCurrentUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    ).child(CHILD_USERNAME)
        .setValue(newUserName)
        .addOnSuccessListener {
            deleteOldUsername(
                newUserName
            )
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

private fun deleteOldUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(
        USER.username
    ).removeValue()
        .addOnSuccessListener {
            showToast(
                APP_ACTIVITY.getString(
                    R.string.toast_data_update
                )
            )
            USER.username = newUserName
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    ).child(CHILD_BIO)
        .setValue(newBio)
        .addOnSuccessListener {
            showToast(
                APP_ACTIVITY.getString(
                    R.string.toast_data_update
                )
            )
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun setNameToDatabase(fullname: String) {
    REF_DATABASE_ROOT.child(
        NODE_USERS
    ).child(CURRENT_UID).child(
        CHILD_FULLNAME
    )
        .setValue(fullname)
        .addOnSuccessListener {
            showToast(
                APP_ACTIVITY.getString(
                    R.string.toast_data_update
                )
            )
            USER.fullname = fullname
            APP_ACTIVITY.mAppDrawer.updateHeader()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageAsFile(
    receivingUserId: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] =
        CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] =
        ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getMessageKey(id: String) = REF_DATABASE_ROOT.child(
    NODE_MESSAGES
).child(CURRENT_UID)
    .child(id).push().key.toString()

fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receivedId: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT.child(
        FOLDER_FILES
    ).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFile(
                receivedId,
                it,
                messageKey,
                typeMessage,
                filename
            )
        }
    }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun saveToMainList(id: String, type: String) {
    val refUser = "$NODE_MAIN_LIST/$CURRENT_UID/$id"
    val refReceived = "$NODE_MAIN_LIST/$id/$CURRENT_UID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = type

    mapReceived[CHILD_ID] = CURRENT_UID
    mapReceived[CHILD_TYPE] = type

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived
    REF_DATABASE_ROOT.updateChildren(commonMap)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun deleteChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(id)
        .removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnCompleteListener { clearChat(id, function) }
}

fun clearChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(id)
        .removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnCompleteListener { function() }
}

fun createGroupToDatabase(
    nameGroup: String,
    uri: Uri,
    listContacts: List<CommonModel>,
    function: () -> Unit
) {
    val keyGroup = REF_DATABASE_ROOT.child(NODE_GROUPS).push().key.toString()
    val path = REF_DATABASE_ROOT.child(NODE_GROUPS).child(keyGroup)
    val pathStorage = REF_STORAGE_ROOT.child(FOLDER_GROUPS_IMAGE).child(keyGroup)
    val mapData = hashMapOf<String, Any>()
    val mapMembers = hashMapOf<String, Any>()

    mapData[CHILD_ID] = keyGroup
    mapData[CHILD_NAME] = nameGroup
    listContacts.forEach {
        mapMembers[it.id] = USER_MEMBER
    }
    mapMembers[CURRENT_UID] = USER_CREATOR
    mapData[NODE_MEMBERS_GROUP] = mapMembers
    mapData[CHILD_PHOTO_URL] = "empty"
    path.updateChildren(mapData)
        .addOnSuccessListener {
            if (uri != Uri.EMPTY) {
                putFileToStorage(uri, pathStorage) {
                    getUrlFromStorage(pathStorage) {
                        path.child(CHILD_PHOTO_URL).setValue(it)
                        addGroupToMainList(mapData, listContacts) {
                            function()
                        }
                    }
                }
            } else {
                addGroupToMainList(mapData, listContacts) {
                    function()
                }
            }
        }
        .addOnFailureListener { it.message }
}

fun addGroupToMainList(
    mapData: HashMap<String, Any>,
    listContacts: List<CommonModel>,
    function: () -> Unit
) {
    val path = REF_DATABASE_ROOT.child(NODE_MAIN_LIST)
    val map = hashMapOf<String, Any>()
    map[CHILD_ID] = mapData[CHILD_ID].toString()
    map[CHILD_TYPE] = TYPE_GROUP
    listContacts.forEach {
        path.child(it.id).child(map[CHILD_ID].toString()).updateChildren(map)
    }
    path.child(CURRENT_UID).child(map[CHILD_ID].toString()).updateChildren(map)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

