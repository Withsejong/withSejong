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
    val totalElment :Int,
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
    val createdAt:String
)

