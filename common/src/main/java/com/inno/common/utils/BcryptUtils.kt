package com.inno.common.utils

object BcryptUtils {

    fun hashPassword(psw: String): String {
//        return BCrypt.hashpw(psw, BCrypt.gensalt())
        return psw
    }

    fun checkPassword(psw: String, hashedPsw: String): Boolean {
//        return BCrypt.checkpw(psw, hashedPsw)
        return psw == hashedPsw
    }

}