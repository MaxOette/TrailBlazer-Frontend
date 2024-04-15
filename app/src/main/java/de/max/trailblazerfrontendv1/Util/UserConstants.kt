package de.max.trailblazerfrontendv1.Util

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class UserConstants {
     companion object {
         var refreshToken: String = ""
         var accessToken: String = ""
         var email: String = ""
         var userLat: Double = 48.8092627
         var userLng: Double = 8.9775588
         var cameraPosition = CameraPosition.fromLatLngZoom(LatLng(userLat, userLng), 14f)

//         fun getRefreshToken(): String{
//             return refreshToken
//         }
//
//         fun setRefreshToken(token: String){
//             refreshToken = token
//         }
//
//         fun getAccesToken(): String{
//             return accessToken
//         }
//
//         fun setAccesToken(token: String){
//             accessToken = token
//         }
//
//         fun getEmail(): String{
//             return email
//         }
//
//         fun setEmail(mail: String){
//             email = mail
//         }
     }
}