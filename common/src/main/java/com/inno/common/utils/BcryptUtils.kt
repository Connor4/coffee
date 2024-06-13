package com.inno.common.utils

import org.mindrot.jbcrypt.BCrypt

object BcryptUtils {

    fun hashPassword(psw: String): String {
        return BCrypt.hashpw(psw, BCrypt.gensalt())
    }

    fun checkPassword(psw: String, hashedPsw: String): Boolean {
        return BCrypt.checkpw(psw, hashedPsw)
    }

}