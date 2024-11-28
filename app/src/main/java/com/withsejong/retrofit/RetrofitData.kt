package com.withsejong.retrofit

data class UserSignupResponse(
    val name: String,
    val studentId: String,

)

data class SejongAuthResponse(
    val msg:String,
    val result:SejongAuthResponseResultJson
)
data class SejongAuthResponseResultJson(
    val authenticator:String,
    val code:String,
    val is_auth:String,
    val status_code:String,
    val success:String,
    val body:SejongAuthResponseResultBodyJson
)
data class SejongAuthResponseResultBodyJson(
    val name:String,
    val major:String
)

data class LoginResponse(
    val studentId: String,
    val nickname: String,
    val major: String,
    val authToken : LoginResponseAuthToken
)
data class LoginResponseAuthToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)
data class ChangeForgotPassword(
    val studentId: String,
    val nickname: String
)

data class DeleteAccountResponse(
    val studentId: String,
    val name:String
)

data class CheckStudentIdResponse(
    val isSigned:Boolean?,
    val isDeleted:Boolean?
)

data class UpdateUserInfoResponse(
    val studentId: String,
    val nickname: String,
    val major: String
)
data class LoadFaqResponse(
    val title:String,
    val context : String
)

data class RefreshTokenResponse(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)
data class LogoutResponse(
    val studentId: String,
    val name: String,
    val nickname: String,
    val major: String
)

data class MakePostResponse(
    val title:String,
    val context : String,
    val studentId: String
)

data class LoadPostResponse(
    val currentPage : Int,
    val totalPages : Int,
    val totalElements :Int,
    val boardFindResponseDtoList : ArrayList<BoardFindResponseDtoList>
)

data class BoardFindResponseDtoList(
    val id:Int,
    val title:String,
    val price:Int,
    val content:String,
    val studentId: String,
    val createdAt:String,
    val status:Int,
    val image:List<Image>,
    val tag:ArrayList<Tag>,
    val nickname: String,
    val major:String
)
data class Image(
    val id:Int,
    val url : String
)
data class Tag(
    val id:Int,
    val category: String
)
data class LoadChattingRoomResponse(
    val roomId:Int,
    val publisher:String,
    val subscriber:String,
    val createdAt:String,
    val boardTitle: String,
    var lastmsg:String
)
data class LoadingChattingResponse(
    val viewType:Int,
    val roomId:Int,
    val message:String,
    val sender:String,
    val createdAt:String,
)

data class MakeChattingRoomResponse(
    val id:Int,
    val publisher:String,
    val subscriber:String,
    val createdAt : String,
    val boardTitle:String,
    val isCreated:Boolean
)

data class PostReportResponse(
    val title:String,
    val content:String,
    val boardId :Int
)

data class DeletePostResponse(

    val date:String,
    val content:String,
    val title:String,
    val price:Int
)

data class LoadLastChatResponse(

    val roomId:Int,
    val message:String,
    val sender:String,
    val createdAt:String

)

data class FcmResponse(
    val redirectBoolean: Boolean,
    val successful:Boolean
)

